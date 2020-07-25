package org.springblade.modules.dataview.service;

/**
 * @author Tonny
 **/
public interface IDefineTableStructureService {
	/**
	 *
	 * 修改表名
	 *
	 * @param sourceName
	 * @param targetName
	 * @return
	 */
	int alterTableName(String sourceName, String targetName);

	/**
	 * 根据传入的表名，创建新的表并且将原表的数据插入到新表中
	 *
	 * @param sourceName
	 * @param targetName
	 */
	boolean backupTable(String sourceName, String targetName);


	/**
	 *
	 * 动态创建表结构
	 *
	 * @param tableName
	 * @param sql
	 * @return
	 */
	boolean createTable(String tableName, String sql);

	/**
	 * 统计某张表中的总数据条数。
	 *
	 * @param tableName
	 * @return 指定表中的总记录条数。
	 */
	int getRecordCount(String tableName);

	/**
	 * 从指定数据库中，查询是否存在某张表
	 *
	 * @param tableName
	 * @return
	 */
	boolean isTableExistInDB(String tableName);

	/**
	 * truncate指定数据库表的数据
	 *
	 * @param tableName
	 * @return
	 */
	int truncateTable(String tableName);

	/**
	 *
	 * 执行自定义SQL语句
	 *
	 * @param sql
	 * @return
	 */
	int executeSQL(String sql);

}
