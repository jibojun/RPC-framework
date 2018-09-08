package com.rpc.transport.codec;

import com.rpc.common.util.LogUtil;
import com.rpc.serialization.api.ObjectOutput;
import com.rpc.serialization.factory.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/8_6:41 PM
 */
public class MessageEncoder extends MessageToByteEncoder {
    private Class<?> genericClass;

    public MessageEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //serialization
        if (genericClass.isInstance(msg)) {
            ObjectOutput output = SerializerFactory.getSerializer(msg);
            byte[] data = output.writeObjectAndReturn();
            LogUtil.logInfo(this.getClass(), String.format("serialization object: %s", (Object) data));
            //data length,write in header, first 4
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
