package com.rpc.serialization.protostuff;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.configuration.SerializationConfigurationEnum;
import com.rpc.common.logger.LogUtil;
import com.rpc.serialization.api.ObjectOutput;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @Author: Bojun Ji
 * @Description: object->byte[]
 * @Date: 2018/6/30_6:30 PM
 */
public class ProtoStuffObjectOutput implements ObjectOutput {
    private final Schema schema;
    private final Object object;
    private final LinkedBuffer buffer = LinkedBuffer.allocate(SerializationConfigurationEnum.PROTOSTUFF_BUFFER_SIZE.getValue());

    public ProtoStuffObjectOutput(Object object) {
        this.object = object;
        this.schema = RuntimeSchema.getSchema(object.getClass());
    }

    public void writeObject() {
        try {
            LinkedBuffer buffer = LinkedBuffer.allocate(SerializationConfigurationEnum.PROTOSTUFF_BUFFER_SIZE.getValue());
            ProtobufIOUtil.writeTo(this.buffer, this.object, this.schema);
        } catch (Exception e) {
            LogUtil.logError(ProtoStuffObjectOutput.class, LogTipEnum.SERIALIZATION_ERROR_TIP + e.getMessage());
        } finally {
            buffer.clear();
        }
    }

    public byte[] writeObjectAndReturn() {
        try {
            LinkedBuffer buffer = LinkedBuffer.allocate(SerializationConfigurationEnum.PROTOSTUFF_BUFFER_SIZE.getValue());
            return ProtostuffIOUtil.toByteArray(this.object, this.schema, this.buffer);
        } catch (Exception e) {
            LogUtil.logError(ProtoStuffObjectOutput.class, LogTipEnum.SERIALIZATION_ERROR_TIP + e.getMessage());
        } finally {
            buffer.clear();
            return null;
        }
    }
}
