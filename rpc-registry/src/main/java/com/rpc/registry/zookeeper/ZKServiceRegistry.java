package com.rpc.registry.zookeeper;

import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.configuration.SeparatorEnum;
import com.rpc.common.configuration.ZooKeeperConfigurationEnum;
import com.rpc.common.util.LogUtil;
import com.rpc.registry.api.ServiceRegistry;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-11 17:11
 * @Description: service registry
 */
public class ZKServiceRegistry implements ServiceRegistry {
    private static CuratorFramework zkClient;

    static {
        //init ZK client, assign retry configuration, connect ZK server by curator
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Integer.parseInt(ZooKeeperConfigurationEnum.ZK_CONNECT_SLEEP_TIME.getValue()), Integer.parseInt(ZooKeeperConfigurationEnum.ZK_CONNECT_MAX_RETRY_TIMES.getValue()));
        zkClient = CuratorFrameworkFactory.newClient(ZooKeeperConfigurationEnum.ZK_SERVER_ADDRESS.getValue(), retryPolicy);
        zkClient.start();
    }

    public void registerService(String serviceName, String serverAddress) {
        //if server address is not null, connect ZK server and create data node for this service
        if (serverAddress != null && !serverAddress.isEmpty()) {
            try {
                //if no registry node, create one
                if (zkClient.checkExists().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue()) == null) {
                    zkClient.create().withMode(CreateMode.PERSISTENT).forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue(), null);
                }
                //if no service name node, create one
                if (serviceName != null && !serviceName.isEmpty() && zkClient.checkExists().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serviceName) == null) {
                    zkClient.create().withMode(CreateMode.PERSISTENT).forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serviceName, null);
                }
                if (serviceName != null && !serviceName.isEmpty() && zkClient.checkExists().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serviceName + SeparatorEnum.URL_SEPARATOR.getValue() + serverAddress) == null) {
                    //create data node with service URL, temp sequential node
                    zkClient.create().withMode(CreateMode.PERSISTENT).forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serviceName + SeparatorEnum.URL_SEPARATOR.getValue() + serverAddress, null);
                }
            } catch (Exception e) {
                LogUtil.logError(ZKServiceRegistry.class, LogTipEnum.ZK_REGISTER_SERVICE_ERROR + e.getMessage());
            }
        }
    }

    @Override
    public void unRegisterService(String serviceName, String serverAddress) {
        if (serverAddress != null) { 
            try {
                //if no service name node, create one
                if (serviceName != null && !serviceName.isEmpty() && zkClient.checkExists().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serviceName) != null) {
                    zkClient.delete().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serviceName + SeparatorEnum.URL_SEPARATOR.getValue() + serverAddress);
                    if (zkClient.getChildren().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serviceName) == null) {
                        zkClient.delete().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue() + SeparatorEnum.URL_SEPARATOR.getValue() + serviceName);
                    }
                }
            } catch (Exception e) {
                LogUtil.logError(ZKServiceRegistry.class, LogTipEnum.ZK_REGISTER_SERVICE_ERROR + e.getMessage());
            }
        }
    }
}
