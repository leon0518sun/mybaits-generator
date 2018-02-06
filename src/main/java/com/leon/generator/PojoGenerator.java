package com.leon.generator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leon.util.ToolsUtil;


/**
 * 自动生成实体类的方法类
 * @author 孙旭
 * @version 1.0
 *
 */
public class PojoGenerator extends AbstractGenerator {

	public static void createDomain(String pojoPackage,
			String pojoPath, String className) {
		
		
		StringBuffer pojoBuffer = new StringBuffer();
		pojoBuffer.append("package" + BLANK_SPACE + pojoPackage + ";" + LINE_SEPARATOR);
		pojoBuffer.append(LINE_SEPARATOR);
		try {
			
			List<Object> strList = new ArrayList<Object>();
			int tableLength = resultSetMetaData.getColumnCount();
			for(int i = 1; i <= tableLength;i++){
				String rolumnClass = resultSetMetaData.getColumnClassName(i);
				strList.add(rolumnClass);
			}
			strList = ToolsUtil.removeDuplicates(strList);
			for (Object object : strList) {
				if(object.toString().equals("java.sql.Date") || object.toString().equals("java.sql.Timestamp"))
					object = "java.util.Date";
				pojoBuffer.append("import " + object.toString() + ";" + LINE_SEPARATOR);
			}
			pojoBuffer.append("import" + BLANK_SPACE + "org.springframework.*;");			
			pojoBuffer.append(LINE_SEPARATOR);
			pojoBuffer.append(classHeader("public", className, false, false,null,null));
			for(int i = 1; i <= tableLength;i++){
				String rolumnClass = resultSetMetaData.getColumnClassName(i);
				String rolumnDBName = resultSetMetaData.getColumnName(i);
				pojoBuffer.append(TAB_SEPARATOR);
				if(rolumnClass.substring(rolumnClass.lastIndexOf(".")+1).equals("Timestamp")){
					pojoBuffer.append("private" + BLANK_SPACE).append("Date" + BLANK_SPACE);
				}else{
					pojoBuffer.append("private" + BLANK_SPACE).append(ToolsUtil.upperFirstCha(rolumnClass.substring(rolumnClass.lastIndexOf(".")+1)) + BLANK_SPACE);
				}
				pojoBuffer.append(ToolsUtil.tableNameToBeanName(rolumnDBName) + ";" + LINE_SEPARATOR );
			}
			pojoBuffer.append(LINE_SEPARATOR);
			for(int i = 1; i <= tableLength;i++){
				String rolumnClass = resultSetMetaData.getColumnClassName(i);
				String rolumnDBName = resultSetMetaData.getColumnName(i);
				String classType = rolumnClass.substring(rolumnClass.lastIndexOf(".")+1);
				if(classType.equals("Timestamp"))
					classType = "Date";
				pojoBuffer.append(setterMethod(ToolsUtil.upperFirstCha(classType), ToolsUtil.tableNameToBeanName(rolumnDBName)));
				pojoBuffer.append(LINE_SEPARATOR);
				pojoBuffer.append(getterMethod(ToolsUtil.upperFirstCha(classType), ToolsUtil.tableNameToBeanName(rolumnDBName)));
				pojoBuffer.append(LINE_SEPARATOR);
			}
			pojoBuffer.append(classFooter());
			writeJavaFile(pojoPath, pojoBuffer.toString(), className);
			createVo(pojoPath,pojoPackage,className);
		} catch (SQLException e) {
			System.out.println("创建JavaBean时,数据库连接失败!");
			e.printStackTrace();
		}
		
		
	}

	private static void createVo(String pojoPath, String pojoPackage, String className) {
		String voPojoPackage = pojoPackage.substring(0,pojoPackage.lastIndexOf(".")) + ".vo";
		StringBuffer voBuffer = new StringBuffer();
		voBuffer.append("package" + BLANK_SPACE + voPojoPackage + ";").append(LINE_SEPARATOR);
		voBuffer.append(LINE_SEPARATOR);
		voBuffer.append("import " + pojoPackage + "." + ToolsUtil.upperFirstCha(className) + ";").append(LINE_SEPARATOR);
		voBuffer.append(classHeader(PUBLIC, className + "Vo", false, false, ToolsUtil.upperFirstCha(className), null));
		voBuffer.append(classFooter());
		writeJavaFile(pojoPath, voBuffer.toString(), className + "Vo");
	}
	



}
