
package com.inus.zoo.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@ Component
public class RegisterServer
{

    private String addrs;
    private int timeout = 2000;
    private ZooKeeper zooKeeper;
    private final static String SERVER_NAME = "/server/dept";

    // 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    public RegisterServer(@ Value("${zoo.addrs}") String addrs)
    {
        this.addrs = addrs;
        try
        {
            this.zooKeeper = new ZooKeeper(this.addrs, timeout, new Watcher()
            {
                public void process(WatchedEvent event)
                {
                    KeeperState state = event.getState();
                    if (Event.KeeperState.SyncConnected == state)
                    {
                        // 建立连接成功,发送信号量,让后续阻塞程序向下执行
                        COUNT_DOWN_LATCH.countDown();
                    }
                }
            });

            COUNT_DOWN_LATCH.await();

            // 查看节点是否存在
            Stat stat = zooKeeper.exists(SERVER_NAME, false);
            if (stat == null)
            {
                // 创建节点
                zooKeeper.create(SERVER_NAME, "dept".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            // 注册服务,无需监听
            zooKeeper.create(SERVER_NAME + "/127.0.0.1:4001", "dept_server".getBytes(), Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
