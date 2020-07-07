package org.springblade.modules.metamodel.service.impl;

import org.springblade.modules.metamodel.service.NeoService;
import org.springblade.modules.metamodel.utils.Neo4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class NeoServiceImpl implements NeoService {

	@Autowired
	private Neo4jUtil neo4jUtil;

	@Override
	public boolean addNode(String Label, List<Map<String, Object>> properties) {
		String cql = "create (:"+Label+"{";
		if(properties!=null){
			for(Map<String,Object> property : properties){
				String mapKey = property.get("key").toString();
				Object mapValue = property.get("value").toString();
				if(mapValue instanceof String){
					cql += mapKey+":'"+mapValue+"',";
				}else if(mapValue instanceof Integer){
					cql += mapKey+":"+mapValue+",";
				}
			}
			cql = cql.substring(0,cql.length()-1);
		}
		cql += "})";
		neo4jUtil.add(cql);
		return true;
	}
}
