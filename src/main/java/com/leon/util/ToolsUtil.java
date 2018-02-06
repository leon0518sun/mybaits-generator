package com.leon.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToolsUtil {
	/**
	 * 去除List中重复值的方法
	 * @param list 欲去除重复值的List
	 * @return 去重完毕的List
	 */
	public static List<Object> removeDuplicates(List<Object> list){
		List<Object> tempList = new ArrayList<Object>();
		Set<Object> set = new HashSet<Object>();
		if(list.size() < 0){
			System.out.println("list为空!");
			return null;
		}
		for(int i = 0; i < list.size(); i++){
			set.add(list.get(i));
		}
		for (Object object : set) {
			tempList.add(object);
		}
		return tempList;
	}
	/**
	 * 将传入字符串的首字母小写
	 * @param string
	 * @return
	 */
	public static String lowerFirstCha(String str){
		return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
	}
	/**
	 * 将传入字符串的首字母大写
	 * @param str
	 * @return
	 */
	public static String upperFirstCha(String str){
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}
	
	public static String lowerAllCha(String str){
		return str.toLowerCase();
	}
	public static String upperAllCha(String str){
		return str.toUpperCase();
	}
	/**
	 * 将数据库表的列名生成为JavaBean命名规范
	 * 例如:user_id ---> userId
	 * @param tableName 列名
	 * @return JavaBean命名规范名
	 */
	public static String tableNameToBeanName(String tableName) {
		tableName = tableName.toLowerCase();//先将列名的所有字母变成小写,主要为了解释oracle数据库
		StringBuffer sbuffer = new StringBuffer(tableName);
		String changeStr = "";
		for (int i = 0; i < sbuffer.length(); i++) {
			char ch = sbuffer.charAt(i);
			String strTemp = String.valueOf(ch);
			if (strTemp.equals("_")) {
				if(i + 1 >= sbuffer.length()){
					continue;
				}
				changeStr += String.valueOf(sbuffer.charAt(i + 1))
						.toUpperCase();
				i++;
			} else {
				changeStr += strTemp;
			}
		}
		return changeStr;
	}
}
