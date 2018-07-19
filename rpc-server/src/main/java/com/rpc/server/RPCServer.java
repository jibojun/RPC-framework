package com.rpc.server;

import com.rpc.common.configuration.SeparatorEnum;
import com.rpc.common.configuration.ConnectionEnum;
import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.logger.LogUtil;
import com.rpc.common.rpc.RPCRequest;
import com.rpc.common.rpc.RPCResponse;
import com.rpc.registry.api.ServiceRegistry;
import com.rpc.registry.zookeeper.ZKServiceRegistry;
import com.rpc.server.annotation.ServerService;
import com.rpc.transport.codec.MessageDecoder;
import com.rpc.transport.codec.MessageEncoder;
import com.rpc.transport.server.ServerDataHandler;
import com.rpc.transport.server.ServerDataSender;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.*;

/**
 * @Author: Bojun Ji
 * @Description: RPC server side start up
 * @Date: 2018/7/6_1:08 AM
 */
@Component
public class RPCServer implements ApplicationContextAware, InitializingBean {
    private Map<String, Object> serviceMap=new HashMap();
    private ServiceRegistry registry=new ZKServiceRegistry();

    //server start up
    public void afterPropertiesSet() throws Exception {
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
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            //new connection handler
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    // add handlers to pipeline for inbound and outbound
                    ChannelPipeline pipeline = sc.pipeline();
                    pipeline.addLast("1",new MessageDecoder(RPCRequest.class));//decoder,inbound
                    pipeline.addLast("2",new ServerDataSender());//outbound, send result back to client side
                    pipeline.addLast("3",new MessageEncoder(RPCResponse.class));//encoder,outbound
                    pipeline.addLast("4",new ServerDataHandler(serviceMap));//inbound, receive and handle data
                }
            });

            //bind
            String host=InetAddress.getLocalHost().getHostAddress();
            int port=ConnectionEnum.SERVER_DEFAULT_EXPORT_PORT.getIntValue();
            ChannelFuture f = bootstrap.bind(host,port);
            LogUtil.logInfo(RPCServer.class,LogTipEnum.SERVER_START_LOG_TIP+host+SeparatorEnum.ADDRESS_SEPARATOR+port);

            //register service
            registry.registerService(host+SeparatorEnum.ADDRESS_SEPARATOR+port);

            if (f.isSuccess()) {
                LogUtil.logInfo(RPCServer.class,"long connection started successfully");
            } else {
                LogUtil.logError(RPCServer.class,"long connection started failed");

            }
        } finally{
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }
    }

    //get service class info from service annotation, build service map
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //get service bean map of the service annotation
        Map<String, Object> serviceBeanMap = applicationContext
                .getBeansWithAnnotation(ServerService.class);
        //if there is services, get class info to set key, use the bean to set value in service map
        if (!serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                serviceMap.put(serviceBean.getClass()
                        .getAnnotation(ServerService.class).value().getName(), serviceBean);
            }
        }
    }

}
