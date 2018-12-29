package com.ruixin.util;

import java.io.FileInputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;

import com.ruixin.config.HandleException;

/**
 * 读取properties配置文件
 */
public class PropertiesUtil{

    private static HashMap<String,String> propertyMap = new HashMap<String, String>();
    private static Properties properties=null;
    private static String defaultProperties="ruixin.properties";
    /**
     * 通过文件名读取配置文件
     * @param propertiesName 配置文件名
     * @return
     */
    public static void init(String propertiesName){
        try {
        	properties=new Properties();
            // 加载properties配置文件生成对应的输入流
            URL url=PropertiesUtil.class.getClassLoader().getResource(propertiesName);
            String filePath = URLDecoder.decode(url.getPath(), "utf-8");
            FileInputStream in = new FileInputStream(filePath);
            // 使用properties对象加载输入流
            properties.load(in);
            properties.forEach((key,value)->{
                propertyMap.put(String.valueOf(key),String.valueOf(value));
            });
            in.close();
        } catch (Exception e) {
        	HandleException.getInstance().handler(propertiesName+"配置文件读取错误",e);
        }
    }

    /**
     * 获取properties里面的属性值
     * @param key
     * @return
     */
    public static String getProperty(String key){
    	if(propertyMap.isEmpty()){
    		init(defaultProperties);
    	}
        return propertyMap.get(key);
    }


    public static Properties getProperties(){
        if(properties==null) properties = new Properties();
        return properties;
    }
}