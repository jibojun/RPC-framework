package com.rpc.registry.zookeeper;

import com.rpc.common.configuration.ZooKeeperConfigurationEnum;
import com.rpc.registry.api.ServiceRegistry;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-11 17:11
 * @Description:
 */
public class ZKServiceRegistry implements ServiceRegistry {
    private static CuratorFramework zkClient;

    static{
        //init ZK client, assign retry configuration, connect ZK server by curator
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Integer.getInteger(ZooKeeperConfigurationEnum.ZK_CONNECT_SLEEP_TIME.getValue()),Integer.getInteger( ZooKeeperConfigurationEnum.ZK_CONNECT_MAX_RETRY_TIMES.getValue()));
        zkClient = CuratorFrameworkFactory.newClient(ZooKeeperConfigurationEnum.ZK_SERVER_ADDRESS.getValue(), retryPolicy);
        zkClient.start();
    }

    public void registerService(String serverAddress) {
        //if server address is not null, connect ZK server and create data node for this service
        if (serverAddress != null) {

        }
    }

    public void createDataNode(String serverAddress) {
        try {
            byte[] data = serverAddress.getBytes();
            if (zkClient.checkExists().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue())==null) {
                zkClient.create().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue(),null);
            }
            //data node
            zkClient.create().forPath(ZooKeeperConfigurationEnum.ZK_DATA_PATH.getValue(),data);
        }catch(Exception e){

        }
    }
}
