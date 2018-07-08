package com.rpc.service.server;

import com.google.common.base.Splitter;
import com.rpc.common.configuration.CharEnum;
import com.rpc.common.configuration.ConnectionEnum;
import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.logger.LogUtil;
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
import java.util.*;
import java.util.Iterator;

/**
 * @Author: Bojun Ji
 * @Description: RPC server side start up
 * @Date: 2018/7/6_1:08 AM
 */
public class RPCServer implements ApplicationContextAware, InitializingBean {
    private String host;
    private int port;
    private Map<String, Object> serviceMap=new HashMap();

    public RPCServer(String address){
        Iterator<String> result=Splitter.on(CharEnum.ADDRESS_SEPARATOR.getValue()).omitEmptyStrings().trimResults().split(address).iterator();
        int count =0;
        while(result.hasNext()&&count<=1){
            if(count==0){
                this.host=result.next();
                count++;
            }else if(count==1){
                this.port=Integer.getInteger(result.next());
            }
        }
    }


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
                    pipeline.addLast("3",new ServerDataSender());//outbound, send result back to client side
                    pipeline.addLast("4",new ServerDataHandler(serviceMap));//inbound, receive and handle data
                }
            });

            //bind
            ChannelFuture f = bootstrap.bind(this.host,this.port).sync();
            LogUtil.logInfo(LogTipEnum.SERVER_START_LOG_TIP+this.host+CharEnum.ADDRESS_SEPARATOR+this.port);

            //register



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

    //get service class info from service annotation, build service map
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

}
