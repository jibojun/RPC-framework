package com.rpc.common.configuration;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-02 10:40
 * @Description: connection configuration
 */
public enum ConnectionEnum {
    SERVER_DEFAULT_EXPORT_PORT(String.valueOf(9999)),
    SERVER_CONNECTION_NUMBER(String.valueOf(1024 * 1024));

    private String configuredValue;

    ConnectionEnum(String configuredValue) {
        this.configuredValue = configuredValue;
    }

    public String getStringValue() {
        return this.configuredValue;
    }

    public int getIntValue() {
        return Integer.valueOf(this.configuredValue);
    }

    public void setConfiguredValue(String configuredValue) {
        this.configuredValue = configuredValue;
    }
}
