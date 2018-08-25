package com.rpc.common.configuration;

public enum ZooKeeperConfigurationEnum {
    ZK_SERVER_ADDRESS("127.0.0.1"),
    ZK_REGISTRY_PATH("/registry"),
    ZK_DATA_PATH("/serviceAddress"),
    ZK_CONNECT_MAX_RETRY_TIMES("3"),
    ZK_CONNECT_SLEEP_TIME("60000");

    private String value;

    ZooKeeperConfigurationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
