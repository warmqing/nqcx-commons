/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.zk;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 创建 zk 连接
 *
 * @author naqichuan 16/2/29 12:57
 */
public class Zk implements Watcher {

    private final static Logger logger = LoggerFactory.getLogger(Zk.class);

    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private ZooKeeper zookeeper;
    private ZkConfig zkConfig;

    /**
     * 默认构造函数
     */
    public Zk() {

    }

    /**
     * 构造函数
     *
     * @param zkConfig
     */
    public Zk(ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
    }

    /**
     * 取得 zk 实例
     *
     * @return
     */
    public Zk connectZk() {
        if (zkConfig == null)
            throw new RuntimeException("找不到 zk config");

        return this.connectZk(this.zkConfig);
    }

    /**
     * 创建ZK连接
     *
     * @param zkConfig
     * @return
     */
    private Zk connectZk(ZkConfig zkConfig) {
        this.setZkConfig(zkConfig);
        try {
            zookeeper = new ZooKeeper(this.zkConfig.getConnetString(), this.zkConfig.getSessionTimeout(), this);
            connectedSemaphore.await();
        } catch (Exception e) {
            logger.error("创建连接失败", e);
        }

        return this;
    }

    @Override
    public void process(WatchedEvent event) {
        logger.info("Zk 监听收到通知，KeeperState: {}, EventType: {}, Instance: {}", event.getState(), event.getType(), zookeeper);

        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType()) {
                //连接成功
                connectedSemaphore.countDown();
                logger.info("Zk 连接成功，KeeperState: {}, EventType: {}", event.getState(), event.getType());
                // 连接成功调用通知
                connectedNotify();
            }
        } else if (Event.KeeperState.Expired == event.getState()) {
            logger.info("Zk 连接过期，需要重新连接，KeeperState: {}, EventType: {}", event.getState(), event.getType());
            connectedSemaphore = new CountDownLatch(1);
            this.connectZk();
        } else if (Event.KeeperState.Disconnected == event.getState()) {
            logger.info("Zk 失去连接，将自动重连，KeeperState: {}, EventType: {}", event.getState(), event.getType());

            // 调用失去连接通知
            disconnectedNotify();

            // zk 会自动重连，无需单独处理
        } else if (Event.KeeperState.AuthFailed == event.getState()) {
            throw new RuntimeException("Zk 连接验证失败，KeeperState: " + event.getState() + ", EventType: " + event.getType());
        } else {
            logger.info("Zk nothing to do，KeeperState: {}, EventType: {}", event.getState(), event.getType());
            // nothing to do
        }
    }

    /**
     * 连接或重连接成功通知
     */
    protected void connectedNotify() {
        logger.info("Zk 连接成功， 调用连接成功通知接口。");
    }

    /**
     * 失去连接通知
     */
    protected void disconnectedNotify() {
        logger.info("Zk 失去连接， 调用失去连接通知接口。");
    }

    /**
     * 连接节点，不存在是创建
     *
     * @param path
     * @param data
     * @param watcher
     * @return
     */
    public boolean connectNode(String path, String data, Watcher watcher) {
        return this.connectNode(path, data, false, watcher);
    }

    /**
     * 连接节点，不存在是创建
     *
     * @param path        节点path
     * @param data        初始数据内容
     * @param isEphemeral
     * @param watcher
     * @return
     */
    public boolean connectNode(String path, String data, boolean isEphemeral, Watcher watcher) {
        if (StringUtils.isBlank(path)) {
            logger.error("连接节点失败");
            return false;
        }

        try {
            if (!this.exists(path)) {
                zookeeper.create(path,
                        data == null ? null : data.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        isEphemeral ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT);
            }

            // 为 path 增加 watcher，并返回结果
            return exists(path, watcher);
        } catch (Exception e) {
            logger.error("连接节点失败", e);
        }

        return false;
    }

    /**
     * 取得字符串格式的节点数据
     *
     * @param path
     * @return
     */
    protected String getDataString(String path) {
        return getDataString(path, null);
    }

    /**
     * 取得字符串格式的节点数据
     *
     * @param path
     * @param watcher
     * @return
     */
    protected String getDataString(String path, Watcher watcher) {
        byte[] data = this.getData(path, watcher);
        if (data == null)
            return null;

        return data == null ? null : new String(data);

    }

    /**
     * 读取指定节点数据内容
     *
     * @param path
     * @param watcher
     * @return
     */
    protected byte[] getData(String path, Watcher watcher) {
        try {
            if (this.exists(path))
                return getZookeeper().getData(path, watcher, null);
        } catch (Exception e) {
            logger.error("读取数据失败，path: " + path, e);
        }

        return null;
    }

    /**
     * 更新指定节点数据内容
     *
     * @param path
     * @param data
     * @return
     */
    protected Stat setData(String path, String data) {
        try {
            if (this.exists(path))
                return getZookeeper().setData(path, data == null ? null : data.getBytes(), -1);
        } catch (Exception e) {
            logger.error("更新数据失败，path: " + path, e);
        }

        return null;
    }

    /**
     * 删除指定节点
     *
     * @param path 节点path
     */
    protected void delete(String path) {
        try {
            if (this.exists(path))
                getZookeeper().delete(path, -1);
        } catch (Exception e) {
            logger.error("删除节点失败，path: " + path, e);
        }
    }

    /**
     * 检查节点是否存在
     *
     * @param path
     * @return true or false
     */
    protected boolean exists(String path) {
        return this.exists(path, null);
    }

    /**
     * 检查节点是否存在
     *
     * @param path
     * @return true or false
     */
    protected boolean exists(String path, Watcher watcher) {
        return watchNode(path, watcher) != null;
    }

    /**
     * 给节点增加监听
     */
    protected Stat watchNode(String path, Watcher watcher) {
        try {
            return getZookeeper().exists(path, watcher);
        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }


    /**
     * 取得下级列表
     *
     * @param path
     * @return
     */
    protected List<String> getChildren(String path) {
        return watchChild(path, null);
    }

    /**
     * 监听子节点
     *
     * @param path    节点 path
     * @param watcher watcher
     * @return 返回节点 path 的 wathc 列表
     */
    protected List<String> watchChild(String path, Watcher watcher) {
        try {
            if (this.exists(path))
                return getZookeeper().getChildren(path, watcher);
        } catch (KeeperException e) {
            logger.error("", e);
        } catch (InterruptedException e) {
            logger.error("", e);
        }

        return null;
    }

    /**
     * 关闭链接
     */
    protected void close() {
        try {
            if (zookeeper != null)
                zookeeper.close();
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    public ZkConfig getZkConfig() {
        return zkConfig;
    }

    public void setZkConfig(ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
    }

    public ZooKeeper getZookeeper() {
        if (zookeeper == null)
            this.connectZk();
        return zookeeper;
    }

    public void setZookeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }
}
