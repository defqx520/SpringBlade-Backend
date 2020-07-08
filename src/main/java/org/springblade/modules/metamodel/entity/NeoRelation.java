package org.springblade.modules.metamodel.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class NeoRelation {
	private Integer startNode;
	private Integer endNode;
	private String label;
	private List<Map<String, Object>> dynamic;
}
