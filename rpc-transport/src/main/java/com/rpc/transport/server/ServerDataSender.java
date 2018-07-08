package com.rpc.transport.server;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.logger.LogUtil;
import io.netty.channel.*;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-06 17:52
 * @Description:
 */
public class ServerDataSender extends ChannelOutboundHandlerAdapter {

    //send result back to cliet side
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        LogUtil.logInfo(LogTipEnum.SERVER_SEND_LOG_TIP.getConfiguredValue());
        ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
    }

}
