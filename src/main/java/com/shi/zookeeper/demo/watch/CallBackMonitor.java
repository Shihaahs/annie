package com.shi.zookeeper.demo.watch;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;

import static org.apache.zookeeper.KeeperException.Code;


/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2020/4/2 10:43 上午
 */
public class CallBackMonitor implements StatCallback {

    private ZooKeeper zk;
    private String zNode;
    boolean dead;
    private DataCallBackMonitor dataCallBackMonitor;
    private byte[] preData;

    public CallBackMonitor(ZooKeeper zk, String zNode, DataCallBackMonitor dataCallBackMonitor) {
        this.zk = zk;
        this.zNode = zNode;
        this.dataCallBackMonitor = dataCallBackMonitor;

        //zk上node创建监听事件
        zk.exists(zNode, true, this, null);
    }

    /**
     * 异步方法收到zk集群数据后回调触发
     */
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        boolean exists;
        switch (i) {
            case Code.Ok:
                exists = true;
                break;
            case Code.NoNode:
                exists = false;
                break;
            case Code.SessionExpired:
            case Code.NoAuth:
                dead = true;
                dataCallBackMonitor.closing(i);
                return;
            default:
                zk.exists(zNode, true, this, null);
                return;
        }
        byte[] b = null;
        if (exists) {
            try {
                b = zk.getData(zNode, false, null);
            } catch (InterruptedException e) {
                return;
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        }
        if ((null == b && b != preData) || (b != null && !Arrays.equals(preData, b))) {
            dataCallBackMonitor.exists(b);
            preData = b;
        }

    }

    public void handle(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType().equals(Watcher.Event.EventType.None)) {
            switch (event.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    //zk 事件过期
                    dead = true;
                    dataCallBackMonitor.closing(KeeperException.Code.SESSIONEXPIRED.intValue());
                    break;
            }
        } else {
            if (path != null && path.equals(zNode)) {
                zk.exists(zNode, true, this, null);
            }
        }
    }


    public interface DataCallBackMonitor {

        void exists(byte[] data);

        void closing(int rc);
    }
}
