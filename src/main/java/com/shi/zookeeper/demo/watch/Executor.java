package com.shi.zookeeper.demo.watch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.*;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2020/4/2 10:45 上午
 */
public class Executor implements Watcher, Runnable, CallBackMonitor.DataCallBackMonitor {

    private String znode;
    private CallBackMonitor callBackMonitor;
    private ZooKeeper zk;
    private String pathname;
    private String[] exec;
    private Process child;

    public Executor(String hostPort, String znode, String filename, String[] exec) throws IOException {
        this.pathname = filename;
        this.exec = exec;
        zk = new ZooKeeper(hostPort, 3000, this);
        callBackMonitor = new CallBackMonitor(zk, znode, this);
    }


    /**
     * Program arguments
     *
     localhost:3181 /watch /Users/wuer/Applications/apache-zookeeper-3.6.0-bin/data/znodeTest /Users/wuer/Apple/Workspace/IdeaWorkspace/annie/target/classes/com/shi/zookeeper/demo/watch/seq.sh
     */
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("USAGE: Parse params error");
            System.exit(2);
        }
        String hostPort = args[0];
        String znode = args[1];
        String filename = args[2];
        String[] exec = new String[args.length - 3];
        System.arraycopy(args, 3, exec, 0, exec.length);
        try {
            new Executor(hostPort, znode, filename, exec).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Watcher 收到znode事件后会触发process
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        callBackMonitor.handle(watchedEvent);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!callBackMonitor.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void exists(byte[] data) {
        if (data == null) {
            if (child != null) {
                System.out.println("Killing handle");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            child = null;
        } else {
            if (child != null) {
                System.out.println("Stopping child");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(pathname));
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Starting child");
                child = Runtime.getRuntime().exec(exec);
                new StreamWriter(child.getInputStream(), System.out);
                new StreamWriter(child.getErrorStream(), System.err);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class StreamWriter extends Thread {
        OutputStream os;
        InputStream is;

        StreamWriter(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
            start();
        }

        public void run() {
            byte[] b = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException e) {
            }
        }
    }

}

