package com.rpc.transport.datatransfer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-02 19:54
 * @Description:
 */
public class DataTransferHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body=(String)msg;
    }

    public void setApplicationContext() throws  Exception{

    }

    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        ServerBootstrap b=new ServerBootstrap();
    }
}
