package com.dajiaoyun.community.lowcoding.demo.dao;

public class DemoDAO {
	public static String getTopics() {
		String sql="select * from flower_game_topics where topic_parent_category_no='01' and topic_actived='1'";
		return sql;
		
	}
	
	public static String getTopicLineItems(String topicParentId) {
		String sql="select * from flower_game_topics_lineitems where topic_parent_id='"+topicParentId+"' and topic_actived='1'";
		return sql;
		
	}
}
