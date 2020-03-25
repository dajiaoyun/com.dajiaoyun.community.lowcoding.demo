package com.dajiaoyun.community.lowcoding.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dajiaoyun.community.lowcoding.demo.dao.TopicDAO;
import com.dajiaoyun.community.lowcoding.ee.service.CommonService;
import com.dajiaoyun.community.lowcoding.ee.util.date.FlexDate;
import com.dajiaoyun.community.lowcoding.model.TableObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class TopicService  extends CommonService<Object>{
	private static final Logger log = LoggerFactory.getLogger(TopicService.class);
	
	public JSONArray getTopics() {
		String sql=TopicDAO.getTopics();
		JSONArray data=this.queryJSONArray(sql);
		if(data!=null) {
			for(int i=0;i<data.size();i++) {
				JSONObject obj=data.getJSONObject(i);
				String topicParentId=obj.getString("topic_id");
				sql=TopicDAO.getTopicLineItems(topicParentId);
				JSONArray children=this.queryJSONArray(sql);
				obj.put("children", children);
			}
		}
		return data;
	}
	
	public String saveUserTopics(String json) {
		JSONObject root = new JSONObject();
		root.put("code", "200");
		try {
			JSONObject jsonObj=JSONObject.parseObject(json);
			JSONArray topics=jsonObj.getJSONArray("topics");
			String user_no=jsonObj.getString("user_no");
			for(int i=0;i<topics.size();i++) {
				JSONObject topic=topics.getJSONObject(i);
				String topic_parent_id=topic.getString("topic_parent_id");
				JSONArray topicLineItems=topic.getJSONArray("children");
				for(int j=0;j<topicLineItems.size();j++) {
					JSONObject topicLineitem=topicLineItems.getJSONObject(i);
					String topic_lineitem_id=topicLineitem.getString("topic_lineitem_id");
					TableObject vo=new TableObject();
					vo.setTableName("flower_user_topics");
					vo.setPrimaryKey("pk_id");
					vo.setPrimaryType(TableObject.INT);
					vo.setDbAutoGenerate(true);
					vo.settenant(false);
					vo.setAttribute("user_no", user_no);
					vo.setAttribute("topic_parent_id", topic_parent_id);
					vo.setAttribute("topic_lineitem_id", topic_lineitem_id);
					vo.setAttribute("createdate", FlexDate.getFullDate());
					
					this.insert(vo);
				}
			}
			
		}catch(Exception e) {
			root.put("code", "403");
			log.error("flowergame input failed",e);
		}
		
		return root.toJSONString();
	}
}
