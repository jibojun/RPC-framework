package com.rpc.transport.client;

import com.rpc.common.rpc.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: Bojun Ji
 * @Description: client side data handler, send data to server, handle returned data
 * @Date: 2018/7/6_1:25 AM
 */
public class ClientDataHandler extends SimpleChannelInboundHandler<RPCResponse> {
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {

    }
}
