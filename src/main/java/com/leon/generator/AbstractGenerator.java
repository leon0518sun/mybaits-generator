package com.leon.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.leon.util.ToolsUtil;

/**
 * 自动成生类的抽象父类
 * @author 孙旭
 * @version 1.0
 *
 */
public abstract class AbstractGenerator {
	public static final String LINE_SEPARATOR = "\r\n";
	public static final String TAB_SEPARATOR = "\t";
	public static final String BLANK_SPACE = " ";
	public static final String EXTENDS_DAO = " ";
	public static final String PUBLIC = "public";
	public static final String PROPERTED = "properted";
	public static final String PRIVATE = "private";
	public static final String RETURN = "return";
	public static PreparedStatement ps;
	public static ResultSet rs;
	public static ResultSetMetaData resultSetMetaData;


	/**
	 * 生成类的头
	 * @param modifier 修饰词(public,protected,default,private)
	 * @param className 类名
	 * @param ifStatic 是否是静态类 (true,false)
	 * @param ifAbstract 是否是抽象类 (true,false)
	 * @return 拼接完的字符串
	 */
	protected static String classHeader(String modifier,
			String className,
			boolean ifStatic,
			boolean ifAbstract,
			String extendsName,
			String interfaceName){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(LINE_SEPARATOR);
		stringBuffer.append(modifier);
		if(ifAbstract){
		    stringBuffer.append(BLANK_SPACE + "abstract");
		}
		if(ifStatic){
		    stringBuffer.append(BLANK_SPACE + "static");
		}
		stringBuffer.append(BLANK_SPACE + "class");
		stringBuffer.append(BLANK_SPACE + className);
		if(!(extendsName == null)){
		    stringBuffer.append(BLANK_SPACE + "extends" + BLANK_SPACE + extendsName);
		}
		if(!(interfaceName == null)){
		    stringBuffer.append(BLANK_SPACE + "implements" + BLANK_SPACE + interfaceName);
		}
		stringBuffer.append(BLANK_SPACE + "{").append(LINE_SEPARATOR);
		return stringBuffer.toString();
	}
	
	/**
	 * 生成类的尾部
	 * @return
	 */
	protected static String classFooter(){
		return LINE_SEPARATOR + "}";
	}
	/**
	 * 生成属性的Get方法
	 * @param propertyType 返回值对象
	 * @param propertyName 属性名
	 * @return
	 */
	protected static String getterMethod(String propertyType,String propertyName){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(TAB_SEPARATOR);
		stringBuffer.append(PUBLIC);
		stringBuffer.append(BLANK_SPACE + propertyType);
		stringBuffer.append(BLANK_SPACE + "get" + ToolsUtil.upperFirstCha(propertyName) + "(){");
		stringBuffer.append(LINE_SEPARATOR);
		stringBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		stringBuffer.append(RETURN + BLANK_SPACE + "this." + propertyName + " == null ? null : "+ propertyName + ";");
		stringBuffer.append(LINE_SEPARATOR);
		stringBuffer.append(TAB_SEPARATOR);
		stringBuffer.append("}").append(LINE_SEPARATOR);
		return stringBuffer.toString();
	}
	/**
	 * 生成属性的Set方法
	 * @param propertyName 属性名
	 * @return
	 */
	protected static String setterMethod(String propertyType,String propertyName){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(TAB_SEPARATOR);
		stringBuffer.append(PUBLIC);
		stringBuffer.append(BLANK_SPACE + "void");
		stringBuffer.append(BLANK_SPACE + "set" + ToolsUtil.upperFirstCha(propertyName) + "(" + propertyType + BLANK_SPACE + propertyName + "){");
		stringBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		stringBuffer.append("this." + propertyName + " = " + propertyName).append(";");
		stringBuffer.append(LINE_SEPARATOR);
		stringBuffer.append(TAB_SEPARATOR).append("}");
		stringBuffer.append(LINE_SEPARATOR);
		return stringBuffer.toString();
	}
	/**
	 * 生成java文件
	 * @param filePath 文件保存路径
	 * @param string 文件内容
	 * @param className 文件名
	 */
	protected static void writeJavaFile(String filePath,String string,String className){
		File file = new File(filePath);
		if(file.exists() == false){
			file.mkdirs();
		}
		try {
			FileOutputStream fos = new FileOutputStream(filePath + File.separator + className +".java");
			fos.write(string.getBytes());
			fos.close();
			System.out.println("文件生成成功,已经保存为:" + filePath + File.separator + className +".java");
		} catch (FileNotFoundException e) {
			System.out.println("文件路径错误");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("文件输出错误");
			e.printStackTrace();
		}
	}
	/**
	 * 生成Xml文件
	 * @param xmlPath 文件保存路径
	 * @param string 文件内容
	 * @param className 文件名
	 */
	protected static void writeXmlFile(String xmlPath, String string,
			String className) {
		File file = new File(xmlPath);
		if(file.exists() == false){
			file.mkdirs();
		}
		try {
			FileOutputStream fos = new FileOutputStream(xmlPath + File.separator + "mybatis-" + ToolsUtil.lowerFirstCha(className) +".xml");
			fos.write(string.getBytes());
			fos.close();
			System.out.println("文件生成成功,已经保存为:" + xmlPath +  File.separator + "mybatis-" + ToolsUtil.lowerFirstCha(className) +".xml");
		} catch (FileNotFoundException e) {
			System.out.println("文件路径错误");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("文件输出错误");
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取表信息的方法 
	 * @param conn 连接参数
	 * @param tableName 表名
	 */
	public static void connectDB(Connection conn, String tableName) {
		try {
			ps = conn.prepareStatement("SELECT * FROM "+ tableName );
			rs = ps.executeQuery();
			resultSetMetaData = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
