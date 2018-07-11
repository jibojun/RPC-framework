package com.rpc.common.configuration;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/8_6:05 PM
 */
public enum SeparatorEnum {
    ADDRESS_SEPARATOR(":");

    private String value;

    SeparatorEnum(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
