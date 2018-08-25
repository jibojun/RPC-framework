package com.rpc.client;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.util.LogUtil;
import com.rpc.common.rpc.RPCRequest;
import com.rpc.common.rpc.RPCResponse;
import com.rpc.registry.api.ServiceDiscovery;
import com.rpc.registry.zookeeper.ZKServiceDiscovery;
import com.rpc.transport.client.ClientDataHandler;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/8/25_12:37 AM
 */
public abstract class AbstractProxyHandler {

    protected String serviceName;
    protected ServiceDiscovery serviceDiscovery = new ZKServiceDiscovery();

    protected Object callService(Method method, Object[] args) throws Exception {
        //build request
        RPCRequest request = new RPCRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass()
                .getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        request.setServiceName(this.serviceName);
        //get server address of this service from registry
        String serverAddress = serviceDiscovery.discover(this.serviceName);
        if (serverAddress == null || serverAddress.isEmpty()) {
            LogUtil.logError(CglibProxy.class, LogTipEnum.DISCOVERY_ERROR.getConfiguredValue());
            return null;
        }
        ClientDataHandler client = new ClientDataHandler(serverAddress);
        //send request to server and get response
        RPCResponse response = client.sendRequest(request);
        if (response.isError()) {
            return response.getError();
        } else {
            return response.getResult();
        }
    }
}
