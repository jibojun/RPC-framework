package com.rpc.serialization.protostuff;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.logger.LogUtil;
import com.rpc.serialization.api.ObjectInput;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Bojun Ji
 * @Description: byte[]->object
 * @Date: 2018/6/30_6:30 PM
 */
public class ProtoStuffObjectInput implements ObjectInput {
    private final Schema schema;
    private final byte[] data;
    private static Objenesis objenesis = new ObjenesisStd(true);

    public ProtoStuffObjectInput(Class cls,byte[] data){
        this.schema=RuntimeSchema.getSchema(cls);
        this.data=data;
    }

    public Object readObject(Object object) {
        try {
            ProtostuffIOUtil.mergeFrom(this.data, object, this.schema);
        }catch (Exception e){
            LogUtil.logError(LogTipEnum.DESERIALIZATION_ERROR_TIP+e.getMessage());
        }
        return object;
    }

    public <T> T readObject(Class<T> cls) {
        T object=null;
        try {
            //create instance, but need to avoid situation that no constructor method without any parameter
            object = objenesis.newInstance(cls);
            ProtostuffIOUtil.mergeFrom(this.data, object, this.schema);
        } catch (Exception e) {
            LogUtil.logError(LogTipEnum.DESERIALIZATION_ERROR_TIP+e.getMessage());
        }
        return object;
    }
}
