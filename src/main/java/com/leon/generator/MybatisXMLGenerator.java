package com.leon.generator;

import java.sql.SQLException;

import com.leon.domain.DatabaseInfo;
import com.leon.util.ToolsUtil;

/**
 * 自动生成mybatis类的常用方法,并输出.xml文件
 * @author 孙旭
 * @version 1.0
 *
 */
public class MybatisXMLGenerator extends AbstractGenerator {

	public static void createMapXml(String pojoPackage, String className,
			String xmlPath, String tableName,DatabaseInfo dbPojo, String daoPackage) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(createXmlHeader(pojoPackage,className,tableName,daoPackage));
		stringBuffer.append(createInsert(pojoPackage,className,tableName));
		stringBuffer.append(createUpdate(pojoPackage,className,tableName));
		stringBuffer.append(createDelete(pojoPackage,className,tableName));
		stringBuffer.append(createFindAllForPage(pojoPackage,className,tableName,dbPojo));
		stringBuffer.append(createFindById(pojoPackage,className,tableName));
		stringBuffer.append(createFindAllCount(pojoPackage,className,tableName));
		stringBuffer.append(createXmlFooter());
		writeXmlFile(xmlPath,stringBuffer.toString(),className);
//		System.out.println(stringBuffer.toString());
	}
	
	/**
	 * 创建查询有效记录的方法,用于分页
	 * @param pojoPackage
	 * @param className
	 * @param tableName
	 * @return
	 */
	private static String createFindAllCount(String pojoPackage, String className, String tableName) {
		StringBuffer countBuffer = new StringBuffer();
		String tablePrimaryKey;
		try {
			tablePrimaryKey = resultSetMetaData.getColumnName(1);
			countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			countBuffer.append("<select id=\"findAll" + ToolsUtil.upperFirstCha(className) 
					+ "Count\" parameterType=\"" + pojoPackage + "." + ToolsUtil.upperFirstCha(className) + "\" resultType=\"Long\">");
			countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
//			countBuffer.append("SELECT COUNT(" + tablePrimaryKey + ") FROM " + tableName + " WHERE validStatus='1'");
			countBuffer.append("SELECT COUNT(" + tablePrimaryKey + ") FROM " + tableName + " WHERE 1 = '1'");
			countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			countBuffer.append("</select>");
		} catch (SQLException e) {
			System.out.println("读取数据总数量时,数据库出现异常!");
			e.printStackTrace();
		}
		return countBuffer.toString();
	}
	/**
	 * 根据主键ID查询方法 
	 * @param pojoPackage
	 * @param className
	 * @param tableName
	 * @return
	 */
	private static String createFindById(String pojoPackage, String className, String tableName) {
		StringBuffer findByIdBuffer = new StringBuffer();
		try {
			String voPojoPackage = pojoPackage.substring(0,pojoPackage.lastIndexOf(".")) + ".vo";
			int tableLength = resultSetMetaData.getColumnCount();
			String tablePrimaryKey = resultSetMetaData.getColumnName(1);
			findByIdBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			findByIdBuffer.append("<select id=\"find" + ToolsUtil.upperFirstCha(className) 
					+ "ById\" parameterType=\"" + pojoPackage + "." + ToolsUtil.upperFirstCha(className) + "\"" 
					+ "  resultMap=\"" +ToolsUtil.lowerFirstCha(className) +"ResultMap\">");
			findByIdBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			findByIdBuffer.append("SELECT ").append(LINE_SEPARATOR);
			for(int i = 1;i <= tableLength; i++){
				String rolumnDBName = resultSetMetaData.getColumnName(i);
				findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				if(i < tableLength)
					findByIdBuffer.append(rolumnDBName + ",");
				if(i == tableLength)
					findByIdBuffer.append(rolumnDBName);
				findByIdBuffer.append(LINE_SEPARATOR);
			}
			findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			findByIdBuffer.append("FROM " + tableName).append(LINE_SEPARATOR);
			findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			findByIdBuffer.append("WHERE " + tablePrimaryKey + " = #{" + ToolsUtil.tableNameToBeanName(tablePrimaryKey) + "}").append(LINE_SEPARATOR);
			findByIdBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			findByIdBuffer.append("</select>");
			findByIdBuffer.append(LINE_SEPARATOR);
		} catch (SQLException e) {
			System.out.println("生成查询ById信息方法时,数据库出现错误! ");
			e.printStackTrace();
		}
		return findByIdBuffer.toString();
	}
	/**
	 * 查询所有有效信息方法 
	 * @param pojoPackage
	 * @param className
	 * @param tableName
	 * @return
	 */
	private static String createFindAllForPage(String pojoPackage, String className, String tableName,DatabaseInfo dbPojo) {
		StringBuffer findAllBuffer = new StringBuffer();
		try {
			String voPojoPackage = pojoPackage.substring(0,pojoPackage.lastIndexOf(".")) + ".vo";
			int tableLength = resultSetMetaData.getColumnCount();
			if(dbPojo.getDriverClass().equals("com.mysql.jdbc.Driver")){
				findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("<select id=\"findAll" + ToolsUtil.upperFirstCha(className) 
						+ "ForPage\" parameterType=\"" + pojoPackage + "." + ToolsUtil.upperFirstCha(className) + "\"" 
						+ " resultMap=\"" +ToolsUtil.lowerFirstCha(className) +"ResultMap\">");
				findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("SELECT ").append(LINE_SEPARATOR);
				for(int i = 1;i <= tableLength; i++){
					String rolumnDBName = resultSetMetaData.getColumnName(i);
					findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
					if(i < tableLength)
						findAllBuffer.append(rolumnDBName + ",");
					if(i == tableLength)
						findAllBuffer.append(rolumnDBName);
					findAllBuffer.append(LINE_SEPARATOR);
				}
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("FROM " + tableName).append(LINE_SEPARATOR);
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
//				findAllBuffer.append("WHERE validStatus='1' ").append(LINE_SEPARATOR);
				findAllBuffer.append("WHERE 1 ='1' ").append(LINE_SEPARATOR);//为了拼接后续语句的语句,代替上面的语句,去掉了有效标识信息
				for(int i = 1;i <= tableLength; i++){
					String rolumnDBName = resultSetMetaData.getColumnName(i);
					findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
					findAllBuffer.append("<if test=\"" + ToolsUtil.tableNameToBeanName(rolumnDBName) + " != null and " + ToolsUtil.tableNameToBeanName(rolumnDBName) + " != \'\'\">").append(LINE_SEPARATOR);
					findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
					if(rolumnDBName.endsWith("Name") 
							|| rolumnDBName.endsWith("name")
							|| rolumnDBName.endsWith("NAME")){
						findAllBuffer.append(" AND " + rolumnDBName + " like " + "\'%${" + ToolsUtil.tableNameToBeanName(rolumnDBName) + "}%\'");
					}else{
						findAllBuffer.append(" AND " + rolumnDBName + " = " + "#{" + ToolsUtil.tableNameToBeanName(rolumnDBName) + "}");
						
					}
					findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
					findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append("</if>");
					findAllBuffer.append(LINE_SEPARATOR);
				}
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append(" LIMIT " + "#{recordStart},#{recordEnd}");
				findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("</select>");
				findAllBuffer.append(LINE_SEPARATOR);
			}else if(dbPojo.getDriverClass().equals("oracle.jdbc.driver.OracleDriver")){
				findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("<select id=\"findAll" + ToolsUtil.upperFirstCha(className) 
						+ "ForPage\" parameterType=\"" + pojoPackage + "." + ToolsUtil.upperFirstCha(className) + "\"" 
						+ "  resultMap=\"" +ToolsUtil.lowerFirstCha(className) +"ResultMap\"\">");
				findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("SELECT ").append(LINE_SEPARATOR);
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append(" ROWNUM ,").append(LINE_SEPARATOR);
				for(int i = 1;i <= tableLength; i++){
					String rolumnDBName = resultSetMetaData.getColumnName(i);
					findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
					if(i < tableLength)
						findAllBuffer.append("A." + rolumnDBName + ",");
					if(i == tableLength)
						findAllBuffer.append("A." + rolumnDBName);
					findAllBuffer.append(LINE_SEPARATOR);
				}
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("FROM " + "(SELECT t.*,ROWNUM ROW_ FROM " + tableName + " t WHERE t.validstatus = '1') A").append(LINE_SEPARATOR);
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
//				findAllBuffer.append("WHERE A.validStatus='1' ").append(LINE_SEPARATOR);
				findAllBuffer.append("WHERE 1 ='1' ").append(LINE_SEPARATOR);//代替上方的语句,去掉了有效标识
				for(int i = 1;i <= tableLength; i++){
					String rolumnDBName = resultSetMetaData.getColumnName(i);
					findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
					findAllBuffer.append("<if test=\"" + ToolsUtil.tableNameToBeanName(rolumnDBName) + " != null and " + ToolsUtil.tableNameToBeanName(rolumnDBName) + " != \'\'\">").append(LINE_SEPARATOR);
					findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
					if(rolumnDBName.endsWith("Name") 
							|| rolumnDBName.endsWith("name")
							|| rolumnDBName.endsWith("NAME")){
						findAllBuffer.append(" AND A." + rolumnDBName + " like " + "\'%${" + ToolsUtil.tableNameToBeanName(rolumnDBName) + "}%\'");
					}else{
						findAllBuffer.append(" AND A." + rolumnDBName + " = " + "#{" + ToolsUtil.tableNameToBeanName(rolumnDBName) + "}");
						
					}
					findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
					findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append("</if>");
					findAllBuffer.append(LINE_SEPARATOR);
				}
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("<![CDATA[").append(LINE_SEPARATOR);
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append(" AND ROW_ > #{recordStart}").append(LINE_SEPARATOR);
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append(" AND ROW_ <= #{recordEnd}").append(LINE_SEPARATOR);
				findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("]]>").append(LINE_SEPARATOR);
				findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				findAllBuffer.append("</select>");
				findAllBuffer.append(LINE_SEPARATOR);
			}
		} catch (SQLException e) {
			System.out.println("生成查询所有信息方法时,数据库出现错误!  ");
			e.printStackTrace();
		}
		return findAllBuffer.toString();
	}
	/**
	 * 删除方法 
	 * @param pojoPackage
	 * @param className
	 * @param tableName
	 * @return
	 */
	private static String createDelete(String pojoPackage, String className, String tableName) {
		StringBuffer delBuffer = new StringBuffer();
		try {
			String tablePrimaryKey = resultSetMetaData.getColumnName(1);
			delBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			delBuffer.append("<delete id=\"delete" + ToolsUtil.upperFirstCha(className)
					+ "ById\" parameterType=\"" + pojoPackage + "." + ToolsUtil.upperFirstCha(className) + "\">");
			delBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			delBuffer.append("DELETE FROM " + tableName + " WHERE " + tablePrimaryKey + "=#{" + ToolsUtil.tableNameToBeanName(tablePrimaryKey) + "}");
			delBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			delBuffer.append("</delete>");
			delBuffer.append(LINE_SEPARATOR);
		} catch (SQLException e) {
			System.out.println("添加删除方法出现数据库错误!");
			e.printStackTrace();
		}
		return delBuffer.toString();
	}
	/**
	 * 修改方法 
	 * @param pojoPackage
	 * @param className
	 * @param tableName
	 * @return
	 */
	private static String createUpdate(String pojoPackage, String className, String tableName) {
		StringBuffer updateBuffer = new StringBuffer();
		int tableLength;
		try {
			tableLength = resultSetMetaData.getColumnCount();
			String tablePrimaryKey = resultSetMetaData.getColumnName(1);
			updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			updateBuffer.append("<update id=\"update" + ToolsUtil.upperFirstCha(className) 
					+ "\" parameterType=\"" + pojoPackage + "." + ToolsUtil.upperFirstCha(className) + "\">");
			updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			updateBuffer.append("UPDATE " + tableName + " SET ");
			updateBuffer.append(LINE_SEPARATOR);
			for(int i = 1; i <= tableLength;i++){
				String rolumnDBName = resultSetMetaData.getColumnName(i);
				updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				if(i < tableLength)
					updateBuffer.append(rolumnDBName + "=#{" + ToolsUtil.tableNameToBeanName(rolumnDBName) + "},");
				if(i == tableLength)
					updateBuffer.append(rolumnDBName + "=#{" + ToolsUtil.tableNameToBeanName(rolumnDBName) + "}");
				updateBuffer.append(LINE_SEPARATOR);
			}
			updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			updateBuffer.append("WHERE ");
			updateBuffer.append(tablePrimaryKey + "=" + "#{" + ToolsUtil.tableNameToBeanName(tablePrimaryKey) + "}");
			updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append("</update>");
			updateBuffer.append(LINE_SEPARATOR);
		} catch (SQLException e) {
			System.out.println("生成修改数据库语句时出现错误!");
			e.printStackTrace();
		}
		return updateBuffer.toString();
	}
	/**
	 * 添加数据方法 
	 * @param pojoPackage
	 * @param className
	 * @param tableName
	 * @return
	 */
	private static String createInsert(String pojoPackage, String className, String tableName) {
		StringBuffer insertBuffer = new StringBuffer();
		try {
			int tableLength = resultSetMetaData.getColumnCount();
			
			insertBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			insertBuffer.append("<insert id=\"add" + ToolsUtil.upperFirstCha(className) 
					+ "\" parameterType=\"" + pojoPackage + "." + ToolsUtil.upperFirstCha(className) +  "\">");
			insertBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
			insertBuffer.append("INSERT INTO " + tableName + "(");
			for(int i = 1; i <= tableLength;i++){
				String rolumnDBName = resultSetMetaData.getColumnName(i);
				if(i < tableLength){
					insertBuffer.append(rolumnDBName + ",");
				}
				if(i == tableLength){
					insertBuffer.append(rolumnDBName + ")");
				}
			}
			insertBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append("VALUES (");
			for(int i = 1; i <= tableLength;i++){
				String rolumnDBName = resultSetMetaData.getColumnName(i);
				if(i < tableLength){
					insertBuffer.append("#{" + ToolsUtil.tableNameToBeanName(rolumnDBName) + "}" + ",");
				}
				if(i == tableLength){
					insertBuffer.append("#{" + ToolsUtil.tableNameToBeanName(rolumnDBName) + "}");
				}
			}
			insertBuffer.append(")");
			insertBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append("</insert>");
			insertBuffer.append(LINE_SEPARATOR);
		} catch (SQLException e) {
			System.out.println("添加数据时,数据库读取错误!");
			e.printStackTrace();
		}
		return insertBuffer.toString();
	}
	/**
	 * 创建Xml文件的尾部
	 * @return
	 */
	private static String createXmlFooter() {
		StringBuffer xmlFooterBuffer = new StringBuffer();
		xmlFooterBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append("</mapper>");
		return xmlFooterBuffer.toString();
	}
	/**
	 * 创建xml文件的头部
	 * @param className
	 * @return
	 */
	private static String createXmlHeader(String pojoPackage,String className,String tableName,String daoPackage) {
		StringBuffer xmlHeaderBuffer  = new StringBuffer();
//		String voPojoPackage = pojoPackage.substring(0,pojoPackage.lastIndexOf(".")) + ".vo";
		xmlHeaderBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(LINE_SEPARATOR);
		xmlHeaderBuffer.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">").append(LINE_SEPARATOR);
		xmlHeaderBuffer.append(LINE_SEPARATOR);
		xmlHeaderBuffer.append(TAB_SEPARATOR);
		xmlHeaderBuffer.append("<mapper namespace=\"" +   daoPackage + "." +ToolsUtil.lowerFirstCha(className) + "dao\">");
