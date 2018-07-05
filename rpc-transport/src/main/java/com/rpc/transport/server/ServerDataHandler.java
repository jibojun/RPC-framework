package com.rpc.transport.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;


/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-02 19:54
 * @Description: handler on server side, handle data from client, return result
 */
public class ServerDataHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body=(String)msg;
    }

    public void setApplicationContext() throws  Exception{

    }

    public void afterPropertiesSet() throws Exception {
        //accept connection group
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //detailed event handler group
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //bind group
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        //configuration， no delay for TCP transfer,
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        //configuration, connection number,
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024 * 1024);
        //configuration, connection keep alive, after acceptor accept the channel
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        //new connection handler
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                // task handling
                ChannelPipeline pipeline = sc.pipeline();
                //pipeline.addLast(new MessageDecoder(), new MessageEncoder(), new NettyServerHandler());
            }
        });

        ChannelFuture f = bootstrap.bind(9999).sync();
        if (f.isSuccess()) {
            //log.info("long connection started success");
        } else {
            //log.error("long connection started fail");

        }
    }
}
