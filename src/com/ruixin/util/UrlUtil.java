package com.ruixin.util;

/**
 * @Author:ruixin
 * @Date: 2018年11月13日 上午11:21:54
 * @Description:Url工具类
 */
public class UrlUtil {

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
	
}
