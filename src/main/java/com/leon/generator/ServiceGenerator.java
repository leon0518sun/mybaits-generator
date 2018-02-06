package com.leon.generator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leon.util.ToolsUtil;

/**
 * 创建Service层的自动生成类
 * @author 孙旭
 * @version 1.0
 *
 */
public class ServiceGenerator extends AbstractGenerator {
	/**
	 * 创建service接口
	 * @param daoPackage
	 * @param daoPath
	 * @param pojoPackage
	 */
	public static void createSerivce(String servicePackage, String servicePath,
			String pojoPackage, String className) {
//		String voPojoPackage = pojoPackage.substring(0,pojoPackage.lastIndexOf(".")) + ".vo";
		StringBuffer serviceBuffer = new StringBuffer();
		serviceBuffer.append("package" + BLANK_SPACE + servicePackage).append(";").append(LINE_SEPARATOR);
		serviceBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		serviceBuffer.append("import " + pojoPackage + "." + ToolsUtil.upperFirstCha(className)).append(";").append(LINE_SEPARATOR);
//		serviceBuffer.append("import " + voPojoPackage + "." + ToolsUtil.upperFirstCha(className) + "Vo").append(";").append(LINE_SEPARATOR);
		serviceBuffer.append("import java.util.*;").append(LINE_SEPARATOR);
		serviceBuffer.append(LINE_SEPARATOR);
		serviceBuffer.append("public interface " + ToolsUtil.upperFirstCha(className) + "Service {");
		serviceBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		serviceBuffer.append(TAB_SEPARATOR);
		serviceBuffer.append("public void add" + ToolsUtil.upperFirstCha(className) 
				+ "(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		serviceBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		serviceBuffer.append(TAB_SEPARATOR);
		serviceBuffer.append("public void update" + ToolsUtil.upperFirstCha(className)
				+ "(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className)+ ") throws Exception;");
		serviceBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		serviceBuffer.append(TAB_SEPARATOR);
		serviceBuffer.append("public void delete" + ToolsUtil.upperFirstCha(className)
				+ "ById(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className)+ ") throws Exception;");
		serviceBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		serviceBuffer.append(TAB_SEPARATOR);
		serviceBuffer.append("public " + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + "find" + ToolsUtil.upperFirstCha(className) 
				+ "ById(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		serviceBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		serviceBuffer.append(TAB_SEPARATOR);
		serviceBuffer.append("public List<" + ToolsUtil.upperFirstCha(className) + "> findAll" + ToolsUtil.upperFirstCha(className) 
				+ "ForPage(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		serviceBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		serviceBuffer.append("public Long findAll" + ToolsUtil.upperFirstCha(className) 
				+ "Count(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		serviceBuffer.append(LINE_SEPARATOR);
		
		serviceBuffer.append("}");
		writeJavaFile(servicePath, serviceBuffer.toString(), className + "Service");
	}
	
	/**
	 * 创建serviceImpl实现类
	 * @param daoPackage
	 * @param daoPath
	 * @param pojoPackage
	 */
	public static void createServiceImpl(String servicePackage, String servicePath,
			String pojoPackage, String daoPackage,String className) {
//		String voPojoPackage = pojoPackage.substring(0,pojoPackage.lastIndexOf(".")) + ".vo";
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("package").append(BLANK_SPACE).append(servicePackage).append(";");
		stringBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		List<Object> strList = new ArrayList<Object>();
		int tableLength;
		try {
			String tablePrimaryKey = resultSetMetaData.getColumnName(1);
			tableLength = resultSetMetaData.getColumnCount();
			for(int i = 1; i <= tableLength;i++){
				String rolumnClass = resultSetMetaData.getColumnClassName(i);
				strList.add(rolumnClass);
			}
			strList = ToolsUtil.removeDuplicates(strList);
			for (Object object : strList) {
				if("java.sql.Date".equals(object.toString()) || "java.sql.Timestamp".equals(object.toString())){
				    continue;
				}
				stringBuffer.append("import " + object.toString() + ";" + LINE_SEPARATOR);
			}
			stringBuffer.append("import java.util.*;");
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append("import" + BLANK_SPACE + pojoPackage + "." + ToolsUtil.upperFirstCha(className)).append(";").append(LINE_SEPARATOR);
//			stringBuffer.append("import" + BLANK_SPACE + voPojoPackage + "." + ToolsUtil.upperFirstCha(className) + "Vo").append(";").append(LINE_SEPARATOR);
			stringBuffer.append("import" + BLANK_SPACE + servicePackage + "." + ToolsUtil.upperFirstCha(className) + "Service;");
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append("import" + BLANK_SPACE + daoPackage + "." + ToolsUtil.upperFirstCha(className) + "Dao;");
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append("import" + BLANK_SPACE + "org.springframework.*;");
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append("@Service(\""+ToolsUtil.lowerFirstCha(className) + "Service\")");
			stringBuffer.append(classHeader(PUBLIC, className + "ServiceImpl", false, false, null, className+"Service"));
			stringBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
			stringBuffer.append("@Autowired");
			stringBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
			stringBuffer.append(PRIVATE + BLANK_SPACE + ToolsUtil.upperFirstCha(className) + "Dao" + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + "dao;");
			stringBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
			stringBuffer.append(setterMethod(ToolsUtil.upperFirstCha(className) + "Dao", ToolsUtil.lowerFirstCha(className) + "dao"));
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append(getterMethod(ToolsUtil.upperFirstCha(className) + "Dao", ToolsUtil.lowerFirstCha(className) + "dao"));
			stringBuffer.append(TAB_SEPARATOR);
			stringBuffer.append(createImplAddMethod(className,tablePrimaryKey));
			stringBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
			stringBuffer.append(createImplUpdateMethod(className));
			stringBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
			stringBuffer.append(createImplDeleteMethod(className));
			stringBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
			stringBuffer.append(createImplFindAllForPageMethod(className));
			stringBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
			stringBuffer.append(createImplFindByIdMethod(className));
			stringBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
			stringBuffer.append(createImplFindCount(className));
			stringBuffer.append(classFooter());
			writeJavaFile(servicePath, stringBuffer.toString(), className + "ServiceImpl");
		} catch (SQLException e) {
			System.out.println("数据库读取错误!");
			e.printStackTrace();
		}

	}
	
	private static String createImplFindCount(String className) {
		StringBuffer countBuffer = new StringBuffer();
		countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("public Long findAll" + ToolsUtil.upperFirstCha(className) 
				+ "Count(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception {");
		countBuffer.append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("return this." + ToolsUtil.lowerFirstCha(className) + "dao.findAll" + ToolsUtil.upperFirstCha(className) 
				+ "Count(" + ToolsUtil.lowerFirstCha(className) + ");");
		countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("}");
		countBuffer.append(LINE_SEPARATOR);
		return countBuffer.toString();
	}

	private static String createImplFindByIdMethod(String className) {
		StringBuffer createBuffer = new StringBuffer();
		createBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		createBuffer.append(PUBLIC + BLANK_SPACE + ToolsUtil.upperFirstCha(className) 
				+ " find" + ToolsUtil.upperFirstCha(className) + "ById(" + ToolsUtil.upperFirstCha(className) 
				+ BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		createBuffer.append(" throws Exception {").append(LINE_SEPARATOR);
		createBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		createBuffer.append("return this." + ToolsUtil.lowerFirstCha(className) + "dao.find" + ToolsUtil.upperFirstCha(className) 
				+ "ById(" + ToolsUtil.lowerFirstCha(className) + ");");
		createBuffer.append(LINE_SEPARATOR);
		createBuffer.append(TAB_SEPARATOR).append("}");
		return createBuffer.toString();
	}
	private static String createImplFindAllForPageMethod(String className) {
		StringBuffer findAllBuffer = new StringBuffer();
		findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append(PUBLIC + BLANK_SPACE + "List<" + ToolsUtil.upperFirstCha(className) + ">"
				+ BLANK_SPACE + "findAll" + ToolsUtil.upperFirstCha(className) + "ForPage(" + ToolsUtil.upperFirstCha(className) 
				+ BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		findAllBuffer.append(" throws Exception {").append(LINE_SEPARATOR);
		findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("return this." + ToolsUtil.lowerFirstCha(className) + "dao.findAll" 
				+ ToolsUtil.upperFirstCha(className) + "ForPage(" + ToolsUtil.lowerFirstCha(className) + ");");
		findAllBuffer.append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append("}");
		return findAllBuffer.toString();
	}
	private static String createImplDeleteMethod(String className) {
		StringBuffer delBuffer = new StringBuffer();
		delBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append(PUBLIC + BLANK_SPACE + "void delete" + ToolsUtil.upperFirstCha(className) 
				+ "ById(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		delBuffer.append(" throws Exception {").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append(ToolsUtil.lowerFirstCha(className) + ".setValidstatus(\"0\");").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("this." + ToolsUtil.lowerFirstCha(className) + "dao.delete" 
				+ ToolsUtil.upperFirstCha(className) +"ById(" + ToolsUtil.lowerFirstCha(className) + ");");
		delBuffer.append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR);
		delBuffer.append("}");
		return delBuffer.toString();
	}
	private static String createImplUpdateMethod(String className) {
		StringBuffer updateBuffer = new StringBuffer();
		updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append(PUBLIC + BLANK_SPACE + "void update" + ToolsUtil.upperFirstCha(className) 
				+ "(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		updateBuffer.append(" throws Exception {");
		updateBuffer.append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("this." + ToolsUtil.lowerFirstCha(className) + "dao.update" 
		+ ToolsUtil.upperFirstCha(className) +"(" + ToolsUtil.lowerFirstCha(className) + ");");
		updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("}");
		return updateBuffer.toString();
	}
	private static String createImplAddMethod(String className, String tablePrimaryKey) {
		StringBuffer addBuffer = new StringBuffer();
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append(PUBLIC + BLANK_SPACE + "void add" + ToolsUtil.upperFirstCha(className)
				+ "(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")" );
		addBuffer.append(" throws Exception {");
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("String uuid = UUID.randomUUID().toString();");
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append(ToolsUtil.lowerFirstCha(className) + ".set" + ToolsUtil.upperFirstCha(ToolsUtil.tableNameToBeanName(tablePrimaryKey)) + "(uuid);");
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append(ToolsUtil.lowerFirstCha(className) + ".setValidstatus(\"1\");");
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		//这里填写一些固定化的信息
		addBuffer.append("this." + ToolsUtil.lowerFirstCha(className) + "dao.add" + ToolsUtil.upperFirstCha(className) 
				+ "(" + ToolsUtil.lowerFirstCha(className) + ");");
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append("}");
		return addBuffer.toString();
	}
}