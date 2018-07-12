package com.rpc.common.configuration;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/8_6:00 PM
 */
public enum LogTipEnum {
    SERVER_START_LOG_TIP("server started on :"),
    SERVER_HANDLE_ERROR_LOG_TIP("error when handle request and return response, detailed message is:"),
    SERVER_SEND_LOG_TIP("sending result to client side"),
    SERVER_EMPTY_DATA_ERROR_LOG_TIP("data from client is empty"),
    DESERIALIZATION_ERROR_TIP("error happened in deserialization, detailed message is:"),
    SERIALIZATION_ERROR_TIP("error happened in serialization, detailed message is:"),
    DISCOVERY_SELECT_ONE_NODE("use the only data node:"),
    DISCOVERY_SELECT_RANDOM_NODE("use random data node:"),
    ZK_REGISTER_SERVICE_ERROR("error happened when register service,detailed error message is:");

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
