package com.rpc.transport.server;

import com.rpc.common.configuration.ConnectionEnum;
import com.rpc.common.logger.LogUtil;

import java.io.ObjectInput;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.rpc.common.rpc.RPCRequest;
import com.rpc.serialization.protostuff.ProtoStuffObjectInput;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-02 19:54
 * @Description: handler on server side, handle data from client, return result
 */
public class ServerDataHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> serviceMap;

    public ServerDataHandler(Map<String, Object> serviceMap) {
        this.serviceMap=serviceMap;
    }

    //handle data from client
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RPCRequest request=(RPCRequest)msg;
        //accept connection group
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //detailed event handler group
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            //use nio, non-blocking
            bootstrap.channel(NioServerSocketChannel.class);
            //bind groups
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

            ChannelFuture f = bootstrap.bind(ConnectionEnum.SERVER_PORT.getIntValue()).sync();
            if (f.isSuccess()) {
                //log.info("long connection started success");
            } else {
                //log.error("long connection started fail");

            }
        } finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised
        LogUtil.logError(cause.getMessage());
        ctx.close();
    }

    //handle request method
    private Object handleRequest(RPCRequest request) throws Exception {
        //classname, got from client request
        String className = request.getClassName();
        Object serviceBean = this.serviceMap.get(className);
        //method init
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        //reflect to get instance
        Class<?> forName = Class.forName(className);
        Method method = forName.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, parameters);
    }
}
