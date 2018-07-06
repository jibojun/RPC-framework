package com.rpc.serialization.protostuff;

import com.rpc.serialization.api.ObjectInput;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/6/30_6:30 PM
 */
public class ProtoStuffObjectInput implements ObjectInput {

    private final Schema schema;
    //input
    private final byte[] data;

    public ProtoStuffObjectInput(Class cls,byte[] data){
        this.schema=RuntimeSchema.getSchema(cls);
        this.data=data;
    }


    public Object readObject(Object object) throws IOException, ClassNotFoundException {
        ProtostuffIOUtil.mergeFrom(this.data, object, this.schema);
        return object;
    }

    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        T object= null;
        try {
            object = cls.newInstance();
            ProtostuffIOUtil.mergeFrom(this.data, object, this.schema);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
}
