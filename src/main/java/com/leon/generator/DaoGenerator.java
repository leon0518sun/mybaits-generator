package com.leon.generator;

public interface DaoGenerator {

	void createDaoImpl(String daoPackage, String daoPath, String pojoPackage, String className);

	void createDao(String daoPackage, String daoPath, String pojoPackage, String className);


}
