/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package cn.chineseall.commons.zk.simultaneous;

import cn.chineseall.commons.zk.Zk;
import cn.chineseall.commons.zk.ZkConfig;
import cn.chineseall.commons.zk.ZkNode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.nqcx.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 并行执行 zk 连接，继承自 Zk
 *
 * @author naqichuan 16/3/4 16:57
 */
public class SimultaneousZk extends Zk {

    private final static Logger logger = LoggerFactory.getLogger(SimultaneousZk.class);

    private final Timer timer = new Timer();
    private TimerTask task;

    private String root = "/nqcx"; // 默认节点根目录
    private int taskPeriod = 5 * 60 * 1000; // 默认任务执行相隔时间 5 分钟
    private Simultaneous simultaneous;

    // My zk node
    private ZkNode zkNode;
    private boolean isMaster = false;

    public SimultaneousZk() {
        super();
    }

    public SimultaneousZk(ZkConfig zkConfig) {
        super(zkConfig);
    }

    @Override
    protected void connectedNotify() {
        super.connectedNotify();
        if (this.zkNode == null || this.zkNode.getCzxid() == 0 || StringUtils.isBlank(this.zkNode.getPath()))
            return;

        init();
    }

    @Override
    protected void disconnectedNotify() {
        super.disconnectedNotify();

        // 让主节点失效，不执行任务分配程序
        this.isMaster = false;
        if (task != null)
            task.cancel();
        task = null;
        timer.purge();
    }

    public void init() {
        // 连接 root 节点，设置监听，收到自身节点变动或子节点变动消息后进行选主
        rootZkNode();

        // 连接自身节点，设置监听，收到自自节点数据变化时处理业务逻辑
        selfZkNode();
    }

    /**
     * 执行任务
     */
    private void runTask() {
        if (task == null) {
            timer.schedule(task = new TimerTask() {

                @Override
                public void run() {
                    if (!isMaster)
                        return;

                    logger.info("我是 master，我将执行任务分配");

                    if (simultaneous == null) {
                        logger.info("找不到任务任务分配程序，simultaneous: " + simultaneous);
                        return;
                    }

                    // 确认节点
                    List<String> childrens = getChildren(root);
                    if (childrens == null || childrens.size() == 0)
                        return;
                    List<ZkNode> zkNodes = new ArrayList<ZkNode>();
                    ZkNode zkNode = null;
                    for (String children : childrens) {
                        if (StringUtils.isBlank(children))
                            continue;

                        zkNodes.add(zkNode = new ZkNode());
                        zkNode.setPath(root + "/" + children);
                    }

                    try {
                        // 执行分配任务
                        simultaneous.assign(zkNodes);
                        // 将分配结果回写到 zk， 完成任务分配
                        if (zkNodes != null && zkNodes.size() > 0) {
                            for (ZkNode zn : zkNodes) {
                                setData(zn.getPath(), zn.getData());
                            }
                        }
                    } catch (Throwable e) {
                        logger.error("", e);
                    }

                }
            }, 1000, taskPeriod);
        }
    }

    /**
     * 连接 root 节点
     */
    private void rootZkNode() {
        Watcher watcher = null;
        // 创建或连接 root 节点，同时注册监听 watcher
        boolean success = super.connectNode(root, null, watcher = new Watcher() {

                    @Override
                    public void process(WatchedEvent event) {
                        logger.info("Root 监听到通知，KeeperState: {}，EventType: {}, root: {}",
                                event.getState(), event.getType(), root);

                        if (Event.KeeperState.SyncConnected == event.getState()) {
                            if (Event.EventType.NodeChildrenChanged == event.getType())
                                // 执行选主
                                isMasterNode();

                            // 连接成功，启动 timer
                            runTask();

                            // 重新注册节点监听 watcher
                            watchNode(root, this);
                            // 重新注册子节点监听 watcher
                            watchChild(root, this);
                        } else if (Event.KeeperState.Disconnected == event.getState()) {
                            isMaster = false;
                            if (task != null)
                                task.cancel();
                            task = null;
                            timer.purge();
                        }
                    }
                }

        );

        if (success) {
            // 注册节点监听 watcher
            watchNode(root, watcher);
            // 注册子节点监听 watcher
            watchChild(root, watcher);
        }
    }


    /**
     * 用于判断是否为 master node
     *
     * @return
     */

