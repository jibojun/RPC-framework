package com.rpc.server;

import com.rpc.common.configuration.SeparatorEnum;
import com.rpc.common.configuration.ConnectionEnum;
import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.entity.ServiceNameBeanEntity;
import com.rpc.common.util.LogUtil;
import com.rpc.common.rpc.RPCRequest;
import com.rpc.common.rpc.RPCResponse;
import com.rpc.registry.api.ServiceRegistry;
import com.rpc.registry.zookeeper.ZKServiceRegistry;
import com.rpc.transport.codec.MessageDecoder;
import com.rpc.transport.codec.MessageEncoder;
import com.rpc.transport.server.ServerDataHandler;
import com.rpc.transport.server.ServerDataSender;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Bojun Ji
 * @Description: RPC server side start up
 * @Date: 2018/7/6_1:08 AM
 */
public class RPCServer {
    //a map with key: class name, value: bean
    private static Map<String, ServiceNameBeanEntity> serviceMap = new ConcurrentHashMap<>();
    //registry
    private static ServiceRegistry registry = new ZKServiceRegistry();

    public static boolean exportAllServices(List<ServiceNameBeanEntity> list) {
        for (ServiceNameBeanEntity item : list) {
            Class<?>[] interfaceClasses = item.getServiceBean().getClass().getInterfaces();
            if (interfaceClasses == null || interfaceClasses.length == 0) {
                LogUtil.logError(RPCServer.class, "no interface implemented for the class");
                return false;
            } else {
                serviceMap.put(interfaceClasses[0].getName(), item);
            }
        }
        try {
            serverStartUp();
            return true;
        } catch (Exception e) {
            LogUtil.logError(RPCServer.class, e.getMessage());
            return false;
        }
    }

    /**
     * server start
     *
     * @throws Exception
     */
    private static void serverStartUp() throws Exception {
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
            bootstrap.option(ChannelOption.SO_BACKLOG, ConnectionEnum.SERVER_CONNECTION_NUMBER.getIntValue());
            //configuration, connection keep alive, after acceptor accept the channel
//            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    // add handlers to pipeline for inbound and outbound
                    ChannelPipeline pipeline = sc.pipeline();
                    pipeline.addLast("1", new MessageDecoder(RPCRequest.class));//decoder,inbound
                    pipeline.addLast("2", new ServerDataHandler(serviceMap));//inbound, receive and handle data
                    pipeline.addLast("3", new MessageEncoder(RPCResponse.class));//encoder,outbound
                    pipeline.addLast("4", new ServerDataSender());//outbound, send result back to client side

                }
            });

            //bind and listening, get local host's address and configured port
            String host = InetAddress.getLocalHost().getHostAddress();
            int port = ConnectionEnum.SERVER_DEFAULT_EXPORT_PORT.getIntValue();
            ChannelFuture f = bootstrap.bind(host, port);
            LogUtil.logInfo(RPCServer.class, LogTipEnum.SERVER_START_LOG_TIP + host + SeparatorEnum.ADDRESS_SEPARATOR.getValue() + port);

            //register services

            for (Map.Entry<String, ServiceNameBeanEntity> entry : serviceMap.entrySet()) {
                registry.registerService(entry.getValue().getServiceName(), host + SeparatorEnum.ADDRESS_SEPARATOR.getValue() + port);
            }

            LogUtil.logInfo(RPCServer.class, "server started");

            f.channel().closeFuture().sync();
            for (Map.Entry<String, ServiceNameBeanEntity> entry : serviceMap.entrySet()) {
                registry.unRegisterService(entry.getValue().getServiceName(), host + SeparatorEnum.ADDRESS_SEPARATOR.getValue() + port);
            }
        } finally {
            //release thread pool resource
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
