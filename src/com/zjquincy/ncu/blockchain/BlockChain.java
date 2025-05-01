package com.zjquincy.ncu.blockchain;

import com.zjquincy.ncu.Entry;
import java.util.HashMap;

public class BlockChain {
    private final HashMap<String, Block> data = new HashMap<>();
    private final GenesisBlock genesis;

    public BlockChain(GenesisBlock genesis) {
        this.genesis = genesis;
    }

    public void loadBlock(Block block) {//读取文件时使用的方法，用于把区块载入进map中
        data.put(block.getIdentifier(), block);
    }

    public void add(Transaction<?> message) {
        PlainBlock block = new PlainBlock(System.currentTimeMillis(), genesis.prevBlockIdentifier, message);
        genesis.prevBlockIdentifier = block.getIdentifier();
        BlockChainUtility.writeBlock(block, Entry.BLOCKCHAIN_DIR);//把新区块写进文件
        BlockChainUtility.writeBlock(genesis, Entry.BLOCKCHAIN_DIR);//更新创世区块
    }

    public BlockChainIntegrity verify() {//检验区块链完整性，检验成功返回区块数量，失败返回-1
        String pointer = genesis.prevBlockIdentifier;//验证指针，指向下一个被检验的区块
        int verifiedBlockNum = 0;
        while (!pointer.contains("genesis")) {//如果指针回到创世区块，证明检验通过
            Block nextBlock = data.get(pointer);
            if (nextBlock != null) {//找到下一个区块，继续验证
                verifiedBlockNum++;
                pointer= nextBlock.prevBlockIdentifier;
            } else {//区块断裂，检验失败
                return new BlockChainIntegrity(false, "未找到区块" + pointer + "，检验未通过！");
            }
        }
        return new BlockChainIntegrity(true, "检验通过！共找到" + verifiedBlockNum + "个有效区块");
    }
}