//		xmlHeaderBuffer.append("<mapper namespace=\"" + ToolsUtil.upperFirstCha(className) + "Map\">");
		xmlHeaderBuffer.append(LINE_SEPARATOR);
		xmlHeaderBuffer.append(LINE_SEPARATOR);
		xmlHeaderBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		xmlHeaderBuffer.append("<resultMap type=\""+ pojoPackage + "." + className + "\" id=\""+ ToolsUtil.lowerFirstCha(className) +"ResultMap\">");
		xmlHeaderBuffer.append(LINE_SEPARATOR);
		xmlHeaderBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		try {
			int tableLength = resultSetMetaData.getColumnCount();
			String primaryKey = resultSetMetaData.getColumnName(1);
			xmlHeaderBuffer.append("<id property=\""  + ToolsUtil.tableNameToBeanName(primaryKey) +  "\" column=\"" + primaryKey + "\"/>");
			for(int i = 2; i <= tableLength;i++){
				String rolumnDBName = resultSetMetaData.getColumnName(i);
				xmlHeaderBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
				xmlHeaderBuffer.append("<result property=\"" + ToolsUtil.tableNameToBeanName(rolumnDBName) + "\" column=\"" + rolumnDBName + "\"/>");
			}
		} catch (SQLException e) {
			System.out.println("生成XML文件头时读取数据库信息错误!");
			e.printStackTrace();
		}
		xmlHeaderBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		xmlHeaderBuffer.append("</resultMap>");
		xmlHeaderBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		return xmlHeaderBuffer.toString();
	}

}
