package com.leon;

import com.leon.generator.ElementsXML;

/**
 * 自动生成代码的主类
 * 
 * @author 孙旭
 * 
 */
public class GeneratorMain {

	/**
	 * 自动生成代码的主方法入口
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		String xmlPath = System.getProperty("user.dir") + "/src/main/java/com/leon/ctGeneratorConfig.xml";
		ElementsXML.createFile(xmlPath);
	}

}
