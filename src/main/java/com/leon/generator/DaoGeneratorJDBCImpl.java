package com.leon.generator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leon.util.ToolsUtil;

/**
 * 无框架使用JDBC创建Dao层的自动生成类
 * @author 孙旭
 *
 */
public class DaoGeneratorJDBCImpl extends AbstractGenerator implements DaoGenerator {
	public void createDao(String daoPackage, String daoPath, String pojoPackage, String className) {
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
				+ "(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		daoBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		daoBuffer.append("public Long findAll" + ToolsUtil.upperFirstCha(className) 
				+ "Count(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ") throws Exception;");
		daoBuffer.append(LINE_SEPARATOR);
		
		daoBuffer.append("}");
		writeJavaFile(daoPath, daoBuffer.toString(), className + "Dao");
	}
	
	public void createDaoImpl(String daoPackage, String daoPath, String pojoPackage, String className) {
		String voPojoPackage = pojoPackage.substring(0,pojoPackage.lastIndexOf(".")) + ".vo";
		StringBuffer daoImplBuffer = new StringBuffer();
		daoImplBuffer.append("package" + BLANK_SPACE + daoPackage).append(";").append(LINE_SEPARATOR);
		daoImplBuffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
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
//							object = "java.util.Date";
							continue;
						daoImplBuffer.append("import " + object.toString() + ";" + LINE_SEPARATOR);
					}
					daoImplBuffer.append("import java.util.*;");
					daoImplBuffer.append(LINE_SEPARATOR);
					daoImplBuffer.append("import org.mybatis.spring.support.SqlSessionDaoSupport;");
					daoImplBuffer.append(LINE_SEPARATOR);
					daoImplBuffer.append("import " + daoPackage + "." + className + "Dao;");
					daoImplBuffer.append(LINE_SEPARATOR);
					daoImplBuffer.append("import" + BLANK_SPACE + pojoPackage + "." + ToolsUtil.upperFirstCha(className)).append(";").append(LINE_SEPARATOR);
					daoImplBuffer.append("import" + BLANK_SPACE + voPojoPackage + "." + ToolsUtil.upperFirstCha(className) + "Vo").append(";").append(LINE_SEPARATOR);
					//导包结束
					//生成类头
					daoImplBuffer.append(classHeader(PUBLIC, className + "DaoImpl", false, false, null, ToolsUtil.upperFirstCha(className)+"Dao"));
					daoImplBuffer.append(TAB_SEPARATOR);
					daoImplBuffer.append("private SqlConfig sqlconfig;").append(LINE_SEPARATOR);
					daoImplBuffer.append(setterMethod("SqlConfig", "sqlconfig"));
					daoImplBuffer.append(LINE_SEPARATOR);
					daoImplBuffer.append(getterMethod("SqlConfig", "sqlconfig"));
					daoImplBuffer.append(LINE_SEPARATOR);
					//生成默认的方法CURD
					daoImplBuffer.append(TAB_SEPARATOR);
					daoImplBuffer.append(createImplAddMethod(className));
					daoImplBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
					daoImplBuffer.append(createImplUpdateMethod(className));
					daoImplBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
					daoImplBuffer.append(createImplDeleteMethod(className));
					daoImplBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
					daoImplBuffer.append(createImplFindAllForPageMethod(className));
					daoImplBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
					daoImplBuffer.append(createImplFindByIdMethod(className));
					daoImplBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
					daoImplBuffer.append(createImplFindAllCountMethod(className));
					daoImplBuffer.append(classFooter());
					writeJavaFile(daoPath, daoImplBuffer.toString(), className + "DaoImpl");
				}catch(SQLException e){
					System.out.println("生成DaoImpl层时数据库读取错误");
					e.printStackTrace();
				}
	}

	private String createImplFindAllCountMethod(String className) {
		StringBuffer countBuffer = new StringBuffer();
		countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("public Long findAll" + ToolsUtil.upperFirstCha(className) 
				+ "Count(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		countBuffer.append("{");
		countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("SqlSession session = null;").append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("Long count = null;").append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("try{").append(LINE_SEPARATOR);
		countBuffer.append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("session = sqlconfig.getSqlSessionFactory().openSession();").append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("count = session.selectOne(\"" + ToolsUtil.upperFirstCha(className) + "Map.findAll" 
				+ ToolsUtil.upperFirstCha(className) + "Count\"," + ToolsUtil.lowerFirstCha(className) + ");");
		countBuffer.append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
//		countBuffer.append("session.commit();");
//		countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("}catch(Exception e){").append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("e.printStackTrace();").append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("session.rollback();").append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("}finally{").append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("session.close();").append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("}").append(LINE_SEPARATOR);
		countBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		countBuffer.append("return count;");
		countBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append("}");
		return countBuffer.toString();
	}

	private String createImplFindByIdMethod(String className) {
		StringBuffer findByIdBuffer = new StringBuffer();
		findByIdBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("public " + ToolsUtil.upperFirstCha(className) + " find" + ToolsUtil.upperFirstCha(className) 
				+ "ById(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		findByIdBuffer.append("{");
		findByIdBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("SqlSession session = null;").append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("try{").append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("session = sqlconfig.getSqlSessionFactory().openSession();").append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append(ToolsUtil.lowerFirstCha(className) + " = session.selectOne(\"" + ToolsUtil.upperFirstCha(className) + "Map.find" 
				+ ToolsUtil.upperFirstCha(className) + "ById\"," + ToolsUtil.lowerFirstCha(className) + ");");
		findByIdBuffer.append(LINE_SEPARATOR);
//		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
//		findByIdBuffer.append("session.commit();");
		findByIdBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("}catch(Exception e){").append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("e.printStackTrace();").append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("session.rollback();").append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("}finally{").append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("session.close();").append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("}").append(LINE_SEPARATOR);
		findByIdBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findByIdBuffer.append("return " + ToolsUtil.lowerFirstCha(className) + ";");
		findByIdBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append("}");
		return findByIdBuffer.toString();
	}

	private String createImplFindAllForPageMethod(String className) {
		StringBuffer findAllBuffer = new StringBuffer();
		findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("public List<" + ToolsUtil.upperFirstCha(className) + "Vo> findAll" + ToolsUtil.upperFirstCha(className) 
				+ "ForPage(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		findAllBuffer.append("{");
		findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("List<" + ToolsUtil.upperFirstCha(className) + "Vo> " + ToolsUtil.lowerFirstCha(className) + "VoList = null;");
		findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("SqlSession session = null;").append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("try{").append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("session = sqlconfig.getSqlSessionFactory().openSession();").append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append(ToolsUtil.lowerFirstCha(className) + "VoList = session.selectList(\"" + ToolsUtil.upperFirstCha(className) + "Map.findAll" 
				+ ToolsUtil.upperFirstCha(className) + "ForPage\"," + ToolsUtil.lowerFirstCha(className) + ");");
		findAllBuffer.append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
//		findAllBuffer.append("session.commit();");
//		findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("}catch(Exception e){").append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("e.printStackTrace();").append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("session.rollback();").append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("}finally{").append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("session.close();").append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("}").append(LINE_SEPARATOR);
		findAllBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		findAllBuffer.append("return " + ToolsUtil.lowerFirstCha(className) + "VoList;");
		findAllBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append("}");
		return findAllBuffer.toString();
	}

	private String createImplDeleteMethod(String className) {
		StringBuffer delBuffer = new StringBuffer();
		delBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("public void delete" + ToolsUtil.upperFirstCha(className) 
				+ "ById(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		delBuffer.append("{");
		delBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("SqlSession session = null;").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("try{").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("session = sqlconfig.getSqlSessionFactory().openSession();").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("session.delete(\"" + ToolsUtil.upperFirstCha(className) + "Map.delete" 
				+ ToolsUtil.upperFirstCha(className) + "ById\"," + ToolsUtil.lowerFirstCha(className) + ");");
		delBuffer.append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("session.commit();");
		delBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("}catch(Exception e){").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("e.printStackTrace();").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("session.rollback();").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("}finally{").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("session.close();").append(LINE_SEPARATOR);
		delBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		delBuffer.append("}");
		delBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append("}");
		return delBuffer.toString();
	}

	private String createImplUpdateMethod(String className) {
		StringBuffer updateBuffer = new StringBuffer();
		updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("public void update" + ToolsUtil.upperFirstCha(className) 
				+ "(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		updateBuffer.append("{");
		updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("SqlSession session = null;").append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("try{").append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("session = sqlconfig.getSqlSessionFactory().openSession();").append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("session.update(\"" + ToolsUtil.upperFirstCha(className) + "Map.update" 
				+ ToolsUtil.upperFirstCha(className) + "\"," + ToolsUtil.lowerFirstCha(className) + ");");
		updateBuffer.append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("session.commit();");
		updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("}catch(Exception e){").append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("e.printStackTrace();").append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("session.rollback();").append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("}finally{").append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("session.close();").append(LINE_SEPARATOR);
		updateBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		updateBuffer.append("}");
		updateBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append("}");
		return updateBuffer.toString();
	}

	private String createImplAddMethod(String className) {
		StringBuffer addBuffer = new StringBuffer();
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("public void add" + ToolsUtil.upperFirstCha(className) 
				+ "(" + ToolsUtil.upperFirstCha(className) + BLANK_SPACE + ToolsUtil.lowerFirstCha(className) + ")");
		addBuffer.append("{");
//		addBuffer.append("throws Exception {");
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("SqlSession session = null;").append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("try{").append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("session = sqlconfig.getSqlSessionFactory().openSession();").append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("session.insert(\"" + ToolsUtil.upperFirstCha(className) + "Map.add" 
				+ ToolsUtil.upperFirstCha(className) + "\"," + ToolsUtil.lowerFirstCha(className) + ");");
		addBuffer.append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("session.commit();");
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("}catch(Exception e){").append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("e.printStackTrace();").append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("session.rollback();").append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("}finally{").append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("session.close();").append(LINE_SEPARATOR);
		addBuffer.append(TAB_SEPARATOR).append(TAB_SEPARATOR);
		addBuffer.append("}");
		addBuffer.append(LINE_SEPARATOR).append(TAB_SEPARATOR).append("}");
		return addBuffer.toString();
	}
}
