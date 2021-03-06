package com.rpc.transport.server;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.util.LogUtil;
import io.netty.channel.*;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-06 17:52
 * @Description:
 */
public class ServerDataSender extends ChannelOutboundHandlerAdapter {

    //listen to outbound event, send result back to client side
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        LogUtil.logInfo(ServerDataSender.class, LogTipEnum.SERVER_SEND_LOG_TIP.getConfiguredValue() + ", result is :" + msg);
        ctx.writeAndFlush(msg);
    }

}
