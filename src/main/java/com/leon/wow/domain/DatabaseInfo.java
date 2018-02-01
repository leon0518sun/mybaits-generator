package com.leon.wow.domain;

/**
 * 数据库连接实体类
 * 
 * @author leon
 * 
 */
public class DatabaseInfo {
	private String driverClass;// 连接驱动包
	private String connectionURL;// 连接地址
	private String userId;// 用户名
	private String password;// 密码

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
