/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nqcx.commons.lang.domain.EntityBase;
import org.nqcx.commons.lang.page.PageBuilder;
import org.nqcx.commons.lang.sort.SortBase;

/**
 * 
 * @author nqcx 2013-4-3 下午6:02:53
 * 
 */
public class DTO<O> extends EntityBase {

	// 成功标记
	private boolean success = false;

	// id
	protected long id;
	// ids
	protected long[] ids;
	// 实体对象
	protected O object;
	// 实体对象列表
	protected List<O> list;

	// 参数列表
	protected Map<String, Object> paramsMap;
	// 返回结果
	protected Map<String, Object> resultMap;

	// 分页
	protected PageBuilder page;
	// 排序
	protected SortBase sort;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long[] getIds() {
		return ids;
	}

	public void setIds(long[] ids) {
		this.ids = ids;
	}

	/**
	 * 取实体对象
	 * 
	 * @return object
	 */
	public O getObject() {
		return object;
	}

	public void setObject(O object) {
		this.object = object;
	}

	/**
	 * 取实体对象列表
	 * 
	 * @return 实体对象的List
	 */
	public List<O> getList() {
		return list;
	}

	public void setList(List<O> list) {
		this.list = list;
	}

	public Map<String, Object> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}

	public void putParam(String key, Object value) {
		if (this.paramsMap == null)
			this.paramsMap = new HashMap<String, Object>();
		this.paramsMap.put(key, value);
	}

	public Object getParam(String key) {
		return this.paramsMap == null ? null : this.paramsMap.get(key);
	}

	public void removeParam(String key) {
		if (this.paramsMap != null)
			this.paramsMap.remove(key);
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public void putResult(String key, Object value) {
		if (this.resultMap == null)
			this.resultMap = new HashMap<String, Object>();
		this.resultMap.put(key, value);
	}

	public Object getResult(String key) {
		return this.resultMap == null ? null : this.resultMap.get(key);
	}

	public void removeResult(String key) {
		if (this.resultMap != null)
			this.resultMap.remove(key);
	}

	public PageBuilder getPage() {
		return page;
	}

	public void setPage(PageBuilder page) {
		this.page = page;
	}

	public long getTotalCount() {
		return getPage() == null ? 0 : getPage().getTotalCount();
	}

	public SortBase getSort() {
		return sort;
	}

	public void setSort(SortBase sort) {
		this.sort = sort;
	}
}
