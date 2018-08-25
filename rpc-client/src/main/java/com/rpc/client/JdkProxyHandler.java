package com.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/8/25_6:37 PM
 */
public class JdkProxyHandler extends AbstractProxyHandler implements InvocationHandler {
    public JdkProxyHandler(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.callService(method, args);
    }
}
