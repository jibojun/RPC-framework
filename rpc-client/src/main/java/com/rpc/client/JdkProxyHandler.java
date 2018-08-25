package com.rpc.client;

import com.rpc.registry.api.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/8/25_6:37 PM
 */
public class JdkProxyHandler extends AbstractProxyHandler implements InvocationHandler {
    private String serviceName;
    private ServiceDiscovery serviceDiscovery;

    public JdkProxyHandler(String serviceName, ServiceDiscovery serviceDiscovery) {
        this.serviceName = serviceName;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.callService(method, args, serviceName, serviceDiscovery);
    }
}
