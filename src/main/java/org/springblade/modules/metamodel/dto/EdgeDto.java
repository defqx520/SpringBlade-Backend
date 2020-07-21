package org.springblade.modules.metamodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class EdgeDto {
	private Integer id;
	private String label;
	private Integer from;
	private Integer to;
}
