package com.rpc.serialization.protostuff;

import com.rpc.serialization.api.ObjectOutput;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/6/30_6:30 PM
 */
public class ProtoStuffObjectOutput implements ObjectOutput {
    private final Schema schema;
    private final Object object;

    public ProtoStuffObjectOutput(Object object){
        this.object=object;
        this.schema=RuntimeSchema.getSchema(object.getClass());
    }

    public void writeObject() throws IOException {
        ProtobufIOUtil.writeTo(LinkedBuffer.allocate(1024),this.object,this.schema);
    }

    public byte[] writeObjectAndReturn() throws IOException {
        return ProtostuffIOUtil.toByteArray(this.object,this.schema,LinkedBuffer.allocate(1024));
    }
}
