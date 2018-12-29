package com.ruixin.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import com.ruixin.annotation.All;
import com.ruixin.annotation.Args;
import com.ruixin.annotation.Autowired;
import com.ruixin.annotation.Bean;
import com.ruixin.annotation.Delete;
import com.ruixin.annotation.DeleteMapping;
import com.ruixin.annotation.GetMapping;
import com.ruixin.annotation.Insert;
import com.ruixin.annotation.Param;
import com.ruixin.annotation.PostMapping;
import com.ruixin.annotation.PutMapping;
import com.ruixin.annotation.QueryProvider;
import com.ruixin.annotation.Select;
import com.ruixin.annotation.Transaction;
import com.ruixin.annotation.Update;
import com.ruixin.annotation.Value;
import com.ruixin.annotation.Web;
import com.ruixin.proxy.DaoProxy;
import com.ruixin.proxy.ServiceProxy;
import com.ruixin.simpleDB.Mapper;
import com.ruixin.simpleDB.MapperContainer;
import com.ruixin.simpleDB.ParamContainer;
import com.ruixin.simpleDB.Provider;
import com.ruixin.simpleDB.QueryType;
import com.ruixin.simpleDB.SelectKey;
import com.ruixin.transaction.TransactionFactory;
import com.ruixin.util.PropertiesUtil;
import com.ruixin.util.ReflectUtil;
import com.ruixin.util.StringUtil;
import com.ruixin.util.TypeConvertor;
import com.ruixin.util.UrlUtil;
/**
 * @Author:ruixin
 * @Date: 2018年11月12日 下午4:46:21
 * @Description:注解解释类
 */
public class AnnotationConfig {

	private AnnotationConfig(){}
	public static AnnotationConfig instance=new AnnotationConfig();
	
	/**
	 * @Description:解析注解
	 */
	public void parseAnnotation(){
		parseBean();
		parseDao();
		parseService();
		parseWeb();
	}
	
	/**
	 * @Description:解析bean注解
	 */
	private void parseBean(){
		Map<String, Class<?>> beans = BeanFactory.instance.getBeans();
		beans.forEach((key,clz)->{
			Object bean = ReflectUtil.instance.newBean(clz);
			initBean(bean);
			//解析bean里面的属性上的注解
			List<Field> fields = ReflectUtil.instance.getFields(clz);
			fields.forEach(field->{
				parseFiledAnn(bean,field);
			});
			BeanFactory.instance.addBeanObj(key, bean);
		});
	}
	
	/**
	 * @param obj
	 * @Description:执行初始化方法
	 */
	private void initBean(Object obj){
		Map<String, Annotation> annotations = ReflectUtil.instance.getAnnotations(obj.getClass());
		String[] initMethods=null;
		if(annotations.containsKey(Bean.class.getSimpleName())){
			Bean bean=(Bean) annotations.get(Bean.class.getSimpleName());
			initMethods=bean.init();
		}
		
		if(initMethods!=null&&initMethods.length>0){
			for(String initMethod:initMethods){
				//注解默认值处理
				if(StringUtil.isBlank(initMethod)){
					continue;
				}
				Method method = ReflectUtil.instance.getMethod(obj.getClass(), initMethod);
				if(method==null){
					HandleException.getInstance().handler("Bean 注解中init属性错误，"+obj.getClass().getSimpleName()+"中没有"+initMethod+"方法", new RuntimeException());
					continue;
				}
				try {
					ReflectUtil.instance.callMethod(obj, method, null);
				} catch (Exception e) {
					HandleException.getInstance().handler("Bean 注解中init属性执行"+obj.getClass().getSimpleName()+"中"+initMethod+"方法错误", e);					
				}
			}
		}
	}
	
	/**
	 * @Description:解析dao注解
	 */
	private void parseDao(){
		Map<String, Class<?>> daos = BeanFactory.instance.getDaos();
		daos.forEach((key,clz)->{
			Object dao=null;
			if(clz.isInterface()){
				dao = DaoProxy.newInstance(clz);
			}
			//此处是dao逻辑代码
			Method[] methods=clz.getDeclaredMethods();
			for(Method method:methods){
				parseDaoMethodAnn(dao,method);
				parseDaoMethodParameterAnn(dao,method);
			}
			BeanFactory.instance.addBeanObj(key, dao);
		});
	}
	
