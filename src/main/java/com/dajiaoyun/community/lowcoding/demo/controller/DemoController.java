package com.dajiaoyun.community.lowcoding.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dajiaoyun.community.lowcoding.demo.service.DemoService;


@RestController
@RequestMapping("/demo")
public class DemoController {
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);
	
	@Autowired
	DemoService topicService;
	
	@RequestMapping(value = "/input", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String input(HttpServletRequest request, @RequestBody String json) {
		JSONObject root = new JSONObject();
		root.put("code", "200");
		try {
			return topicService.saveUserTopics(json);
		}catch(Exception e) {
			root.put("code", "403");
			log.error("flowergame input failed",e);
		}
		return root.toJSONString();
	}
	
	@RequestMapping(value = "/topics", method = RequestMethod.GET)
	@ResponseBody
	public String list(HttpServletRequest request) {
		JSONObject root = new JSONObject();
		root.put("code", "200");
		JSONArray data=topicService.getTopics();
		root.put("data", data);
		return root.toJSONString();
	}
	
}
