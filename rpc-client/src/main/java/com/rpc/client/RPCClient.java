package com.rpc.client;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/8/2_11:26 PM
 */
public class RPCClient {

    /**
     * init cglib generator and return a proxy class
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(String serviceName, T target) {
        //cglib
        IProxy cglibProxyGenerator = new CglibProxy(target, serviceName);
        return (T) cglibProxyGenerator.getProxy();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(String serviceName, T target, String serverAddress) {
        //cglib
        IProxy cglibProxyGenerator = new CglibProxy(target, serviceName, serverAddress);
        return (T) cglibProxyGenerator.getProxy();
    }


    @SuppressWarnings("unchecked")
    public static <T> T getProxy(String serviceName, Class<?> interfaceClass) {
        //jdk proxy
        IProxy jdkProxyGenerator = new JdkProxy(serviceName, interfaceClass);
        return (T) jdkProxyGenerator.getProxy();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(String serviceName, Class<?> interfaceClass, String serverAddress) {
        //jdk proxy with direct connection without registry
        IProxy jdkProxyGenerator = new JdkProxy(serviceName, interfaceClass, serverAddress);
        return (T) jdkProxyGenerator.getProxy();
    }
}
