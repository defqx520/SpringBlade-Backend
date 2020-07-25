package org.springblade.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

public interface MyMapper extends BaseMapper<T> {

	List<Map<String, Object>> getTaskInfo(@Param("taskId") String taskId, @Param("tableName") String tableName);

	void createTable(String sql);

	List<Map<String, Object>> fetchFileData(@Param("tableName") String tableName, @Param("attachId") String attachId, @Param("limit") Long limit);

	Long getDataTotal(String tableName, String attachId);

	@Select("select column_name, column_comment from information_schema.columns where table_name='mannual_test'")
	List<Map<String, String>> getTableColumns(@Param("tableName") String tableName);
}