	/**
	 * @param dao
	 * @param method
	 * @Description:解析dao方法参数上的Param注解
	 */
	private void parseDaoMethodParameterAnn(Object dao, Method method) {
		 Map<Integer, Map<String, Annotation>> paramAnnotations = ReflectUtil.instance.getParamAnnotations(method);
		 paramAnnotations.forEach((index,anns)->{
			 if(anns.containsKey(Param.class.getSimpleName())){
				 Param param=(Param) anns.get(Param.class.getSimpleName());
				 com.ruixin.simpleDB.Parameter parameter=new com.ruixin.simpleDB.Parameter(method.getDeclaringClass().getSimpleName(),method.getName(),index,param.value(),method.getParameterTypes()[index]);
				 ParamContainer.getInstance().addParam(parameter);
			 }
		 });
	}

	/**
	 * @param dao
	 * @param method
	 * @Description:解析dao层方法上的注解
	 */
	private void parseDaoMethodAnn(Object obj, Method method) {
		Map<String, Annotation> annotations = ReflectUtil.instance.getAnnotations(method);
		//解析Insert注解
		if(annotations.containsKey(Insert.class.getSimpleName())){
			Insert insert=(Insert) annotations.get(Insert.class.getSimpleName());
			String mapperId=method.getDeclaringClass().getSimpleName()+"."+method.getName();
			Mapper mapper=new Mapper(mapperId,QueryType.INSERT);
			if(StringUtil.isNotBlank(insert.sql())){
				mapper.setSql(insert.sql());
			}else if(StringUtil.isNotBlank(insert.value())){
				mapper.setSql(insert.value());
			}
			mapper.setReturnType(insert.returnType());
			mapper.setUseGeneratedKeys(insert.useGeneratedKeys());
			mapper.setSelectKey(parseSelectKey(annotations));
			MapperContainer.getInstance().addMapper(mapper);
		}
		//解析Delete注解
		else if(annotations.containsKey(Delete.class.getSimpleName())){
			Delete delete=(Delete) annotations.get(Delete.class.getSimpleName());
			String mapperId=method.getDeclaringClass().getSimpleName()+"."+method.getName();
			Mapper mapper=new Mapper(mapperId,QueryType.DELETE);
			if(StringUtil.isNotBlank(delete.sql())){
				mapper.setSql(delete.sql());
			}else if(StringUtil.isNotBlank(delete.value())){
				mapper.setSql(delete.value());
			}
			mapper.setSelectKey(parseSelectKey(annotations));
			MapperContainer.getInstance().addMapper(mapper);
		}
		//解析update注解
		else if(annotations.containsKey(Update.class.getSimpleName())){
			Update update=(Update) annotations.get(Update.class.getSimpleName());
			String mapperId=method.getDeclaringClass().getSimpleName()+"."+method.getName();
			Mapper mapper=new Mapper(mapperId,QueryType.UPDATE);
			if(StringUtil.isNotBlank(update.sql())){
				mapper.setSql(update.sql());
			}else if(StringUtil.isNotBlank(update.value())){
				mapper.setSql(update.value());
			}
			mapper.setSelectKey(parseSelectKey(annotations));
			MapperContainer.getInstance().addMapper(mapper);
		}
		//解析select注解
		else if(annotations.containsKey(Select.class.getSimpleName())){
			Select select=(Select) annotations.get(Select.class.getSimpleName());
			String mapperId=method.getDeclaringClass().getSimpleName()+"."+method.getName();
			Mapper mapper=new Mapper(mapperId,QueryType.SELECT);
			if(StringUtil.isNotBlank(select.sql())){
				mapper.setSql(select.sql());
			}else if(StringUtil.isNotBlank(select.value())){
				mapper.setSql(select.value());
			}
			mapper.setSelectKey(parseSelectKey(annotations));
			mapper.setReturnType(select.returnType());
			MapperContainer.getInstance().addMapper(mapper);
		}
		//解析QueryProvider注解
		else if(annotations.containsKey(QueryProvider.class.getSimpleName())){
			QueryProvider queryProvider=(QueryProvider) annotations.get(QueryProvider.class.getSimpleName());
			String providerId=method.getDeclaringClass().getSimpleName()+"."+method.getName();
			Provider provider=new Provider(providerId);
			provider.setType(queryProvider.type());
			provider.setMethod(queryProvider.method());
			provider.setQueryType(queryProvider.queryType());
			provider.setReturnType(queryProvider.returnType());
			provider.setSelectKey(parseSelectKey(annotations));
			provider.setUseGeneratedKeys(queryProvider.useGeneratedKeys());
			MapperContainer.getInstance().addProvider(provider);
		}
	}

