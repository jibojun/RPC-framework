package com.rpc.common.entity;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/22_11:16 PM
 */
public class ServiceNameBeanEntity {
    private String serviceName;
    private Object serviceBean;

    public ServiceNameBeanEntity(String serviceName, Object serviceBean) {
        this.serviceName = serviceName;
        this.serviceBean = serviceBean;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Object getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(Object serviceBean) {
        this.serviceBean = serviceBean;
    }
}
