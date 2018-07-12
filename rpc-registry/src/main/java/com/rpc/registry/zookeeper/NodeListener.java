package com.rpc.registry.zookeeper;

import com.rpc.common.configuration.SeparatorEnum;
import com.rpc.common.configuration.ZooKeeperConfigurationEnum;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-12 16:05
 * @Description:
 */
public final class NodeListener implements CuratorListener {
    private volatile List<String> addressList;
    private Lock lock=new ReentrantLock();

    public NodeListener(List<String> addressList){
        this.addressList=addressList;
    }

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        final WatchedEvent watchedEvent = event.getWatchedEvent();
        if (watchedEvent != null) {
            if (watchedEvent.getState() == KeeperState.SyncConnected) {
                switch (watchedEvent.getType()) {
                    case NodeChildrenChanged:
                        //when children of registry node changed, add nodes to list
                        try{
                            lock.lock();
                            addressList.addAll(processChildrenDataNodes(client,event.getChildren()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            lock.unlock();
                        }
                        break;
                    case NodeDataChanged:
                        //no operations when this registry node changed, but monitor its children
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private List<String> processChildrenDataNodes(CuratorFramework client, List<String> childrenList) throws Exception {
        List<String> resultList=new ArrayList<>();
        for(String dataNodePath:childrenList){
            resultList.add(new String(client.getData().forPath(ZooKeeperConfigurationEnum.ZK_REGISTRY_PATH.getValue()+SeparatorEnum.URL_SEPARATOR.getValue()+dataNodePath)));
        }
        return resultList;
    }
}
