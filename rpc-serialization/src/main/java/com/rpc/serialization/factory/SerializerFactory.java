package com.rpc.serialization.factory;

import com.rpc.common.configuration.SerializationConfigurationEnum;
import com.rpc.serialization.api.ObjectInput;
import com.rpc.serialization.api.ObjectOutput;
import com.rpc.serialization.fastjson.FastJsonObjectInput;
import com.rpc.serialization.fastjson.FastJsonObjectOutput;
import com.rpc.serialization.protostuff.ProtoStuffObjectInput;
import com.rpc.serialization.protostuff.ProtoStuffObjectOutput;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-11 15:30
 * @Description:
 */
public class SerializerFactory {

    public static ObjectOutput getSerializer(Object obj) {
        switch (SerializationConfigurationEnum.CONFIGURED_SERIALIZER.getValue()) {
            case 1:
                return new ProtoStuffObjectOutput(obj);
            case 2:
                return new FastJsonObjectOutput();
            case 3:
                return null;
            default:
                return new ProtoStuffObjectOutput(obj);
        }
    }

    public static ObjectInput getDeserializer(Class cls, byte[] data) {
        switch (SerializationConfigurationEnum.CONFIGURED_SERIALIZER.getValue()) {
            case 1:
                return new ProtoStuffObjectInput(cls, data);
            case 2:
                return new FastJsonObjectInput();
            case 3:
                return null;
            default:
                return new ProtoStuffObjectInput(cls, data);
        }
    }
}