	/**
	 * @param annotations
	 * @return
	 * @Description:解析SelectKey注解
	 */
	private SelectKey parseSelectKey(Map<String, Annotation> annotations){
		if(annotations.containsKey(com.ruixin.annotation.SelectKey.class.getSimpleName())){
			com.ruixin.annotation.SelectKey selectKey=(com.ruixin.annotation.SelectKey) annotations.get(com.ruixin.annotation.SelectKey.class.getSimpleName());
			SelectKey selectKey2=new SelectKey(selectKey.statement(),selectKey.resultType(),selectKey.order(),selectKey.keyProperty());
			return selectKey2;
		}
		return null;
	}
	
	/**
	 * @Description:解析service注解
	 */
	private void parseService(){
		Map<String, Class<?>> services = BeanFactory.instance.getServices();
		services.forEach((key,clz)->{
			Object service = ServiceProxy.newInstance(clz);
			//解析service里面的属性上的注解
			List<Field> fields = ReflectUtil.instance.getFields(clz);
			fields.forEach(field->{
				parseFiledAnn(service,field);
			});
			
			//解析Service上的Transaction注解
			Map<String, Annotation> annotations = ReflectUtil.instance.getAnnotations(clz);
			if(annotations.containsKey(Transaction.class.getSimpleName())){
				Transaction transaction=(Transaction) annotations.get(Transaction.class.getSimpleName());
				paraseServiceTransaction(clz,transaction);
			}
			//解析service方法上的注解  事务
			Method[] methods=clz.getDeclaredMethods();
			for(Method method:methods){
				parseServiceMethodAnn(service,method);
			}
			BeanFactory.instance.addBeanObj(key, service);
		});
	}
	
	/**
	 * @Description:解析service类上面的Transaction注解
	 */
	private void paraseServiceTransaction(Class<?> clz,Transaction traAnn){
		Method[] methods=clz.getDeclaredMethods();
		String clzName=clz.getSimpleName();
		for(Method method:methods){
			String methodName=method.getName();
			com.ruixin.transaction.Transaction transaction=new com.ruixin.transaction.Transaction(clzName,methodName,traAnn.readOnly(),traAnn.level());
			TransactionFactory.getInstance().addTransaction(transaction);
		}
	}
	
	/**
	 * @param service
	 * @param method
	 * @Description:解析service方法上的注解
	 */
	private void parseServiceMethodAnn(Object service, Method method) {
		Map<String, Annotation> annotations = ReflectUtil.instance.getAnnotations(method);
		String className=method.getDeclaringClass().getSimpleName();
		String methodName=method.getName();
		//解析Transaction
		if(annotations.containsKey(Transaction.class.getSimpleName())){
			Transaction transaction=(Transaction) annotations.get(Transaction.class.getSimpleName());
			com.ruixin.transaction.Transaction transaction1=new com.ruixin.transaction.Transaction(className,methodName,transaction.readOnly(),transaction.level());
			TransactionFactory.getInstance().addTransaction(transaction1);
		}
	}

	
	
	/**
	 * @Description:解析web注解
	 */
	private void parseWeb(){
		Map<String, Class<?>> webs = BeanFactory.instance.getWebs();
		webs.forEach((key,clz)->{
			Object web = ReflectUtil.instance.newBean(clz);
			//解析web里面的属性上的注解
			List<Field> fields = ReflectUtil.instance.getFields(clz);
			fields.forEach(field->{
				parseFiledAnn(web,field);
			});
			//解析web里面方法上的注解
			Method[] methods=clz.getDeclaredMethods();
			for(Method method:methods){
				parseWebMethodAnn(web,method);
			}
			BeanFactory.instance.addBeanObj(key, web);
		});
	}
	
	/**
	 * @param obj
	 * @param field
	 * @Description:解析属性值上的注解
	 */
	private void parseFiledAnn(Object obj,Field field){
		Map<String, Annotation> annotations = ReflectUtil.instance.getAnnotations(field);
		if(annotations.isEmpty()){
			return;
		}
		//解析autowire注解
		if(annotations.containsKey(Autowired.class.getSimpleName())){
			Autowired autowired=(Autowired) annotations.get(Autowired.class.getSimpleName());
			String beanName=StringUtil.isNotBlank(autowired.value())?autowired.value():field.getName();
			Object object=BeanFactory.instance.getBeanObj(beanName);
			ReflectUtil.instance.invokField(obj, field, object,false);
		}
		//解析Value注解
		if(annotations.containsKey(Value.class.getSimpleName())){
			Value value=(Value) annotations.get(Value.class.getSimpleName());
			String key=StringUtil.isNotBlank(value.value())?value.value():field.getName();
			String object=PropertiesUtil.getProperty(key);
			if(StringUtil.isNotBlank(object)){				
				ReflectUtil.instance.invokField(obj, field, object ,true);
			}
		}
	}
	
