package com.rpc.sample.provider.impl;

import com.rpc.api.ITestService;
import org.springframework.stereotype.Component;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/8/25_12:09 AM
 */
@Component("testService")
public class TestService implements ITestService {
    @Override
    public String testService(int i) {
        String result;
        switch (i) {
            case 1:
                result = "hit 1";
                break;
            case 2:
                result = "hit 2";
                break;
            case 3:
                result = "hit 3";
                break;
            default:
                result = "hit nothing";
                break;
        }
        return result;
    }

    @Override
    public int testService1(String s) {
        int result = 0;
        switch (s) {
            case "a":
                result = 1;
                break;
            case "b":
                result = 2;
                break;
            default:
                break;
        }
        return result;
    }
}
