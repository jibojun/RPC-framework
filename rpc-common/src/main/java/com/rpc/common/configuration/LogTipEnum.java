package com.rpc.common.configuration;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/8_6:00 PM
 */
public enum LogTipEnum {
    SERVER_START_LOG_TIP("server started on :"),
    SERVER_HANDLE_ERROR_LOG_TIP("error when handle request and return response, deatiled message is:"),
    SERVER_SEND_LOG_TIP("sending result to client side"),
    SERVER_EMPTY_DATA_ERROR_LOG_TIP("data from client is empty"),;

    private String configuredValue;

    LogTipEnum(String configuredValue){
        this.configuredValue=configuredValue;
    }

    public String getConfiguredValue() {
        return configuredValue;
    }

    public void setConfiguredValue(String configuredValue) {
        this.configuredValue = configuredValue;
    }
}
