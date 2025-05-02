package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;
import com.zjquincy.ncu.blockchain.BlockChainIntegrity;

/*
 * 区块链完整性检验响应格式：
 * String response_type = "CHECK_INTEGRITY"
 * String message 信息，是BlockChainIntegrity类对象
 * */
public class CheckIntegrityResponse extends AbstractResponse {
    @SerializedName("message")
    private BlockChainIntegrity message;

    public CheckIntegrityResponse(BlockChainIntegrity result) {
        response_type = "CHECK_INTEGRITY";
        message = result;
    }
}
