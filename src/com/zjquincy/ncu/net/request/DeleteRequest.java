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
import com.zjquincy.ncu.net.response.DeleteResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * 删除数据请求格式：
 * String request_type = "DELETE"
 * String username 用户名
 * String type 删除的数据类型，可以为"staff"或"department"
 * JsonString[] dataList 删除的具体数据列表，应当可以被反序列化为IData接口的实例。客户端应发送json文本数组，而非json对象数组。
 * */
//该类结构与CreateRequest基本完全一致。
public class DeleteRequest extends AbstractRequest {
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
                NetUtility.sendResponse(exchange, new DeleteResponse("未找到用户：" + username));//一般情况下不会出现这种情况
            } else if (user.getLevel() < User.OPERATOR) {//用户权限不足
                NetUtility.sendResponse(exchange, new DeleteResponse("权限不足！"));
            } else {//解析出新增数据的类型
                Class<? extends IData> dataType = getDataType();
                if (dataType == null) {
                    NetUtility.sendResponse(exchange, new DeleteResponse("数据类型解析失败：" + type));
                } else {//数据类型合法，开始删除
                    Connection connection = DriverManager.getConnection(Entry.DB_URL, user.getDBUsername(), user.getDBPassword());
                    int deleted = 0;
                    StringBuilder failMessage = new StringBuilder();
                    List<Transaction<IData>> transactions = new ArrayList<>();
                    for (String json : dataList) {
                        IData data = Entry.gson.fromJson(json, dataType);
                        if (data != null) {
                            int result = data.delete(connection);
                            if (result == 1) {
                                deleted++;
                                transactions.add(new Transaction<>(Transaction.TransactionType.DELETE, data, null));
                            } else {
                                failMessage.append("删除失败：").append(data).append("\n");
                            }
                        }
                    }
                    Entry.blockChain.add(transactions);//把本次插入操作上链，打包成新的区块
                    if (deleted == dataList.length) {
                        NetUtility.sendResponse(exchange, new DeleteResponse(String.format("删除%d条数据成功！", deleted)));
                    } else {//有插入失败的数据
                        NetUtility.sendResponse(exchange, new DeleteResponse(
                                String.format("本次删除成功%d条数据，有%d条删除失败。\n失败信息：%s",
                                        deleted, dataList.length - deleted, failMessage)));
                    }
                }
            }

        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new DeleteResponse("未知错误：\n" + e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    private Class<? extends IData> getDataType() {//参数为json种的数据类型
        if (type.equals("staff"))
            return Staff.class;
        if (type.equals("department"))
            return Department.class;
        return null;
    }
}
