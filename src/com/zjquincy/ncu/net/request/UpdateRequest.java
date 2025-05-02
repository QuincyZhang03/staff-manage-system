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
import com.zjquincy.ncu.net.response.UpdateResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * 修改数据请求格式：
 * String request_type = "UPDATE"
 * String username 用户名
 * String type 修改的数据类型，可以为"staff"或"department"
 * JsonString[][] dataList 删除的具体数据列表。每一项又有两个子项，分别是原数据和修改后数据
 * */
public class UpdateRequest extends AbstractRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("type")
    private String type;
    @SerializedName("dataList")
    private String[][] dataList;//每一行第一列是原数据，第二列是修改后数据

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            User user = User.fetchUser(username);
            if (user == null) {
                NetUtility.sendResponse(exchange, new UpdateResponse("未找到用户：" + username));//一般情况下不会出现这种情况
            } else if (user.getLevel() < User.OPERATOR) {//用户权限不足
                NetUtility.sendResponse(exchange, new UpdateResponse("权限不足！"));
            } else {//解析出修改数据的类型
                Class<? extends IData> dataType = getDataType();
                if (dataType == null) {
                    NetUtility.sendResponse(exchange, new UpdateResponse("数据类型解析失败：" + type));
                } else {//数据类型合法，开始修改
                    Connection connection = DriverManager.getConnection(Entry.DB_URL, user.getDBUsername(), user.getDBPassword());
                    int inserted = 0;
                    StringBuilder failMessage = new StringBuilder();
                    List<Transaction<IData>> transactions = new ArrayList<>();
                    for (String[] pair : dataList) {
                        IData prev = Entry.gson.fromJson(pair[0], dataType);
                        IData after = Entry.gson.fromJson(pair[1], dataType);
                        if (prev != null && after != null) {
                            int result = prev.update(connection, after);
                            if (result == 1) {
                                inserted++;
                                //插入成功的操作要上链
                                transactions.add(new Transaction<>(Transaction.TransactionType.UPDATE, prev, after));
                            } else {
                                failMessage.append("修改失败：").append(prev).append(" -> ").append(after).append("\n");
                            }
                        }
                    }
                    Entry.blockChain.add(transactions);//把本次插入操作上链，打包成新的区块
                    if (inserted == dataList.length) {
                        NetUtility.sendResponse(exchange, new UpdateResponse(String.format("修改%d条数据成功！", inserted)));
                    } else {//有插入失败的数据
                        NetUtility.sendResponse(exchange, new UpdateResponse(
                                String.format("本次修改成功%d条数据，有%d条修改失败。\n失败信息：%s",
                                        inserted, dataList.length - inserted, failMessage)));
                    }
                }
            }

        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new UpdateResponse("未知错误：\n" + e.getMessage()));
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