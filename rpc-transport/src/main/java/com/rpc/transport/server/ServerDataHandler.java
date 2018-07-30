package com.rpc.transport.server;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.entity.ServiceNameBeanEntity;
import com.rpc.common.logger.LogUtil;

import java.lang.reflect.Method;
import java.util.*;

import com.rpc.common.rpc.RPCRequest;
import com.rpc.common.rpc.RPCResponse;
import io.netty.channel.*;


/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-02 19:54
 * @Description: handler on server side, handle data from client, return result
 */
public class ServerDataHandler extends ChannelInboundHandlerAdapter {

    private Map<String, ServiceNameBeanEntity> serviceMap;

    public ServerDataHandler(Map<String, ServiceNameBeanEntity> serviceMap) {
        this.serviceMap = serviceMap;
    }

    //handle data from client
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RPCRequest request = (RPCRequest) msg;
        RPCResponse response = new RPCResponse();
        try {
            response.setRequestId(request.getRequestId());
            Object result=handleRequest(request);
            if(result==null){
                response.setError(new String(LogTipEnum.SERVER_ERROR.getConfiguredValue()));
            }else {
                response.setResult(result);
            }
        } catch (Exception e) {
            response.setError(e);
            LogUtil.logError(ServerDataHandler.class, LogTipEnum.SERVER_HANDLE_ERROR_LOG_TIP + e.getMessage());
        }
        //write event
        ctx.channel().write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised
        LogUtil.logError(ServerDataHandler.class, cause.getMessage());
        ctx.close();
    }

    //handle request method
    private Object handleRequest(RPCRequest request) throws Exception {
        //classname, got from client request
        String className = request.getClassName();
        Object serviceBean = this.serviceMap.get(className).getServiceBean();
        if (serviceBean == null) {
            LogUtil.logError(ServerDataHandler.class, "no bean for service:" + this.serviceMap.get(className).getServiceName());
            return null;
        } else {
            LogUtil.logInfo(ServerDataHandler.class, "service:" + this.serviceMap.get(className).getServiceName() + " is trying to run");
        }
        //method init
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        //reflect to get instance
        Class<?> cls = Class.forName(className);
        Method method = cls.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, parameters);
    }
}
