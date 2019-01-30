package com.ruixin.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:ruixin
 * @Date: 2018年11月13日 上午11:21:54
 * @Description:Url工具类
 */
public class UrlUtil {

	public static int EQ=0;
	
	/**
	 * @param pre Web注解上的前缀
	 * @param url mapping上的路径
	 * @return
	 * @Description: 处理mapping路径  格式  /a
	 */
	public static String parseUrl(String pre,String url){
		url=url.trim();//清除空格
		pre=pre.trim();
		if(StringUtil.isBlank(pre)){
			return formatUrl(url);
		}else{
			pre=formatUrl(pre);
			url=formatUrl(url);
			if(pre.equals("/")){
				return url;
			}else if(url.equals("/")){
				return pre;
			}else{
				return pre+url;
			}
		}
	}
	
	/**
	 * @Description:格式化url
	 */
	public static String formatUrl(String url){
		url=url.trim();
		if(url.startsWith("/")&&!url.endsWith("/")){  // 处理标准格式/a
			return url;
		}else if(url.startsWith("/")&&url.endsWith("/")){  // 处理格式/a/
			return url.substring(0, url.length()-1);
		}else if(!url.startsWith("/")&&url.endsWith("/")){  // 处理格式 a/
			return "/"+url.substring(0,url.length()-1);
		}else{  // 处理格式  a
			return "/"+url;
		}
	}
	
	/**
	 * @param uri
	 * @param url
	 * @return
	 * @Description:如果mapping中匹配带*符号
	 */
	public static boolean patternUrl(String uri,String url){
		if(url.endsWith("*")){
			if(url.equals("/*")){ // /*
				url="/";
			}else{
				url=url.substring(0, url.length()-1);
			}
			return uri.startsWith(url);
		}
		return false;
	}

	/**
	 * @param uri
	 * @param url
	 * @return
	 * @Description:restful模糊匹配字段获取
	 */
	public static Map<String,String> getParameter(String uri, String url) {
		Map<String,String> map=new HashMap<>();
		uri=formatUrl(uri);
		if(url.endsWith("*")){
			url=url.substring(0, url.length()-2);
		}
		url=formatUrl(url);
		String[] uris=uri.split("/");
		String[] urls=url.split("/");
		for(int i=0;i<urls.length;i++){
			String urlIndex=urls[i].trim();
			if(urlIndex.startsWith("$")){
				map.put(urlIndex.substring(1),uris[i]);
			}
		}
		return map;
	}

	/**
	 * @param uri
	 * @param url
	 * @return
	 * @Description:restful模糊匹配   0:匹配失败
	 */
	public static int patternRestful(String uri, String url) {
		boolean isPattern=false;
		int patternIndex=0;
		uri=formatUrl(uri);
		if(url.endsWith("*")){
			isPattern=true;
		}
		String[] uris=uri.split("/");
		String[] urls=url.split("/");
		if(!isPattern&&uris.length!=urls.length){
			return patternIndex;
		}
		boolean flag=true;
		for(int i=0;i<urls.length;i++){
			String urlIndex=urls[i].trim();
			String uriIndex=urls[i].trim();
			if(!urlIndex.equals(uriIndex)&&urlIndex.startsWith("$")){
				flag=false;
				patternIndex=0;
				break;
			}else{
				patternIndex+=uriIndex.length();
			}
		}
		if(!flag){
			return patternIndex;
		}
		return patternIndex;
	}
	
	/**
	 * @param uri
	 * @param url
	 * @return
	 * @Description:Url映射规则  精准匹配、前缀匹配、* 匹配、restful匹配    
	 * 返回值表示匹配系数
	 * EQ: 100%匹配
	 * -1: 不匹配
	 */
	public static int urlPattern(String uri,String url){
		int result=-1;
		if(uri.equals(url)){	
			return EQ;
		}
		uri=formatUrl(uri);
		if(uri.startsWith(url)){
			return url.length();
		}else if(patternUrl(uri, url)){
			if(url.endsWith("*")){
				url=url.substring(0, url.length()-1);
			}
			return url.length();
		}else{
			int patternIndex=patternRestful(uri, url);
			if(patternIndex>0){
				return patternIndex;
			}
		}
		
		return result;
	}
}
