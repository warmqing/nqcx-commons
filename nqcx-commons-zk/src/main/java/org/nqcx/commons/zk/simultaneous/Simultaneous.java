/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.zk.simultaneous;


import org.nqcx.commons.zk.ZkNode;

import java.util.List;

/**
 * 并行执行接口
 *
 * @author naqichuan 16/3/7 09:31
 */
public interface Simultaneous {

    /**
     * 分配任务
     */
    void assign(List<ZkNode> zkNodes);

    /**
     * 接任务执行程序
     */
    void proccess(ZkNode zkNode);
}
