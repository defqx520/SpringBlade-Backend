package org.springblade.modules.metamodel.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class NeoNode {
	/**
	 * 图数据库中节点的Id
	 */
	private Integer id;
	/**
	 * 节点的标签，用于描述节点所属类别
	 */
    private String label;
	/**
	 * 用于在模型层展示的节点名称
	 */
	private String neoName;
	/**
	 * 用于描述这类节点数据展示形式
	 * form: 表单样式
	 * table: 表格样式
	 * file: 文件链接样式
	 */
	private String neoResType;
	/**
	 * 其他一些键值对
	 */
	private List<Map<String, String>> other;

}
