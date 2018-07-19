package com.rpc.common.configuration;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-11 14:33
 * @Description:
 */
public enum SerializationConfigurationEnum {
    PROTOSTUFF_BUFFER_SIZE(1024),
    //1 to select protostuff, 2 to select fastjson, 3 to select hessian
    CONFIGURED_SERIALIZER(1);

    private int value;

    SerializationConfigurationEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
