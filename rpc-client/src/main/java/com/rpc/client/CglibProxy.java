package com.rpc.client;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-13 17:23
 * @Description: cglib dynamic proxy for class
 */
public class CglibProxy<T> extends AbstractProxyHandler implements MethodInterceptor, IProxy {
    private Enhancer enhancer = new Enhancer();
    private T target;

    public CglibProxy(T target, String serviceName) {
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
        return this.callService(method, args);
    }
}
