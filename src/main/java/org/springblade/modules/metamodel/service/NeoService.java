package org.springblade.modules.metamodel.service;

import java.util.List;
import java.util.Map;

public interface NeoService {
	boolean addNode(String Label, List<Map<String, Object>> properties);

	List<Map<String, Object>> getAllNodesAsSelect();

	boolean addRelation(Integer startNode, Integer endNode, String label, List<Map<String, Object>> properties);
}
