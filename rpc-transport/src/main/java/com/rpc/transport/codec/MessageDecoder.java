package com.rpc.transport.codec;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.util.LogUtil;
import com.rpc.serialization.api.ObjectInput;
import com.rpc.serialization.factory.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/8_6:41 PM
 */
public class MessageDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;

    public MessageDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @SuppressWarnings("unchecked")
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //mark index
        in.markReaderIndex();
        //get data size wrote in head, the first 4
        int dataSize = in.readInt();
        //check data size
        if (dataSize <= 0) {
            ctx.close();
            LogUtil.logError(MessageDecoder.class, LogTipEnum.SERVER_EMPTY_DATA_ERROR_LOG_TIP.getConfiguredValue());
        }
        if (in.readableBytes() < dataSize) {
            //reposition to marked index
            in.resetReaderIndex();
        }
        //deserialization
        byte[] data = new byte[dataSize];
        in.readBytes(data);
        ObjectInput input = SerializerFactory.getDeserializer(genericClass, data);
        Object obj = input.readObject(genericClass);
        LogUtil.logInfo(this.getClass(), String.format("deserialization object: %s", obj));
        out.add(obj);
    }
}
