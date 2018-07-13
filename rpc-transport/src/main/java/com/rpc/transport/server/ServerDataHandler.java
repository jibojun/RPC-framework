package com.rpc.transport.server;

import com.rpc.common.configuration.LogTipEnum;
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

    private Map<String, Object> serviceMap;

    public ServerDataHandler(Map<String, Object> serviceMap) {
        this.serviceMap=serviceMap;
    }

    //handle data from client
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RPCRequest request=(RPCRequest)msg;
        RPCResponse response=new RPCResponse();
        try{
            response.setRequestId(request.getRequestId());
            response.setResult(handleRequest(request));
        }catch (Exception e){
            response.setError(e);
            LogUtil.logError(LogTipEnum.SERVER_HANDLE_ERROR_LOG_TIP+e.getMessage());
        }
        //write event
        ctx.write(response);
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
