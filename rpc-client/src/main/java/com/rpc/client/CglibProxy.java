package com.rpc.client;

import com.rpc.registry.api.ServiceDiscovery;
import com.rpc.registry.zookeeper.ZKServiceDiscovery;
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
    private String serviceName;
    private ServiceDiscovery serviceDiscovery = new ZKServiceDiscovery();
    private Enhancer enhancer = new Enhancer();
    private T target;
    private String serverAddress;

    public CglibProxy(T target, String serviceName) {
        this.target = target;
        this.serviceName = serviceName;
    }

    public CglibProxy(T target, String serviceName, String serverAddress) {
        this.target = target;
        this.serviceName = serviceName;
        this.serverAddress = serverAddress;
    }

    public Object getProxy() {
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        //cglib dynamic proxy to send request to server side
        return this.callService(method, args, serviceName, serviceDiscovery, serverAddress);
    }
}
