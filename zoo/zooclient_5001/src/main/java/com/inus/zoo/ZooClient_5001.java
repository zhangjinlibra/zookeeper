
package com.inus.zoo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.fastjson.JSONObject;

@ SpringBootApplication
public class ZooClient_5001
{
    // 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args)
    {
        SpringApplication.run(ZooClient_5001.class, args);
    }

    public static void zoo(String[] args) throws IOException, InterruptedException, KeeperException
    {
        String addrs = "192.168.67.135:2181,192.168.67.135:2182,192.168.67.135:2183";
        int timeout = 2000;
        zooKeeper = new ZooKeeper(addrs, timeout, new Watcher()
        {
            public void process(WatchedEvent event)
            {
                try
                {
                    KeeperState state = event.getState();
                    EventType type = event.getType();

                    if (Event.KeeperState.SyncConnected == state)
                    {
                        List<String> data = zooKeeper.getChildren("/inus", true);
                        System.err.println(JSONObject.toJSONString(data));

                        if (EventType.None == type)
                        {
                            // 如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                            COUNT_DOWN_LATCH.countDown();
                        }
                        else if (type == EventType.NodeCreated)
                        {
                            System.out.println("NodeCreated");
                        }
                        else if (type == EventType.NodeDataChanged)
                        {
                            System.out.println("NodeDataChanged");
                        }
                        else if (type == EventType.NodeDeleted)
                        {
                            System.out.println("NodeDeleted");
                        }
                        else if (type == EventType.NodeChildrenChanged)
                        {
                            System.out.println("NodeChildrenChanged");
                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        COUNT_DOWN_LATCH.await();
        System.out.println("zookeeper session established");

        // 创建顶级节点节点
        // zooKeeper.setData("/inus", "root-server".getBytes(), 1);

        // 创建子节点
        // zooKeeper.create("/inus/deve", "deve".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

        Stat stat = zooKeeper.exists("/dept", false);
        System.out.println(JSONObject.toJSONString(stat));

        // 获取节点信息
        List<String> data = zooKeeper.getChildren("/inus", true);
        System.err.println(JSONObject.toJSONString(data));

        zooKeeper.getData("/inus", true, null);

        Thread.sleep(Long.MAX_VALUE);
    }

}
