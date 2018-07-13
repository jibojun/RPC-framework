package com.rpc.client;

import com.rpc.common.rpc.RPCRequest;
import com.rpc.common.rpc.RPCResponse;
import com.rpc.registry.api.ServiceDiscovery;
import com.rpc.registry.zookeeper.ZKServiceDiscovery;
import com.rpc.transport.client.ClientDataHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-13 17:23
 * @Description:
 */
public class ClientProxy implements MethodInterceptor {
    private ServiceDiscovery serviceDiscovery=new ZKServiceDiscovery();

    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        //cglib dynamic proxy to send request to server side
        RPCRequest request=new RPCRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass()
                .getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        //get server address from registry
        String serverAddress=serviceDiscovery.discover();
        ClientDataHandler client=new ClientDataHandler(serverAddress);
        RPCResponse response=client.sendRequest(request);
        if(response.isError()){
            throw response.getError();
        }else {
            return response.getResult();
        }
    }
}
