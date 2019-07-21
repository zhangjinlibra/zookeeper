
package com.inus.zoo.zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@ Component
public class WatchServer
{

    private String addrs;
    private int timeout = 2000;
    private ZooKeeper zooKeeper;
    private final static String SERVER_NAME = "/server/dept";

    private List<String> deptServerList = null;

    // 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    public WatchServer(@ Value("${zoo.addrs}") String addrs)
    {
        this.addrs = addrs;
        try
        {
            this.zooKeeper = new ZooKeeper(this.addrs, timeout, new Watcher()
            {
                public void process(WatchedEvent event)
                {
                    try
                    {
                        KeeperState state = event.getState();
                        if (Event.KeeperState.SyncConnected == state)
                        {
                            if (event.getType() == Watcher.Event.EventType.None)
                            {
                                COUNT_DOWN_LATCH.countDown();
                            }
                            else if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged)
                            {
                                deptServerList = zooKeeper.getChildren(SERVER_NAME, true);
                                System.out.println(JSONObject.toJSONString(deptServerList));
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });

            COUNT_DOWN_LATCH.await();

            // 监听节点信息
            deptServerList = zooKeeper.getChildren(SERVER_NAME, true);
            System.out.println(JSONObject.toJSONString(deptServerList));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<String> getDeptServerList()
    {
        return deptServerList;
    }

}
