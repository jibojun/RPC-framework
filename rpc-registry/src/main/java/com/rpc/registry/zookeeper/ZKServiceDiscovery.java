package com.rpc.registry.zookeeper;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.configuration.ZooKeeperConfigurationEnum;
import com.rpc.common.logger.LogUtil;
import com.rpc.registry.api.ServiceDiscovery;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-11 17:11
 * @Description: service discovery for client side
 */
public class ZKServiceDiscovery implements ServiceDiscovery {
    private static CuratorFramework zkClient;
    //list to store address info from ZK
    private volatile static List<String> addressList = new ArrayList<>();

    static {
        //init ZK client, assign retry configuration, connect ZK server by curator
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Integer.getInteger(ZooKeeperConfigurationEnum.ZK_CONNECT_SLEEP_TIME.getValue()), Integer.getInteger(ZooKeeperConfigurationEnum.ZK_CONNECT_MAX_RETRY_TIMES.getValue()));
        zkClient = CuratorFrameworkFactory.newClient(ZooKeeperConfigurationEnum.ZK_SERVER_ADDRESS.getValue(), retryPolicy);
        //add listener to watch node change
        zkClient.getCuratorListenable().addListener(new NodeListener(addressList));
        zkClient.start();
    }

    public ZKServiceDiscovery() {
        try {
            //watch registry node's children
            addWatch(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue(), false);
        } catch (Exception e) {
            LogUtil.logError(ZKServiceDiscovery.class, e.getMessage());
        }
    }

    //discover and get node in node list
    public String discover() {
        int size = addressList.size();
        if (size > 0) {
            if (size == 1) {
                //only 1 node, use it
                LogUtil.logInfo(ZKServiceDiscovery.class, LogTipEnum.DISCOVERY_SELECT_ONE_NODE + addressList.get(0));
                return addressList.get(0);
            } else {
                //multiple nodes, random select
                String dataNode = addressList.get(ThreadLocalRandom.current().nextInt(size));
                LogUtil.logInfo(ZKServiceDiscovery.class, LogTipEnum.DISCOVERY_SELECT_RANDOM_NODE + dataNode);
                return dataNode;
            }
        }
        return null;
    }

    private static void addWatch(String node, boolean isSelf) throws Exception {
        if (isSelf) {
            zkClient.getData().watched().forPath(node);
        } else {
            zkClient.getChildren().watched().forPath(node);
        }
    }
}
