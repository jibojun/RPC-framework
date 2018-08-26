package com.rpc.sample.consumer;

import com.rpc.api.ITestService;
import com.rpc.client.RPCClient;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/8/24_11:17 PM
 */
public class ConsumerSample {

    public static void main(String[] args) {
        ITestService service = RPCClient.getProxy("testService", ITestService.class);
        System.out.println(service.testService(1));
        System.out.println(service.testService(2));
        System.out.println(service.testService(3));
        System.out.println(service.testService1("a"));
        System.out.println(service.testService1("b"));
        System.out.println(service.testService1("c"));
    }
}
