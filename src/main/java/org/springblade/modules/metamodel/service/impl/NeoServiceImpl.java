package org.springblade.modules.metamodel.service.impl;

import org.springblade.modules.metamodel.service.NeoService;
import org.springblade.modules.metamodel.utils.Neo4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NeoServiceImpl implements NeoService {

	@Autowired
	private Neo4jUtil neo4jUtil;

	@Override
	public boolean addNode() {
		String cql = "create (:Person{name:\"张三\"})-[r:friendRelation]->(:Person{name:\"王五\"})";
		neo4jUtil.add(cql);
		return true;
	}
}
