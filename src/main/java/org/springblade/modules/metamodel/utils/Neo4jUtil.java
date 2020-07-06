package org.springblade.modules.metamodel.utils;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 通用的neo4j调用类
 *
 * @version 1.0 18-6-5 上午11:21
 */
@Component
public class Neo4jUtil {
    private static Driver driver;

    @Autowired
    public Neo4jUtil(Driver driver) {
        Neo4jUtil.driver = driver;
    }


    /**
     * 执行添加cql
     * @param cql 查询语句
     */
    public static void add(String cql) {
        //启动事务
		Transaction tx = null;
        try {
        	Session session = driver.session();
			tx  = session.beginTransaction();
            tx.run(cql);
            //提交事务
            tx.commit();
        } catch (Exception e) {
        	if(tx!=null){
        		tx.rollback();
			}
            e.printStackTrace();
        } finally {
			if(tx!=null){
				tx.close();
			}
		}
    }
}
