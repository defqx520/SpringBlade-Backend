package org.springblade.modules.metamodel.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class NeoRelation {
	private Integer id;
	private Integer startNode;
	private Integer endNode;
	private String label;
	private String neoName;   //用于展示名称
	private List<Map<String, String>> other;  //边上的属性映射key value对
}
