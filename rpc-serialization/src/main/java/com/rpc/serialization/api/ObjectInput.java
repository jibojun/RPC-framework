package com.rpc.serialization.api;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Author: Bojun Ji
 * @Description: read object
 * @Date: 2018/6/30_6:07 PM
 */
public interface ObjectInput {
    Object readObject() throws IOException, ClassNotFoundException;
    <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException;
    <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException;
}
