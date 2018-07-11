package com.rpc.registry.api;

/**
 * @Author: Bojun Ji
 * @Description: register service on ZK
 * @Date: 2018/7/8_6:09 PM
 */
public interface ServiceRegistry {
    void registerService(String serverAddress);
    void createDataNode(String data);
}
