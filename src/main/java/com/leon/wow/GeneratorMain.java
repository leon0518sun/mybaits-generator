package com.leon.wow;

import com.leon.wow.generator.ElementsXML;

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
		String XMLPath = System.getProperty("user.dir") + "/src/main/java/com/leon/wow/ctGeneratorConfig.xml";
		ElementsXML.createFile(XMLPath);
	}

}
