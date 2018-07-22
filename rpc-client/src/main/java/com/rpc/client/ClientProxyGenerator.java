package com.rpc.client;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.logger.LogUtil;
import com.rpc.common.rpc.RPCRequest;
import com.rpc.common.rpc.RPCResponse;
import com.rpc.registry.api.ServiceDiscovery;
import com.rpc.registry.zookeeper.ZKServiceDiscovery;
import com.rpc.transport.client.ClientDataHandler;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-13 17:23
 * @Description: dynamic proxy to get server response
 */
public class ClientProxyGenerator<T> implements MethodInterceptor {
    private ServiceDiscovery serviceDiscovery = new ZKServiceDiscovery();
    private Enhancer enhancer = new Enhancer();
    private T target;
    private String serviceName;

    public ClientProxyGenerator(T target, String serviceName) {
        this.target = target;
        this.serviceName = serviceName;
    }

    public Object getProxy() {
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        //cglib dynamic proxy to send request to server side
        //build request
        RPCRequest request = new RPCRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass()
                .getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        //get server address of this service from registry
        String serverAddress = serviceDiscovery.discover(this.serviceName);
        if(serverAddress==null||serverAddress.isEmpty()){
            LogUtil.logError(ClientProxyGenerator.class,LogTipEnum.DISCOVERY_ERROR.getConfiguredValue());
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
