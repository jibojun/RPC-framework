package com.rpc.sample.provider;

import com.rpc.api.ITestService;
import com.rpc.common.entity.ServiceNameBeanEntity;
import com.rpc.server.RPCServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-25 12:13
 * @Description:
 */
public class ProviderSample {
    public static void main(String[] args) {
        ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
        ((ClassPathXmlApplicationContext) context).start();
        ITestService serviceBean=(ITestService)context.getBean("testService");
        List<ServiceNameBeanEntity> serviceList=new ArrayList<>();
        serviceList.add(new ServiceNameBeanEntity("testService",serviceBean));
        RPCServer.exportAllServices(serviceList);
    }
}