	/**
	 * @param obj
	 * @param method
	 * @Description:解析web上的注解
	 */
	private void parseWebMethodAnn(Object obj,Method method){
		Map<String, Annotation> annotations = ReflectUtil.instance.getAnnotations(method);
		Web web=obj.getClass().getAnnotation(Web.class);
		String pre=web.preUrl();
		//解析Get注解
		if(annotations.containsKey(GetMapping.class.getSimpleName())){
			GetMapping get=(GetMapping) annotations.get(GetMapping.class.getSimpleName());
			String[] urls=get.value(); //支持多个url
			for(String url:urls){
				String uri=UrlUtil.parseUrl(pre, url);
				HandlerMapping.instance.addMapping(obj, "GET", uri, method);
			}
		}
		//解析Post注解
		else if(annotations.containsKey(PostMapping.class.getSimpleName())){
			PostMapping get=(PostMapping) annotations.get(PostMapping.class.getSimpleName());
			String[] urls=get.value(); //支持多个url
			for(String url:urls){
				String uri=UrlUtil.parseUrl(pre, url);
				HandlerMapping.instance.addMapping(obj, "POST", uri, method);
			}
		}
		//解析Delete注解
		else if(annotations.containsKey(DeleteMapping.class.getSimpleName())){
			DeleteMapping delete=(DeleteMapping) annotations.get(DeleteMapping.class.getSimpleName());
			String[] urls=delete.value(); //支持多个url
			for(String url:urls){
				String uri=UrlUtil.parseUrl(pre, url);
				HandlerMapping.instance.addMapping(obj, "DELETE", uri, method);
			}
		}
		//解析Put注解
		else if(annotations.containsKey(PutMapping.class.getSimpleName())){
			PutMapping put=(PutMapping) annotations.get(PutMapping.class.getSimpleName());
			String[] urls=put.value(); //支持多个url
			for(String url:urls){
				String uri=UrlUtil.parseUrl(pre, url);
				HandlerMapping.instance.addMapping(obj, "PUT", uri, method);
			}
		}
		//解析All注解
		else if(annotations.containsKey(All.class.getSimpleName())){
			All all=(All) annotations.get(All.class.getSimpleName());
			String[] urls=all.value(); //支持多个url
			for(String url:urls){
				String uri=UrlUtil.parseUrl(pre, url);
				HandlerMapping.instance.addMapping(obj, "ALL", uri, method);
			}
		}
	}
	
	/**
	 * @param method
	 * @param parameters
	 * @Description:处理方法参数上的注解Args
	 */
	public void parseArgs(Method method,Object[] args,Map<String,String[]> reqArgs){
		Parameter[] parameters = method.getParameters();
		for(int i=0;i<parameters.length;i++){
			Args annotation = parameters[i].getAnnotation(Args.class);
			if(annotation!=null){
				String value=annotation.value();
				String defaultValue=annotation.defaultValue();
				boolean require=annotation.require();
				String[] strings = reqArgs.get(value);
				if(strings!=null&&strings.length>0){
					if(parameters[i].getType().isArray()){
						Object array=Array.newInstance(parameters[i].getType().getComponentType(),strings.length);
						for (int j = 0; j < strings.length; j++) {
							Object rs=TypeConvertor.parseData(parameters[i].getType().getComponentType(),strings[j]);
							Array.set(array, j, rs);
						}
						args[i]=array;
					}else{
						Object arg=TypeConvertor.parseData(parameters[i].getType(),strings[0]);
						args[i]=arg;
					}
				}else if(require&&(strings==null||strings.length==0)){
					HandleException.getInstance().handler(value+" id require but not found",new Exception());
				}else if(StringUtil.isNotBlank(defaultValue)){
					if(parameters[i].getType().isArray()){
						Object[] arg=new Object[strings.length];
						for (int j = 0; j < arg.length; j++) {
							arg[j]=TypeConvertor.parseData(parameters[i].getType().getComponentType(),strings[j]);
						}
						args[i]=arg;
					}else{
						Object arg=TypeConvertor.parseData(parameters[i].getType(),defaultValue);
						args[i]=arg;
					}
				}
			}
		}
	}
}
