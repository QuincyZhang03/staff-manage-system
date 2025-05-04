package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.Entry;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.blockchain.Transaction;
import com.zjquincy.ncu.data.Department;
import com.zjquincy.ncu.data.IData;
import com.zjquincy.ncu.data.Staff;
import com.zjquincy.ncu.net.NetUtility;
import com.zjquincy.ncu.net.response.CreateResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * 新增数据请求格式：
 * String request_type = "CREATE"
 * String username 用户名
 * String type 新增的数据类型，可以为"staff"或"department"
 * JsonString[] dataList 新增的具体数据列表，应当可以被反序列化为IData接口的实例。客户端应发送json文本数组，而非json对象数组。
 * */
public class CreateRequest extends AbstractRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("type")
    private String type;
    @SerializedName("dataList")
    private String[] dataList;//注意这个数据是一个json列表，里面有若干个数据，要逐个反序列化

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            User user = User.fetchUser(username);
            if (user == null) {
                NetUtility.sendResponse(exchange, new CreateResponse("未找到用户：" + username));//一般情况下不会出现这种情况
            } else if (user.getLevel() < User.OPERATOR) {//用户权限不足
                NetUtility.sendResponse(exchange, new CreateResponse("权限不足！"));
            } else {//解析出新增数据的类型
                Class<? extends IData> dataType = getDataType();
                if (dataType == null) {
                    NetUtility.sendResponse(exchange, new CreateResponse("数据类型解析失败：" + type));
                } else {//数据类型合法，开始插入
                    Connection connection = DriverManager.getConnection(Entry.DB_URL, user.getDBUsername(), user.getDBPassword());
                    int inserted = 0;
                    StringBuilder failMessage = new StringBuilder();
                    List<Transaction<IData>> transactions = new ArrayList<>();
                    for (String json : dataList) {
                        IData data = Entry.gson.fromJson(json, dataType);//用对应的类反序列化成对应类的对象
                        if (data != null) {
                            try {
                                if (data.create(connection) == 1) {
                                    inserted++;
                                    //插入成功的操作要上链
                                    transactions.add(new Transaction<>(Transaction.TransactionType.CREATE, null, data));
                                } else {
                                    failMessage.append("插入失败：").append(data).append("\n未知原因\n");
                                }
                            } catch (SQLException sqlException) {//插入出错的不能阻塞整个请求
                                failMessage.append("插入失败：").append(data)
                                        .append("\n原因：").append(sqlException.getMessage()).append("\n");
                            }
                        }
                    }
                    Entry.blockChain.add(transactions);//把本次插入操作上链，打包成新的区块
                    if (inserted == dataList.length) {
                        NetUtility.sendResponse(exchange, new CreateResponse(String.format("插入%d条数据成功！", inserted)));
                    } else {//有插入失败的数据
                        NetUtility.sendResponse(exchange, new CreateResponse(
                                String.format("本次插入成功%d条数据，有%d条插入失败。\n失败信息如下：\n%s",
                                        inserted, dataList.length - inserted, failMessage)));
                    }
                }
            }

        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new CreateResponse("未知错误：\n" + e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    private Class<? extends IData> getDataType() {//参数为json中的数据类型
        if (type.equals("staff"))
            return Staff.class;
        if (type.equals("department"))
            return Department.class;
        return null;
    }
}
