package com.rpc.serialization.api;

import java.io.IOException;

/**
 * @Author: Bojun Ji
 * @Description: write object,serialization, object->byte[]
 * @Date: 2018/6/30_6:07 PM
 */
public interface ObjectOutput {
    void writeObject() throws IOException;
    byte[] writeObjectAndReturn() throws IOException;
}
