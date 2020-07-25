package org.springblade.modules.dataview.service.impl;

import lombok.AllArgsConstructor;
import org.springblade.modules.dataview.mapper.DefineTableStructureMapper;
import org.springblade.modules.dataview.service.IDefineTableStructureService;
import org.springframework.stereotype.Service;

/**
 * @author Tonny
 * @creatime 2020-07-23-15:57
 **/

@Service
@AllArgsConstructor
public class DefineTableStructureServiceImpl implements IDefineTableStructureService{

	private DefineTableStructureMapper defineTableStructureMapper;

	/**
	 * 修改表名
	 * @param sourceName
	 * @param targetName
	 * @return
	 */
	public int alterTableName(String sourceName, String targetName){
		return defineTableStructureMapper.alterTableName(sourceName, targetName);
	}

	/**
	 *
	 * 备份表及数据
	 *
	 * @param sourceName
	 * @param targetName
	 * @return
	 */
	public boolean backupTable(String sourceName, String targetName){
		if(isTableExistInDB(targetName)){
			return false;
		}
		defineTableStructureMapper.createNewTableAndInsertData(sourceName, targetName);
		return true;
	}

	/**
	 *
	 * 动态创建表结构
	 *
	 * @param tableName
	 * @param sql
	 * @return
	 */
	public boolean createTable(String tableName, String sql){
		//表存在创建失败
		if(isTableExistInDB(tableName))
		{
			return false;
		}
		//SQL语句执行失败
		if(executeSQL(sql) == 0){
			return true;
		}
		return false;
	}

	/**
	 * 统计某张表中的总数据条数。
	 *
	 * @param tableName
	 * @return 指定表中的总记录条数。
	 */
	public int getRecordCount(String tableName){
		return defineTableStructureMapper.getRecordCount(tableName);
	}

	/**
	 * 从指定数据库中，查询是否存在某张表
	 *
	 * @param tableName
	 * @return
	 */
	public boolean isTableExistInDB(String tableName){
		String databaseName = defineTableStructureMapper.getCurDataBaseName();
		if(defineTableStructureMapper.isTargetTableExistInDB(databaseName, tableName) != null){
			return true;
		}
		return false;
	}

	/**
	 * truncate指定数据库表的数据
	 *
	 * @param tableName
	 * @return
	 */
	public int truncateTable(String tableName){
		return defineTableStructureMapper.truncateTable(tableName);
	}

	/**
	 * 执行自定义SQL语句
	 * @param sql
	 * @return
	 */
	public int executeSQL(String sql){
		return defineTableStructureMapper.executeSQL(sql);
	}
}
