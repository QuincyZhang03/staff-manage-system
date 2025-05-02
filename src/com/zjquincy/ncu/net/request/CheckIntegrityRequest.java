package com.zjquincy.ncu.net.request;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.zjquincy.ncu.Entry;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.blockchain.BlockChainIntegrity;
import com.zjquincy.ncu.net.NetUtility;
import com.zjquincy.ncu.net.response.CheckIntegrityResponse;

import java.io.IOException;
import java.sql.SQLException;

/*
 * 区块链完整性检验请求格式：
 * String request_type = "CHECK_INTEGRITY"
 * String username 用户名
 * */
public class CheckIntegrityRequest extends AbstractRequest {
    @SerializedName("username")
    private String username;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            User user = User.fetchUser(username);
            if (user == null) {//此处是未获取到用户，理论上不会发生这种情况
                NetUtility.sendResponse(exchange,
                        new CheckIntegrityResponse(new BlockChainIntegrity(false, "用户未找到！")));
            } else if (user.getLevel() < User.ADMIN) {//非超级管理员，不予执行
                NetUtility.sendResponse(exchange,
                        new CheckIntegrityResponse(new BlockChainIntegrity(false, "权限不足！")));
            } else {
                BlockChainIntegrity result = Entry.blockChain.verify();
                NetUtility.sendResponse(exchange, new CheckIntegrityResponse(result));
            }
        } catch (SQLException e) {
            NetUtility.sendResponse(exchange, new CheckIntegrityResponse(
                    new BlockChainIntegrity(false, "未知错误！\n" + e.getMessage())));
            throw new RuntimeException(e);
        }
    }
}
