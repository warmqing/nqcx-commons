/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.controller;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.nqcx.commons.web.WebSupport;
import org.nqcx.commons.web.result.Result;
import org.nqcx.commons.web.url.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author naqichuan 2013-4-8 下午4:07:04
 * 
 */
@Controller
public class ResultController extends WebSupport {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Result rs;

	@Autowired
	@Qualifier("_homeUrl")
	private UrlBuilder homeUrl;

	@RequestMapping(value = "/error", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String error(String url, Model model) {
		return error("0", url, model);
	}

	@RequestMapping(value = "/error/{code}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String error(@PathVariable String code, String url, Model model) {
		return result("error", code, url, model);
	}

	@RequestMapping(value = "/msg", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String msg(String url, Model model) throws Exception {
		return msg("0", url, model);
	}

	@RequestMapping(value = "/msg/{code}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String msg(@PathVariable String code, String url, Model model)
			throws Exception {
		return result("msg", code, url, model);
	}

	@RequestMapping(value = "/success", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String success(String url, Model model) throws Exception {
		return success("0", url, model);
	}

	@RequestMapping(value = "/success/{code}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String success(@PathVariable String code, String url, Model model)
			throws Exception {
		return result("success", code, url, model);
	}

	private String result(String type, String code, String url, Model model) {
		if (this.rs == null)
			this.rs = new Result();

		BeanUtils.copyProperties(getResult(type, code), rs);

		try {
			rs.setIndex(homeUrl.forPath(null).build());
		} catch (MalformedURLException e) {
			logger.error("ResultController.result", e);
		}

		if (url != null && url.length() > 0)
			rs.setUrl(url);

		model.addAttribute("type", rs.getType());
		model.addAttribute("title", getPropertyValue(rs.getTitle(), null));
		model.addAttribute("subject", getPropertyValue(rs.getSubject(), null));

		if (rs.getSuggestTitle() != null)
			model.addAttribute("suggestTitle",
					getPropertyValue(rs.getSuggestTitle(), null));
		else
			model.addAttribute("suggestTitle", "");

		if (rs.getSuggest() != null && rs.getSuggest().size() > 0)
			model.addAttribute("suggest", getValue(rs.getSuggest()));
		else
			model.addAttribute("suggest", null);

		model.addAttribute("auto", getPropertyValue(rs.getAuto(), null));
		model.addAttribute("index", rs.getIndex());
		model.addAttribute("url", rs.getUrl());

		return "msg";
	}

	/**
	 * 
	 * @author naqichuan Dec 24, 2013 9:37:54 PM
	 * @param list
	 * @return
	 */
	private Object getValue(List<String> list) {
		List<String> rl = new ArrayList<String>();
		for (String code : list) {
			rl.add((String) getPropertyValue(code, null));
		}
		return rl;
	}

	public Result getResult() {
		return rs;
	}

	public void setResult(Result result) {
		this.rs = result;
	}
}
