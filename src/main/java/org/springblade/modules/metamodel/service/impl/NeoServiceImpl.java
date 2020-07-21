package org.springblade.modules.metamodel.service.impl;

import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.InternalNode;
import org.springblade.modules.metamodel.dto.EdgeDto;
import org.springblade.modules.metamodel.dto.NodeDto;
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
	 * @param label
	 * @param neoName     这是默认必须包含的属性  用于节点名称的展示
	 * @param neoResType  这是默认必须包含的属性  表示该节点返回数据的展现形式
	 * @param other       非必须 定义了数据表包含的<字段名称>及<中文注释>
	 * @return
	 */
	@Override
	public boolean addNode(String label, String neoName, String neoResType, List<Map<String, String>> other) {
		String cql = "create (:"+label+"{ neoName:'"+neoName+"',neoResType:'"+neoResType+"',";
		if(other!=null){
			for(Map<String,String> property : other){
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
		String cql = "match (n) return id(n) as value,n.neoName as label";
		List<Record> records = neo4jUtil.getNeoNodesOrEdges(cql);
		for(Record r : records){
			list.add(r.asMap());
		}
		return list;
	}

	@Override
	public boolean addRelation(Integer startNode, Integer endNode, String label, String neoName, List<Map<String, String>> other) {
		String cql = "match (m) where id(m)="+startNode+" match (n) where id(n)="+endNode+" create (m)-"+"[:"+label+"{neoName:'"+neoName+"',";
		if(other!=null){
			for(Map<String,String> property : other){
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

	/**
	 * 获取图数据库中所有节点
	 * @return
	 */
	@Override
	public List<NodeDto> getAllNodes() {
		List<NodeDto> list = new ArrayList<>();
		String cql = "match (n) return id(n) as id, n.neoName as label";
		List<Record> records = neo4jUtil.getNeoNodesOrEdges(cql);
		for(Record r : records){
			Map<String, Object> map = r.asMap();
			NodeDto nodeDto = new NodeDto(Integer.valueOf(map.get("id").toString()),map.get("label").toString());
			list.add(nodeDto);
		}
		return list;
	}

	/**
	 * 获取图数据库中所有边
	 * @return
	 */
	@Override
	public List<EdgeDto> getAllEdges() {
		List<EdgeDto> list = new ArrayList<>();
		String cql = "match(n)-[r]->(m) return id(r) as id, id(startnode(r)) as from,id(endnode(r)) as to, r.neoName as label";
		List<Record> records = neo4jUtil.getNeoNodesOrEdges(cql);
		for(Record r : records){
			Map<String, Object> map = r.asMap();
			EdgeDto edgeDto = new EdgeDto(Integer.valueOf(map.get("id").toString()), map.get("label").toString(), Integer.valueOf(map.get("from").toString()),Integer.valueOf(map.get("to").toString()));
			list.add(edgeDto);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getNodeById(Integer nodeId) {
		List<Map<String, Object>> list = new ArrayList<>();
		String cql = "match(n) where id(n) = "+nodeId+" return properties(n) as properties, id(n) as id, labels(n) as label";
		List<Record> records = neo4jUtil.getNeoNodesOrEdges(cql);
		if(records != null && records.size() > 0){
			for (Record r : records){
				Value value = r.values().get(0);
				for(String name : value.keys()){
					Map<String, Object> map = new HashMap<>();
					map.put("name", name);
					map.put("comment", value.get(name).asString());
					list.add(map);
				}
			}
		}
		return list;
	}
}
