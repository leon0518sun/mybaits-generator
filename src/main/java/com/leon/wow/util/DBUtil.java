package com.leon.wow.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.leon.wow.domain.DatabaseInfo;
/**
 * jdbc工具类，提供获得连接、关闭连接的方法
 * @author leon
 *
 */
public class DBUtil {
	public static Connection getConnection(DatabaseInfo dbPojo) throws Exception{
		Connection conn = null;
		try {
			Class.forName(dbPojo.getDriverClass());
			conn = DriverManager.getConnection(
					dbPojo.getConnectionURL(),dbPojo.getUserId(),dbPojo.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return conn;
	}
	
	public static void close(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
