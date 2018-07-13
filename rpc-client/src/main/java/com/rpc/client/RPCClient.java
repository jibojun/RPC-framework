package com.rpc.client;

import com.rpc.client.annotation.ClientRef;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-10 19:52
 * @Description:
 */
public class RPCClient extends InstantiationAwareBeanPostProcessorAdapter implements ApplicationContextAware {

        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//            //get service bean map of the reference annotation
//            if (!serviceBeanMap.isEmpty()) {
//            for (Object serviceBean : serviceBeanMap.values()) {
//                ClientProxy proxy=new ClientProxy();
//                Enhancer enhancer = new Enhancer();
//                enhancer.setSuperclass(serviceBean.getClass());
//                enhancer.setCallback(proxy);
//                Object result=enhancer.create();
//            }
//        }
    }
}

