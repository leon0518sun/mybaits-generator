package com.leon.wow.generator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leon.wow.util.ToolsUtil;

/**
 * 基于Spring框架的Dao层自动生成类
 * @author 孙旭
 * @version 1.0
 *
 */
public class DaoGeneratorSpringImpl extends AbstractGenerator implements DaoGenerator {
	
	/**
	 * 创建daoImpl文件的方法 
	 */
	public void createDaoImpl(String daoPackage, String daoPath, String pojoPackage, String className) {
		String voPojoPackage = pojoPackage.substring(0,pojoPackage.lastIndexOf(".")) + ".vo";
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("package").append(BLANK_SPACE).append(daoPackage).append(";");
		stringBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		//导入包
		List<Object> strList = new ArrayList<Object>();//用于接收并处理重复数据
		//获取当前表有多少列
		int tableLength;
		try {
			tableLength = resultSetMetaData.getColumnCount();
			for(int i = 1; i <= tableLength;i++){
				String rolumnClass = resultSetMetaData.getColumnClassName(i);//获取数据库列对应的java类型
				strList.add(rolumnClass);
			}
			strList = ToolsUtil.removeDuplicates(strList);
			for (Object object : strList) {
				if(object.toString().equals("java.sql.Date") || object.toString().equals("java.sql.Timestamp"))//将数据库的时间类型转为java的时间类型
//					object = "java.util.Date";
					continue;
				stringBuffer.append("import " + object.toString() + ";" + LINE_SEPARATOR);
			}
			stringBuffer.append("import java.util.*;");
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append("import org.mybatis.spring.support.SqlSessionDaoSupport;");
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append("import " + daoPackage + "." + className + "Dao;");
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append("import" + BLANK_SPACE + pojoPackage + "." + ToolsUtil.upperFirstCha(className)).append(";").append(LINE_SEPARATOR);
			stringBuffer.append("import" + BLANK_SPACE + voPojoPackage + "." + ToolsUtil.upperFirstCha(className) + "Vo").append(";").append(LINE_SEPARATOR);
			//导包结束
			//生成类头
			stringBuffer.append(classHeader(PUBLIC, className + "DaoImpl", false, false, "SqlSessionDaoSupport", ToolsUtil.upperFirstCha(className)+"Dao"));
			//生成默认的方法CURD
			stringBuffer.append(TAB_SEPARATOR);
			stringBuffer.append(createImplAddMethod(className));
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append(createImplUpdateMethod(className));
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append(createImplDeleteMethod(className));
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append(createImplFindAllForPageMethod(className));
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append(createImplFindByIdMethod(className));
			stringBuffer.append(LINE_SEPARATOR);
			stringBuffer.append(createImplFindAllCountMethod(className));
			stringBuffer.append(classFooter());
			writeJavaFile(daoPath, stringBuffer.toString(), className + "DaoImpl");
		} catch (SQLException e) {
			System.out.println("数据库读取错误");
			e.printStackTrace();
		}
		
		
	}
	

