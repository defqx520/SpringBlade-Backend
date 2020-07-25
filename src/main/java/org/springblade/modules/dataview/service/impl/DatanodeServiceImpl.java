/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.dataview.service.impl;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.dataview.entity.Datanode;
import org.springblade.modules.dataview.entity.Fieldset;
import org.springblade.modules.dataview.mapper.DatanodeMapper;
import org.springblade.modules.dataview.service.IDatanodeService;
import org.springblade.modules.dataview.service.IFieldsetService;
import org.springblade.modules.dataview.vo.DatanodeVO;
import org.springblade.modules.dataview.vo.FieldsetVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  服务实现类
 *
 * @author Blade
 * @since 2020-07-08
 */
@Service
@AllArgsConstructor
public class DatanodeServiceImpl extends ServiceImpl<DatanodeMapper, Datanode> implements IDatanodeService {

	private IFieldsetService fieldsetService;

	@Override
	public IPage<DatanodeVO> selectDatanodePage(IPage<DatanodeVO> page, DatanodeVO datanode) {
		return page.setRecords(baseMapper.selectDatanodePage(page, datanode));
	}

	@Override
	public boolean saveFieldSets(DatanodeVO datanodeVO, Datanode datanode){
		Long datanodeId = datanode.getId();
		int removeResult = fieldsetService.removeByParentID(datanodeId);

		//首先删除已有的子表数据
		if(removeResult >= 0){
			List<FieldsetVO> fieldsets = datanodeVO.getDynamic();
			List<Fieldset> fieldsets_t = new ArrayList<>();
			IdentifierGenerator identifierGenerator=new DefaultIdentifierGenerator();
			for(FieldsetVO fd : fieldsets)
			{
				Fieldset fd_t = BeanUtil.copy(fd, Fieldset.class);
				identifierGenerator.nextId(fd_t);
				fd_t.setDatanodeId(datanodeId);
				fieldsets_t.add(fd_t);
			}
			int insertResult = fieldsetService.insertBatch(fieldsets_t);
			if(insertResult < 1 ){
				return false;
			}
		}else{
			return false;
		}
		return true;
	}

}
