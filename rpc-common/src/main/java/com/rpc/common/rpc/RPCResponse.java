package com.rpc.common.rpc;

/**
 * @Author: Bojun Ji
 * @Description: response with properties returned to client
 * @Date: 2018/7/6_1:30 AM
 */
public class RPCResponse {
    private String requestId;
    private Throwable error;
    private Object result;

    public boolean isError() {
        return error!=null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
