package com.zjquincy.ncu.blockchain;

import java.io.*;
import java.nio.file.Files;

public class BlockChainUtility {

    public static BlockChain readBlockChain(String chainDir) {//读取区块链
        File dir = new File(chainDir);
        String genesisName = null;
        if (!dir.exists()) {//目录不存在，则生成
            dir.mkdir();
        } else {//否则检查创世区块是否存在
            String[] fileList = dir.list();
            for (String name : fileList) {
                if (name.contains("genesis")) {
                    genesisName = name.replaceAll(".bin", "");//去除扩展名
                    break;
                }
            }
        }
        if (genesisName != null) {//区块链存在
            GenesisBlock genesis = (GenesisBlock) readBlock(chainDir, genesisName);
            //读出里面的创世区块用于生成区块链
            BlockChain chain = new BlockChain(genesis);
            //生成后，读取其他区块
            File[] fileList = dir.listFiles((d, name) -> !name.contains("genesis"));//不需要再次把创世区块加入区块列表中
            for (File f : fileList) {//将所有的普通区块加入到区块链的区块列表里
                String fileName = f.getName().replaceAll(".bin", "");
                PlainBlock block = (PlainBlock) readBlock(chainDir, fileName);
                chain.loadBlock(block);
            }
            return chain;
        }
        //区块链不存在，返回一个新的区块链，并且把这个区块链的创世区块写到文件里
        GenesisBlock genesis = new GenesisBlock(System.currentTimeMillis());
        writeBlock(genesis, chainDir);
        return new BlockChain(genesis);
    }

    public static void writeBlock(Block block, String dir) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(block);
            objectStream.flush();
            objectStream.close();
            byte[] blockBytes = byteStream.toByteArray();
            byteStream.close();
            //把区块写成字节数组
            byte[] encryptedBytes = AESUtility.encrypt(blockBytes);
            File dest = new File(dir, block.getIdentifier() + ".bin");
            Files.write(dest.toPath(), encryptedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Block readBlock(String dir, String identifier) {
        try {
            File source = new File(dir, identifier + ".bin");
            byte[] encryptedBytes = Files.readAllBytes(source.toPath());
            byte[] blockBytes = AESUtility.decrypt(encryptedBytes); //解密
            ByteArrayInputStream byteStream = new ByteArrayInputStream(blockBytes);
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            Block block = (Block) objectStream.readObject();//从字节反序列化成Block对象
            objectStream.close();
            byteStream.close();
            return block;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
