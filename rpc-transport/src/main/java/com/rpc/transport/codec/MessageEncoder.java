package com.rpc.transport.codec;

import com.rpc.serialization.api.ObjectOutput;
import com.rpc.serialization.protostuff.ProtoStuffObjectOutput;
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
        if(genericClass.isInstance(msg)){
            ObjectOutput output=new ProtoStuffObjectOutput(msg);
            byte[] data=output.writeObjectAndReturn();
            out.writeBytes(data);
            out.writeInt(data.length);
        }
    }
}
