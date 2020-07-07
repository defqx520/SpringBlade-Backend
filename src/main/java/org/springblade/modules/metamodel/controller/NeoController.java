package org.springblade.modules.metamodel.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.modules.metamodel.entity.NeoNode;
import org.springblade.modules.metamodel.service.NeoService;
import org.springblade.modules.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
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
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增图节点", notes = "传入User")
	public R addNode(@RequestBody NeoNode neoNode) {
		String label = neoNode.getLabel();
		List<Map<String, Object>> properties = neoNode.getDynamic();
		return R.status(neoService.addNode(label, properties));
	}

}
