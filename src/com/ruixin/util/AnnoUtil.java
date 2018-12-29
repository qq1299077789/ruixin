package com.ruixin.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.ruixin.annotation.Bean;
import com.ruixin.annotation.Dao;
import com.ruixin.annotation.Service;
import com.ruixin.annotation.Web;
import com.ruixin.config.BeanFactory;

/**
 * @Author:ruixin
 * @Date: 2018年11月12日 下午2:34:35
 * @Description:注解工具类
 */
public class AnnoUtil {

	private AnnoUtil(){}
	public static AnnoUtil instance=new AnnoUtil();
	
	/**
	 * @Description:解析所有class里面的bean
	 */
	public void parseClassAnn(){
		List<Class<?>> classes=PackageScan.instance.classes;
		for(Class<?> clz:classes){
			parseAnn(clz);
		}
	}
	
	/**
	 * @param clz
	 * @return
	 * @Description:判断一个类是否是bean
	 */
	public boolean isBean(Class<?> clz){
		Map<String, Annotation> annotations = ReflectUtil.instance.getAnnotations(clz);
		if(annotations.isEmpty()){
			return false;
		}else if(annotations.containsKey(Bean.class.getSimpleName())){
			return true;
		}else if(annotations.containsKey(Web.class.getSimpleName())){
			return true;
		}else if(annotations.containsKey(Service.class.getSimpleName())){
			return true;
		}else if(annotations.containsKey(Dao.class.getSimpleName())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @param clz
	 * @return
	 * @Description:获取一个类上的bean注解
	 */
	public Annotation getBeanAnn(Class<?> clz){
		Map<String, Annotation> annotations = ReflectUtil.instance.getAnnotations(clz);
		if(annotations.isEmpty()){
			return null;
		}else if(annotations.containsKey(Bean.class.getSimpleName())){
			return annotations.get(Bean.class.getSimpleName());
		}else if(annotations.containsKey(Web.class.getSimpleName())){
			return annotations.get(Web.class.getSimpleName());
		}else if(annotations.containsKey(Service.class.getSimpleName())){
			return annotations.get(Service.class.getSimpleName());
		}else if(annotations.containsKey(Dao.class.getSimpleName())){
			return annotations.get(Dao.class.getSimpleName());
		}else{
			return null;
		}
	}
	
	/**
	 * @param clz
	 * @Description:将class分类
	 */
	public void parseAnn(Class<?> clz){
		Map<String, Annotation> annotations = ReflectUtil.instance.getAnnotations(clz);
		String beanName="";
		if(annotations.containsKey(Bean.class.getSimpleName())){
			Bean bean=(Bean) annotations.get(Bean.class.getSimpleName());
			beanName=StringUtil.isNotBlank(bean.value())?bean.value():getBeanName(clz.getSimpleName());
			BeanFactory.instance.addBean(beanName, clz);
		}else if(annotations.containsKey(Web.class.getSimpleName())){
			Web web=(Web) annotations.get(Web.class.getSimpleName());
			beanName=StringUtil.isNotBlank(web.value())?web.value():getBeanName(clz.getSimpleName());
			BeanFactory.instance.addWeb(beanName, clz);
		}else if(annotations.containsKey(Service.class.getSimpleName())){
			Service service=(Service) annotations.get(Service.class.getSimpleName());
			beanName=StringUtil.isNotBlank(service.value())?service.value():getBeanName(clz.getSimpleName());
			BeanFactory.instance.addService(beanName, clz);
		}else if(annotations.containsKey(Dao.class.getSimpleName())){
			Dao dao=(Dao) annotations.get(Dao.class.getSimpleName());
			beanName=StringUtil.isNotBlank(dao.value())?dao.value():getBeanName(clz.getSimpleName());
			BeanFactory.instance.addDao(beanName, clz);
		}
	}

	/**
	 * @param str
	 * @return
	 * @Description:注解bean名称不存在时，默认bean名称
	 */
	private String getBeanName(String str){
		return str.substring(0, 1).toLowerCase()+str.substring(1, str.length());
	}
	
	/**
	 * @param method
	 * @param clz
	 * @return
	 * @Description:判断方法是否含有某个注解
	 */
	@SuppressWarnings("unchecked")
	public boolean hasAnnotations(Method method, Class<?> clz) {
		Annotation annotation = method.getAnnotation((Class<Annotation>) clz);
		if(annotation!=null){
			return true;
		}
		return false;
	}
}