    private void isMasterNode() {
        this.isMaster = false;

        if (this.zkNode == null || this.zkNode.getCzxid() == 0 || StringUtils.isBlank(this.zkNode.getPath()))
            return;

        try {
            Stat stat = getZookeeper().exists(this.zkNode.getPath(), false);
            if (stat == null)
                return;

            long czxid = stat.getCzxid();

            List<String> list = this.getChildren(root);
            if (list == null || list.size() == 0)
                return;

            Stat nStat = null;
            long nMinCzxid = 0;
            for (String node : list) {
                if ((nStat = getZookeeper().exists(root + "/" + node, false)) == null || nStat.getCzxid() == 0)
                    continue;

                if (nMinCzxid == 0 || nMinCzxid > nStat.getCzxid())
                    nMinCzxid = nStat.getCzxid();
            }

            this.isMaster = (czxid == nMinCzxid);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * 自身节点
     */
    private void selfZkNode() {
        this.isMaster = false;

        // 确认自身，节点不存在，将节点对象置空
        if (this.zkNode != null
                && (this.zkNode.getCzxid() == 0 || StringUtils.isBlank(this.zkNode.getPath()) || !exists(this.zkNode.getPath())))
            this.zkNode = null;

        // 新初始化节点信息
        if (this.zkNode == null) {
            this.zkNode = new ZkNode();
            this.zkNode.setPath(this.getSelfZkNodePath());
        }
        this.zkNode.setData(null);

        // 创建节点
        boolean success = this.connectNode(this.zkNode.getPath(), this.zkNode.getData(), true, new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        logger.info("Node 监听到通知，KeeperState: {}，EventType: {}, ZkNode: {}",
                                event.getState(), event.getType(), zkNode);

                        if (Event.KeeperState.SyncConnected == event.getState()) {
                            if (Event.EventType.NodeDeleted == event.getType()) {
                                // 重新连接节点
                                logger.info("节点被删除，ZkNode: " + zkNode + ", 需要重新创建节点！");
                                selfZkNode();
                                return;
                            } else if (Event.EventType.NodeDataChanged == event.getType()) {
                                // 数据变化，执行业务逻辑
                                String data = getDataString(zkNode.getPath());

                                if (StringUtils.isNotBlank(data) && simultaneous != null) {
                                    zkNode.setData(data);

                                    logger.info("节点数据有变化，正在执行业务处理过程...");
                                    try {
                                        simultaneous.proccess(zkNode);
                                    } catch (Throwable e) {
                                        logger.error("", e);
                                    }
                                    logger.info("节点数据有变化，执行业务处理过程结束。");

                                    zkNode.setData(null);
                                    setData(zkNode.getPath(), zkNode.getData());
                                }
                            } else {
                                // nothing to do
                            }

                            watchNode(zkNode.getPath(), this);
                        }
                    }
                }
        );
        if (!success) {
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                // nothing to do
            }
            selfZkNode();
            return;
        }

        // 设置 czxid
        try {
            Stat stat = getZookeeper().exists(this.zkNode.getPath(), false);
            if (stat == null)
                return;

            this.zkNode.setCzxid(stat.getCzxid());
        } catch (Exception e) {
            logger.error("", e);
        }

        // 执行一次选主
        this.isMasterNode();
    }

    /**
     * 取得自身节点的 path
     *
     * @return
     */
    private String getSelfZkNodePath() {

        try {
            // 随机 sleep 1~10秒
            Thread.sleep((new Random().nextInt(9) + 1) * 1000);
        } catch (InterruptedException e) {
            // nothing to do
        }

        List<String> nodes = getChildren(root);

        long max = 0;
        if (nodes != null && nodes.size() > 0) {
            long n = 0;
            String[] nodeArray = null;
            for (String node : nodes) {
                if (node == null)
                    continue;

                if ((nodeArray = node.split("_")).length != 2) {
                    // 非法节点，删除掉
                    delete(root + "/" + node);
                    continue;
                }

                n = Long.parseLong(nodeArray[1]);
                if (max < n)
                    max = n;
            }
        }

        return root + "/" + StringUtils.randomCharAndNum(5).toUpperCase() + "_" + String.valueOf(max + 1);
    }

    /**
     * @param root
     */
    public void setRoot(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }

    public int getTaskPeriod() {
        return taskPeriod;
    }

    public void setTaskPeriod(int taskPeriod) {
        this.taskPeriod = taskPeriod;
    }

    public Simultaneous getSimultaneous() {
        return simultaneous;
    }

    public void setSimultaneous(Simultaneous simultaneous) {
        this.simultaneous = simultaneous;
    }


//    /**
//     * @param args
//     * @throws InterruptedException
//     */
//    public static void main(String[] args) throws InterruptedException {
//        ZkConfig zkConfig = new ZkConfig("192.168.0.196:2181,192.168.0.196:2182,192.168.0.196:2183", 20 * 1000);
//        SimultaneousZk czk = new SimultaneousZk(zkConfig);
////        czk.connectZk();
//        czk.setRoot("/nqcx");
//        czk.setTaskPeriod(10 * 1000);
//
//        czk.setSimultaneous(new Simultaneous() {
//            @Override
//            public void assign(List<ZkNode> zkNodes) {
//                System.out.println("******************" + zkNodes);
//                for (ZkNode zkNode : zkNodes) {
//                    zkNode.setData("1");
//                }
//            }
//
//            @Override
//            public void proccess(ZkNode zkNode) {
//                System.out.println("==================" + zkNode);
//            }
//        });
//
//        czk.init();
//
//        Thread.sleep(10 * 60 * 1000);
//    }
}
