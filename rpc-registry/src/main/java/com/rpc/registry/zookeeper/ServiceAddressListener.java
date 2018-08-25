package com.rpc.registry.zookeeper;

import com.rpc.common.util.StringUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/22_5:19 PM
 */
public class ServiceAddressListener implements PathChildrenCacheListener {
    private volatile Map<String, List<String>> serviceMap;
    private String listenedServiceName;

    public ServiceAddressListener(Map<String, List<String>> serviceMap, String listenedServiceName) {
        this.serviceMap = serviceMap;
        this.listenedServiceName = listenedServiceName;
    }

    @Override
    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
        switch (pathChildrenCacheEvent.getType()) {
            case CHILD_ADDED: {
                //add the service address for this service
                if (serviceMap.containsKey(this.listenedServiceName)) {
                    synchronized (ServiceAddressListener.class) {
                        List<String> serviceAddressList = serviceMap.get(listenedServiceName);
                        if (serviceAddressList != null) {
                            serviceAddressList.add(new String(pathChildrenCacheEvent.getData().getData()));
                        } else {
                            List<String> tmpList = new ArrayList<>();
                            tmpList.add(StringUtil.getZkSubPath(pathChildrenCacheEvent.getData().getPath(), 2));
                            serviceMap.put(listenedServiceName, tmpList);
                        }
                    }
                    serviceMap.put(new String(pathChildrenCacheEvent.getData().getData()), new ArrayList<>());
                }
                break;
            }
            case CHILD_REMOVED: {
                //remove the service address for this service
                if (serviceMap.containsKey(this.listenedServiceName)) {
                    synchronized (ServiceAddressListener.class) {
                        List<String> serviceAddressList = serviceMap.get(listenedServiceName);
                        if (serviceAddressList != null) {
                            serviceAddressList.remove(StringUtil.getZkSubPath(pathChildrenCacheEvent.getData().getPath(), 2));
                        }
                    }
                }
                break;
            }
            case CHILD_UPDATED: {
                break;
            }
        }
    }
}
