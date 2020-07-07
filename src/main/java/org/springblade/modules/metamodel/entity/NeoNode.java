package org.springblade.modules.metamodel.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class NeoNode {
    private String label;
    private List<Map<String, Object>> dynamic;

}
