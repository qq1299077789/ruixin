package com.ruixin.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.ruixin.config.HandleException;

/**
 * @Author:ruixin
 * @Date: 2018年11月11日 下午2:42:45
 * @Description:反射工具类
 */
public class ReflectUtil {

	private ReflectUtil(){};
	
	public static ReflectUtil instance = new ReflectUtil();
	
	/**
	 * @param clz
	 * @return
	 * @Description:获取一个类的注解
	 */
	public Map<String,Annotation> getAnnotations(Class<?> clz){
		Annotation[] annotations = clz.getAnnotations();
		Map<String,Annotation> anns= new HashMap<>();
		for(Annotation ann:annotations){
			anns.put(ann.annotationType().getSimpleName(),ann);
		}
		return anns;
	}
	
	/**
	 * @param method
	 * @return
	 * @Description:获取方法上的注解
	 */
	public Map<String,Annotation> getAnnotations(Method method){
		Annotation[] annotations = method.getAnnotations();
		Map<String,Annotation> anns= new HashMap<>();
		for(Annotation ann:annotations){
			anns.put(ann.annotationType().getSimpleName(),ann);
		}
		return anns;
	}
	
	/**
	 * @param method
	 * @return 参数的位置，注解
	 * @Description:获取方法参数上的注解
	 */
	public Map<Integer,Map<String,Annotation>> getParamAnnotations(Method method){
		Map<Integer,Map<String,Annotation>> anns= new HashMap<>();
		Parameter[] parameters = method.getParameters();
		for(int i=0;i<parameters.length;i++){
			Parameter parameter = parameters[i];
			Annotation[] annotations = parameter.getAnnotations();
			Map<String,Annotation> map=new HashMap<>();
			for(Annotation annotation:annotations){
				map.put(annotation.annotationType().getSimpleName(), annotation);
			}
			anns.put(i, map);
		}
		return anns;
	}
	
	/**
	 * @param clz
	 * @return
	 * @Description:创建一个新对象
	 */
	public Object newBean(Class<?> clz){
		try {
			return clz.newInstance();
		} catch (Exception e) {
			HandleException.getInstance().handler(e);
		}
		return null;
	}
	
	/**
	 * @param clz
	 * @return
	 * @Description:获取一个对象的所有属性
	 */
	public List<Field> getFields(Class<?> clz){
		Field[] fields = clz.getDeclaredFields();
		return Arrays.asList(fields);
	}
	
	/**
	 * @param clz
	 * @return
	 * @Description:获取一个对象的所有属性
	 */
	public Map<String,Field> getMapFields(Class<?> clz){
		Field[] fields = clz.getDeclaredFields();
		Map<String, Field> fieldMap=new HashMap<String,Field>();
		for(Field field:fields){
			field.setAccessible(true);
			fieldMap.put(field.getName(), field);
		}
		return fieldMap;
	}
	
	/**
	 * @param field
	 * @return
	 * @Description:获取field上的所有注解
	 */
	public Map<String,Annotation> getAnnotations(Field field){
		Annotation[] annotations = field.getAnnotations();
		Map<String,Annotation> anns= new HashMap<>();
		for(Annotation ann:annotations){
			anns.put(ann.annotationType().getSimpleName(),ann);
		}
		return anns;
	}
	
	/**
	 * @param obj
	 * @param field
	 * @param arg
	 * @param needParse 是否需要转换
	 * @Description:设置属性值
	 */
	public void invokField(Object obj,Field field,Object arg,boolean needParse){
		field.setAccessible(true);
		try {
			if(needParse){
				arg=TypeConvertor.parseData(field.getType(),arg);
			}
			field.set(obj, arg);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			HandleException.getInstance().handler(e);
		}
	}
	
	/**
	 * @param obj
	 * @param method 
	 * @param args  方法的参数
	 * @throws Throwable 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @Description:调用方法
	 */
	public Object callMethod(Object obj,Method method,Object...args) throws Exception{
		return method.invoke(obj, args);
	}
	
	/**
	 * @param clz
	 * @return
	 * @Description:获取一个类的所有子类集合
	 */
	public List<Class<?>> getSubclasses(Class<?> clz) {
		Field field = null;
        Vector<?> v = null;
        List<Class<?>> allSubclass = new ArrayList<Class<?>>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?> classOfClassLoader = classLoader.getClass();
        while (classOfClassLoader != ClassLoader.class) {
            classOfClassLoader = classOfClassLoader.getSuperclass();
        }
        try {
            field = classOfClassLoader.getDeclaredField("classes");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
        	HandleException.getInstance().handler(e);
        }
        try {
            v = (Vector<?>) field.get(classLoader);
        } catch (IllegalAccessException e) {
        	HandleException.getInstance().handler(e);
        }
        for (int i = 0; i < v.size(); ++i) {
            Class<?> c = (Class<?>) v.get(i);
            if (clz.isAssignableFrom(c) && !clz.equals(c)) {
                allSubclass.add((Class<?>) c);
            }
        }
        return allSubclass;
    }
	
	/**
	 * @param clz
	 * @return
	 * @Description:获取对象实例
	 */
	public Object getInstance(Class<?> clz){
		Object newInstance=null;
		try {
			newInstance = clz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			HandleException.getInstance().handler(e);
		}
		return newInstance;
	}
	
	/**
	 * @param clz
	 * @param fieldName
	 * @return
	 * @Description:获取Field
	 */
	public Field getField(Class<?> clz,String fieldName){
		 Map<String, Field> mapFields = getMapFields(clz);
		 return mapFields.get(fieldName);
	}

	/**
	 * @param clz
	 * @param methodName
	 * @return
	 * @Description 通过方法名查找方法
	 */
	public Method getMethod(Class<?> clz,String methodName) {
		return getMethods(clz).get(methodName);
	}
	
	/**
	 * @param clz
	 * @return
	 * @Description:获取类的所有方法
	 */
	public Map<String,Method> getMethods(Class<?> clz){
		Map<String,Method> methods=new HashMap<>();
		for(Method method:clz.getMethods()){
			method.setAccessible(true);
			methods.put(method.getName(), method);
		}
		return methods;
	}
}
