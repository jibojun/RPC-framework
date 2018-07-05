package com.rpc.serialization.fastjson;

import com.rpc.serialization.api.ObjectInput;

import java.io.IOException;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/2_1:09 AM
 */
public class FastJsonObjectInput implements ObjectInput {
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return null;
    }

    public Object readObject(Object object) throws IOException, ClassNotFoundException {
        return null;
    }
}
