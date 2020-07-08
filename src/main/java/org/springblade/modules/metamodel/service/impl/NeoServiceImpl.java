package org.springblade.modules.metamodel.service.impl;

import org.neo4j.driver.Record;
import org.neo4j.driver.internal.InternalNode;
import org.springblade.modules.metamodel.service.NeoService;
import org.springblade.modules.metamodel.utils.Neo4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class NeoServiceImpl implements NeoService {

	@Autowired
	private Neo4jUtil neo4jUtil;

	/**
	 * 创建Neo4j Node
	 * @param Label  标签
	 * @param properties  key-value映射列表，map中至少包含“key”和“value”两个键
	 * @return
	 */
	@Override
	public boolean addNode(String Label, List<Map<String, Object>> properties) {
		String cql = "create (:"+Label+"{";
		if(properties!=null){
			for(Map<String,Object> property : properties){
				if(property.get("key")!=null && property.get("value")!=null){
					String mapKey = property.get("key").toString();
					Object mapValue = property.get("value").toString();
					if(mapValue instanceof String){
						cql += mapKey+":'"+mapValue+"',";
					}else if(mapValue instanceof Integer){
						cql += mapKey+":"+mapValue+",";
					}
				}
				if(cql.lastIndexOf(",")==cql.length()-1){
					cql = cql.substring(0,cql.length()-1);
				}
			}
			cql = cql.substring(0,cql.length()-1);
		}
		cql += "})";
		neo4jUtil.add(cql);
		return true;
	}

	/**
	 * 获取图数据库中所有节点 的id 和 name属性值
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAllNodesAsSelect() {
		List<Map<String, Object>> list = new ArrayList<>();
		String cql = "match (n) return id(n) as value,n.name as label";
		List<Record> records = neo4jUtil.getNeoNodes(cql);
		for(Record r : records){
			list.add(r.asMap());
		}
		return list;
	}

	@Override
	public boolean addRelation(Integer startNode, Integer endNode, String label, List<Map<String, Object>> properties) {
		String cql = "match (m) where id(m)="+startNode+" match (n) where id(n)="+endNode+" create (m)-"+"[:"+label+"{";
		if(properties!=null){
			for(Map<String,Object> property : properties){
				if(property.get("key")!=null && property.get("value")!=null){
					String mapKey = property.get("key").toString();
					Object mapValue = property.get("value").toString();
					if(mapValue instanceof String){
						cql += mapKey+":'"+mapValue+"',";
					}else if(mapValue instanceof Integer){
						cql += mapKey+":"+mapValue+",";
					}
				}
			}
			if(cql.lastIndexOf(",")==cql.length()-1){
				cql = cql.substring(0,cql.length()-1);
			}
		}
		cql += "}]->(n)";
		neo4jUtil.add(cql);
		return true;
	}
}
