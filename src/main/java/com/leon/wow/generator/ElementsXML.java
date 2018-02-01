package com.leon.wow.generator;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.leon.wow.domain.DatabaseInfo;
import com.leon.wow.util.DBUtil;

/**
 * 处理XML配置文件的类
 * @author 孙旭
 * @version 1.0
 *
 */
public class ElementsXML extends AbstractGenerator{
	public static void createFile(String XMLPath) {
		SAXReader reader = new SAXReader();
		Connection conn;//数据库连接源
		try {
			Document document = reader.read(new File(XMLPath));			//读取XML文件取得数据库信息
			Element rootElement = document.getRootElement();
			Element contextElement = rootElement.element("context");
			Element jdbcConnectionElement  = contextElement.element("jdbcConnection");//获取数据库连接池
			String driverClass = jdbcConnectionElement.attributeValue("driverClass");
			String connectionURL = jdbcConnectionElement.attributeValue("connectionURL");
			String userId = jdbcConnectionElement.attributeValue("userId");
			String password = jdbcConnectionElement.attributeValue("password");
			DatabaseInfo dbPojo = new DatabaseInfo();
			dbPojo.setDriverClass(driverClass);
			dbPojo.setConnectionURL(connectionURL);
			dbPojo.setPassword(password);
			dbPojo.setUserId(userId);
			
			Element tableNameElement = contextElement.element("jdbcTableName");//获取数据表信息
			String tableName = tableNameElement.attributeValue("tableName");//获取数据库表名
			String className = tableNameElement.attributeValue("className");//获取类名
			
			Element javaModelGeneratorElement = contextElement.element("javaModelGenerator");//获取POJO类的信息
			String pojoPackage = javaModelGeneratorElement.attributeValue("package");//获取POJO类包名
			String pojoPath = javaModelGeneratorElement.attributeValue("saveFile");//获取POJO类存放路径
			
			Element javaDaoGeneratorElement = contextElement.element("javaDaoGenerator");//获取Dao层的信息
			String daoPackage = javaDaoGeneratorElement.attributeValue("package");//获取Dao层的包名
			String daoPath = javaDaoGeneratorElement.attributeValue("saveFile");//获取Dao层存储的路径
			
			Element javaServiceGeneratorElement = contextElement.element("javaServiceGenerator");//获取Service层信息
			String servicePackage = javaServiceGeneratorElement.attributeValue("package");//获取service层的包名
			String servicePath = javaServiceGeneratorElement.attributeValue("saveFile");//获取service文件存储路径
			String isSpring = javaServiceGeneratorElement.attributeValue("spring");//判断是否是Spring框架
			
			Element xmlGeneratorElement = contextElement.element("sqlMapGenerator");//获取xml配置信息
			String xmlPath = xmlGeneratorElement.attributeValue("saveFile");//获取xml文件存储路径
			
			try {
				conn = DBUtil.getConnection(dbPojo);
				AbstractGenerator.connectDB(conn,tableName);//读取数据库表内容
				PojoGenerator.createDomain(pojoPackage,pojoPath,className);//创建实体类方法
				DaoGenerator daoGenerator = null;
				if(isSpring.equals("true")){
					daoGenerator = new DaoGeneratorSpringImpl();//创建Dao层的实例方法(分为Spring版,与POJO版通过接口的实现类不同来生成)
				}else{
					daoGenerator = new DaoGeneratorJDBCImpl();//创建Dao层的实例方法(分为Spring版,与POJO版通过接口的实现类不同来生成)
				}
				daoGenerator.createDao(daoPackage, daoPath, pojoPackage,className);//创建Dao层Dao接口
				daoGenerator.createDaoImpl(daoPackage,daoPath,pojoPackage,className);//创建Dao层Impl实现类
				ServiceGenerator.createSerivce(servicePackage, servicePath, pojoPackage,className);//创建Service接口
				ServiceGenerator.createServiceImpl(servicePackage, servicePath, pojoPackage,daoPackage,className);//创建ServiceImpl实现类
				MybatisXMLGenerator.createMapXml(pojoPackage,className,xmlPath,tableName,dbPojo);//创建XML文件
				conn.close();
			} catch (SQLException e) {
				System.out.println("数据库连接失败!");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (DocumentException e) {
			System.out.println("文件加载失败,请检查路径");
			e.printStackTrace();
		}
		
	}

}
