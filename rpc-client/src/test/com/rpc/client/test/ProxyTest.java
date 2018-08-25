package com.rpc.client.test;

import com.rpc.client.CglibProxy;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/22_11:32 PM
 */
public class ProxyTest {
    @Test
    public void testClientProxy() {
        CglibProxy cglibProxyGenerator = new CglibProxy(new TestClass(), "testService");
        TestClass proxy = (TestClass) cglibProxyGenerator.getProxy();
        assertThat(proxy, notNullValue());
        proxy.testMethod1();
        proxy.testMethod2();
        proxy.testMethod3();
    }
}
