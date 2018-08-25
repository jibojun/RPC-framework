package com.rpc.registry.zookeeper;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.configuration.SeparatorEnum;
import com.rpc.common.configuration.ZooKeeperConfigurationEnum;
import com.rpc.common.util.LogUtil;
import com.rpc.common.util.StringUtil;
import com.rpc.registry.api.ServiceDiscovery;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-11 17:11
 * @Description: service discovery for client side
 */
public class ZKServiceDiscovery implements ServiceDiscovery {
    private static CuratorFramework zkClient;
    //list to store service address info from ZK
    private volatile static Map<String, List<String>> serviceMap = new ConcurrentHashMap<>();
    private static PathChildrenCache serviceNamePathChildCache;


    static {
        //init ZK client, assign retry configuration, connect ZK server by curator
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Integer.parseInt(ZooKeeperConfigurationEnum.ZK_CONNECT_SLEEP_TIME.getValue()), Integer.parseInt(ZooKeeperConfigurationEnum.ZK_CONNECT_MAX_RETRY_TIMES.getValue()));
        zkClient = CuratorFrameworkFactory.newClient(ZooKeeperConfigurationEnum.ZK_SERVER_ADDRESS.getValue(), retryPolicy);
        zkClient.start();


        //init path children cache
        try {
            //listen to service name nodes
            serviceNamePathChildCache = new PathChildrenCache(zkClient, ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue(), true);
            serviceNamePathChildCache.start();
            serviceNamePathChildCache.getListenable().addListener(new ServiceNameListener(serviceMap));
            //init service map and listen to service address nodes
            List<String> serverNamePaths = zkClient.getChildren().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue());
            //init map
            initServiceMap(serverNamePaths);
            for (String serverNamePath : serverNamePaths) {
                //add listener
                PathChildrenCache serviceAddressPathChildCache = new PathChildrenCache(zkClient, ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serverNamePath, true);
                serviceAddressPathChildCache.start();
                serviceAddressPathChildCache.getListenable().addListener(new ServiceAddressListener(serviceMap, serverNamePath));
            }
        } catch (Exception e) {
            LogUtil.logError(ZKServiceDiscovery.class, e.getMessage());
        }
    }

    private static void initServiceMap(List<String> serverNamePaths) {
        for (String serverNamePath : serverNamePaths) {
            try {
                List<String> serverAddressNodes = zkClient.getChildren().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serverNamePath);
                if (serverAddressNodes != null && !serverAddressNodes.isEmpty()) {
                    serviceMap.put(serverNamePath, serverAddressNodes);
                }
            } catch (Exception e) {
                LogUtil.logError(ZKServiceDiscovery.class, e.getMessage());
            }
        }
    }


    //discover and get node in node list
    public String discover(String serviceName) {
        List<String> serviceAddressList = serviceMap.get(serviceName);
        if (serviceAddressList == null || serviceAddressList.isEmpty()) {
            return null;
        }
        int size = serviceAddressList.size();
        if (size == 1) {
            //only 1 address node, use it
            LogUtil.logInfo(ZKServiceDiscovery.class, LogTipEnum.SERVICE + serviceName + LogTipEnum.DISCOVERY_SELECT_ONE_NODE + serviceAddressList.get(0));
            return serviceAddressList.get(0);
        } else {
            //multiple nodes, random select
            String dataNode = serviceAddressList.get(ThreadLocalRandom.current().nextInt(size));
            LogUtil.logInfo(ZKServiceDiscovery.class, LogTipEnum.SERVICE + serviceName + LogTipEnum.DISCOVERY_SELECT_RANDOM_NODE + dataNode);
            return dataNode;
        }
    }
}
