package com.rpc.serialization.api;

import java.io.IOException;

/**
 * @Author: Bojun Ji
 * @Description: read object,deserialization, byte[]->object
 * @Date: 2018/6/30_6:07 PM
 */
public interface ObjectInput {
    <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException;
    Object readObject(Object object) throws IOException, ClassNotFoundException;
}
