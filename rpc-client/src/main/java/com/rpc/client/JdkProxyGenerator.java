package com.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/8/25_12:28 AM
 */
public class JdkProxyGenerator extends AbstractProxyGenerator implements InvocationHandler {
    private Class<?> interfaceClass;


    public JdkProxyGenerator(String serviceName, Class<?> interfaceClass) {
        this.serviceName = serviceName;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.callService(method, args);
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new JdkProxyGenerator(this.serviceName, this.interfaceClass));
    }
}
