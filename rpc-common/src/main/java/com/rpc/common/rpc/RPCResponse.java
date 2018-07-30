package com.rpc.common.rpc;

import java.io.Serializable;

/**
 * @Author: Bojun Ji
 * @Description: response with properties returned to client
 * @Date: 2018/7/6_1:30 AM
 */
public class RPCResponse implements Serializable {
    private String requestId;
    private Object error;
    private Object result;

    public boolean isError() {
        return error != null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Object getError() {
        return error;
    }
}
