package com.ruixin.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ruixin.config.AnnotationConfig;
import com.ruixin.simpleDB.DBManager;
import com.ruixin.util.AnnoUtil;
import com.ruixin.util.LoggerUtils;
import com.ruixin.util.PackageScan;
import com.ruixin.util.PropertiesUtil;
import com.ruixin.util.StringUtil;
@WebListener
public class StartLoader implements ServletContextListener {

	public void start(){
		//启动tomcat
		//开始扫描Bean
		LoggerUtils.debug("开始扫描Bean...");
		String beanPackage="com.ruixin";
		if(PropertiesUtil.getProperties().contains("bean.package")){
			beanPackage=PropertiesUtil.getProperty("bean.package");
		}
		if(StringUtil.isNotBlank(beanPackage)){
			String[] packages=beanPackage.split(",");
			for(String str:packages){
				PackageScan.instance.packageScan(str);
			}
		}
		//开始解析annotation
		/*解析所有class里面的bean*/
		LoggerUtils.debug("解析所有class里面的bean...");
		AnnoUtil.instance.parseClassAnn();
		
		/*解析bean里面的注解*/
		LoggerUtils.debug("解析bean里面的注解...");
		AnnotationConfig.instance.parseAnnotation();
		/*开始加载数据库链接池*/
		DBManager.getManager().register();
	}

	public void end(){
	}
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		start();
	}
		

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
