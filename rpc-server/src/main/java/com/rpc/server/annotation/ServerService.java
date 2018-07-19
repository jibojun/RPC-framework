package com.rpc.server.annotation;

import com.rpc.common.configuration.ConnectionEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Bojun Ji
 * @Description: annotation for service publish
 * @Date: 2018/7/6_1:12 AM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ServerService {
    Class<?> value();
}
