package com.rpc.serialization.fastjson;

import com.rpc.serialization.api.ObjectOutput;

import java.io.IOException;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/2_1:09 AM
 */
public class FastJsonObjectOutput implements ObjectOutput {
    public void writeObject() throws IOException {

    }

    public byte[] writeObjectAndReturn() throws IOException {
        return new byte[0];
    }
}
