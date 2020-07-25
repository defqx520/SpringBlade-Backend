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
package org.springblade.modules.dataview.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.dataview.entity.Datanode;
import org.springblade.modules.dataview.entity.Fieldset;
import org.springblade.modules.dataview.service.IFieldsetService;
import org.springblade.modules.dataview.vo.DatanodeVO;
import org.springblade.modules.dataview.vo.FieldsetVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-07-08
 */
public class DatanodeWrapper extends BaseEntityWrapper<Datanode, DatanodeVO>  {

	private static IFieldsetService fieldsetService;

	static {
		fieldsetService = SpringUtil.getBean(IFieldsetService.class);
	}

    public static DatanodeWrapper build() {
        return new DatanodeWrapper();
    }

	@Override
	public DatanodeVO entityVO(Datanode datanode) {
		DatanodeVO datanodeVO = BeanUtil.copy(datanode, DatanodeVO.class);
		Long datanodeId = datanode.getId();
		if(datanodeId != null) {
			List<Fieldset> list = fieldsetService.getByDatanodeID(datanode.getId());
			List<FieldsetVO> filedsets = new ArrayList<>();
			for(Fieldset fd : list){
				FieldsetVO fd_t = FieldsetWrapper.build().entityVO(fd);
				filedsets.add(fd_t);
			}
			datanodeVO.setDynamic(filedsets);
		}
		return datanodeVO;
	}

}
