package org.springblade.modules.metamodel.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.modules.metamodel.entity.NeoNode;
import org.springblade.modules.metamodel.entity.NeoRelation;
import org.springblade.modules.metamodel.service.NeoService;
import org.springblade.modules.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author Chill
 */
@ApiIgnore
@RestController
@RequestMapping("blade-neo4j")
@AllArgsConstructor
public class NeoController {

	@Autowired
	private NeoService neoService;

	/**
	 * 新增图节点
	 */
	@PostMapping("/addNode")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "新增图节点", notes = "传入neoNode")
	public R addNode(@RequestBody NeoNode neoNode) {
		String label = neoNode.getLabel();
		List<Map<String, Object>> properties = neoNode.getDynamic();
		return R.status(neoService.addNode(label, properties));
	}

	/**
	 * 新增图关系
	 */
	@PostMapping("/addRelation")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "新增图节点关系", notes = "传入neoRelation")
	public R addRelation(@RequestBody NeoRelation neoRelation) {
		Integer startNode = neoRelation.getStartNode();  //起始节点的ID
		Integer endNode = neoRelation.getEndNode();
		String label = neoRelation.getLabel();
		List<Map<String, Object>> properties = neoRelation.getDynamic();
		return R.status(neoService.addRelation(startNode, endNode, label, properties));
	}

	/**
	 * select下拉框获取所有图节点
	 * @return
	 */
	@GetMapping("/select")
	@ApiOperation(value = "下拉数据源", notes = "无需传参，返回所有节点")
	public R<List<Map<String,Object>>> select() {
		List<Map<String,Object>> list = neoService.getAllNodesAsSelect();
		return R.data(list);
	}

}
