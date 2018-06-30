package com.rpc.serialization.protostuff;

import com.rpc.serialization.api.ObjectInput;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/6/30_6:30 PM
 */
public class ProtoStuffObjectInput implements ObjectInput {


    public Object readObject() throws IOException, ClassNotFoundException {
        return null;
    }

    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return null;
    }

    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        return null;
    }
}
