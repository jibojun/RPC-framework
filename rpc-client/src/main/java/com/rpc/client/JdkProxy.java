package com.rpc.client;

import java.lang.reflect.Proxy;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/8/25_12:28 AM
 */
public class JdkProxy implements IProxy {
    private String serviceName;
    private Class<?> interfaceClass;


    public JdkProxy(String serviceName, Class<?> interfaceClass) {
        this.serviceName = serviceName;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new JdkProxyHandler(serviceName));
    }
}
