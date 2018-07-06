package com.rpc.transport.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-06 17:52
 * @Description:
 */
public class ServerDataSender extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise){

    }
}
