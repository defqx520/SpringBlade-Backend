package org.springblade.modules.metamodel.service;

import org.springblade.modules.metamodel.dto.EdgeDto;
import org.springblade.modules.metamodel.dto.NodeDto;

import java.util.List;
import java.util.Map;

public interface NeoService {
	boolean addNode(String label, String neoName, String neoResType, List<Map<String, String>> other);

	List<Map<String, Object>> getAllNodesAsSelect();

	boolean addRelation(Integer startNode, Integer endNode, String label, String neoName, List<Map<String, String>> other);

	List<NodeDto> getAllNodes();

	List<EdgeDto> getAllEdges();

	List<Map<String, Object>> getNodeById(Integer nodeId);
}