	/**
	 * 创建Dao接口的方法 
	 */
	public void createDao(String daoPackage, String daoPath, String pojoPackage ,String className) {
		String voPojoPackage = pojoPackage.substring(0,pojoPackage.lastIndexOf(".")) + ".vo";
		StringBuffer daoBuffer = new StringBuffer();
		daoBuffer.append("package" + BLANK_SPACE + daoPackage).append(";").append(LINE_SEPARATOR);
		daoBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		daoBuffer.append("import " + pojoPackage + "." + ToolsUtil.upperFirstCha(className)).append(";").append(LINE_SEPARATOR);
		daoBuffer.append("import" + BLANK_SPACE + voPojoPackage + "." + ToolsUtil.upperFirstCha(className) + "Vo").append(";").append(LINE_SEPARATOR);
		daoBuffer.append("import java.util.*;").append(LINE_SEPARATOR);
		daoBuffer.append(LINE_SEPARATOR);
		daoBuffer.append("public interface " + ToolsUtil.upperFirstCha(className) + "Dao {");
		daoBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		daoBuffer.append(TAB_SEPARATOR);
		daoBuffer.append("public void add" + ToolsUtil.upperFirstCha(className) 
				+ "(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		daoBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		daoBuffer.append(TAB_SEPARATOR);
		daoBuffer.append("public void update" + ToolsUtil.upperFirstCha(className)
				+ "(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className)+ ") throws Exception;");
		daoBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		daoBuffer.append(TAB_SEPARATOR);
		daoBuffer.append("public void delete" + ToolsUtil.upperFirstCha(className)
				+ "ById(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className)+ ") throws Exception;");
		daoBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		daoBuffer.append(TAB_SEPARATOR);
		daoBuffer.append("public " + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + "find" + ToolsUtil.upperFirstCha(className) 
				+ "ById(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		daoBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		daoBuffer.append(TAB_SEPARATOR);
		daoBuffer.append("public List<" + ToolsUtil.upperFirstCha(className) + "Vo> findAll" + ToolsUtil.upperFirstCha(className) 
				+ "ForPage(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		daoBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		daoBuffer.append("public Long findAll" + ToolsUtil.upperFirstCha(className) 
				+ "Count(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		daoBuffer.append(LINE_SEPARATOR);
		daoBuffer.append("}");
		writeJavaFile(daoPath, daoBuffer.toString(), className + "Dao");
	}
	
	/**
	 * 创建findById方法
	 * @param className
	 * @return
	 */
	private String createImplFindByIdMethod(String className) {
		StringBuffer findByIdBuffer = new StringBuffer();
		findByIdBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("public " + ToolsUtil.upperFirstCha(className));
		findByIdBuffer.append(BLANK_SPACE);
		findByIdBuffer.append("find"  + ToolsUtil.upperFirstCha(className) + "ById" + 
		"(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		findByIdBuffer.append(" throws Exception {");
		findByIdBuffer.append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("return this.getSqlSession().selectOne(\"" 
				+ ToolsUtil.upperFirstCha(className) + "Map.find" + ToolsUtil.upperFirstCha(className) + "ById\"," + ToolsUtil.lowerFirstCha(className) + ");");
		findByIdBuffer.append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append("}");
		return findByIdBuffer.toString();
	}
	
	/**
	 * 创建查找所有表内容的方法(分页查询) 
	 * @param className
	 * @return
	 */
	private String createImplFindAllForPageMethod(String className) {
		StringBuffer findAllBuffer = new StringBuffer();
		findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("public List<" +ToolsUtil.upperFirstCha(className)+ "Vo>");
		findAllBuffer.append(BLANK_SPACE);
		findAllBuffer.append("findAll" + ToolsUtil.upperFirstCha(className));
		findAllBuffer.append("ForPage(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception {");
		findAllBuffer.append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("return this.getSqlSession().selectList(\"" 
				+ ToolsUtil.upperFirstCha(className) + "Map.findAll" + ToolsUtil.upperFirstCha(className) + "ForPage\"," + ToolsUtil.lowerFirstCha(className) + ");");
		findAllBuffer.append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append("}");
		return findAllBuffer.toString();
	}
	/**
	 * 创建delete方法
	 * @param className
	 * @param primaryKey
	 * @return
	 */
	private String createImplDeleteMethod(String className) {
		StringBuffer deleteBuffer = new StringBuffer();
		deleteBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		deleteBuffer.append("public void delete" + ToolsUtil.upperFirstCha(className));
		deleteBuffer.append("ById(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		deleteBuffer.append(" throws Exception {").append(LINE_SEPARATOR);
		deleteBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		deleteBuffer.append("this.getSqlSession().delete(\"" 
				+ ToolsUtil.upperFirstCha(className) + "Map.delete" + ToolsUtil.upperFirstCha(className) + "ById\"," + ToolsUtil.lowerFirstCha(className) + ");");
		deleteBuffer.append(LINE_SEPARATOR);
		deleteBuffer.append(TAB_SEPARATOR).append("}");
		return deleteBuffer.toString();
	}
	/**
	 * 创建update方法
	 * @param className
	 * @param primaryKey
	 * @return
	 */
	private String createImplUpdateMethod(String className) {
		StringBuffer updateBuffer = new StringBuffer();
		updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("public void update" + ToolsUtil.upperFirstCha(className));
		updateBuffer.append("(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className)+ ")");
		updateBuffer.append(" throws Exception {").append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("this.getSqlSession().update(\"" 
				+ ToolsUtil.upperFirstCha(className) + "Map.update" + ToolsUtil.upperFirstCha(className) + "\"," + ToolsUtil.lowerFirstCha(className) + ");");
		updateBuffer.append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append("}");
		return updateBuffer.toString();
	}
	/**
	 * 创建Add方法
	 * @param className
	 * @return
	 */
	private String createImplAddMethod(String className) {
		StringBuffer addBuffer = new StringBuffer();
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("public void add" + ToolsUtil.upperFirstCha(className) + "(" + ToolsUtil.upperFirstCha(className)
				+ BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		addBuffer.append(" throws Exception {").append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("this.getSqlSession().insert(\"" + ToolsUtil.upperFirstCha(className) + "Map.add" + ToolsUtil.upperFirstCha(className) + "\"," + ToolsUtil.lowerFirstCha(className) + ");");
		addBuffer.append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append("}").append(LINE_SEPARATOR);
		return addBuffer.toString();
	}
	
	/**
	 * 创建查询所有条目数量的方法
	 * @param className
	 * @return
	 */
	private String createImplFindAllCountMethod(String className) {
		StringBuffer countBuffer = new StringBuffer();
		countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("public Long findAll" + ToolsUtil.upperFirstCha(className) 
				+ "Count(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception {");
		countBuffer.append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("return this.getSqlSession().selectOne(\"" 
				+ ToolsUtil.upperFirstCha(className) + "Map.findAll" + ToolsUtil.upperFirstCha(className) + "Count\"," + ToolsUtil.lowerFirstCha(className) + ");");
		countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("}");
		countBuffer.append(LINE_SEPARATOR);
		return countBuffer.toString();
	}
}
