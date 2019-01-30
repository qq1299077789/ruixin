# ruixin 

---

## 一、入口操作

现有的代码里面，入口操作类是StartLoader，它实现ServletContextListener接口，在tomcat启动时，会自动加载此类的contextInitialized方法，进而调用start入口操作代码。

start方法调用时，首先会读取ruixin.properties配置文件，根据配置文件的bean.package属性值进行bean扫描加载操作。
### 1.ruixin.properties 属性解释

* bean.package 表示需要扫描bean的包名，默认值com.ruixin,支持模糊扫描，也支持多包扫描，包含多个包时，用逗号隔开。(为了提升加载速度，包名越详细越好)

* web.prefix 表示视图层前缀，默认值是/WEB-INF/jsp/

* web.suffix=.jsp 表示视图层后缀，默认值是 .jsp

* db.name 表示数据库用户名

* db.pass 表示数据库密码

* db.dirver 表示数据库驱动

* db.url 表示数据库链接

* db.poolSize 表示数据库连接池大小

* file.uploadPath 表示文件上传地址 默认值是/upload
## 二、Bean管理（BeanFactory工厂类）

BeanFactory工厂类主要来管理注入的Bean，Bean的种类主要包括web、service、dao以及普通的bean，这分别对应@Web、@Service、@Dao、@Bean注解。支持手工注入。

## 三、Annotation注解解析(AnnotationConfig注解解释类、AnnoUtil注解工具类)

在入口操作start方法中，会执行AnnoUtil注解工具类的parseClassAnn方法，来分别把扫描到的所有类根据类注解挨个注入到BeanFactory中。然后再由AnnotationConfig类的parseAnnotation方法开始解析，类注解解析的先后顺序是@Bean>@Dao>@Service>@Web。类注解解析后立即开始解析属性注解@Autowire、@Value以及方法注解@JsonReturn、@All、@GetMapping、@PostMapping、@DeleteMapping、@PutMapping和方法参数注解@Args、@PathVariable。

### 1.ClassAnnotation 类注解解释
* @Bean 普通的bean，但是不属于控制层，业务层和持久层
    * value：表示bean的唯一标识，为空则默认是类名称
    * init：初始化后默认加载方法，允许多个
 
* @Web 控制层bean
    * value：表示bean的唯一标识，为空则默认是类名称
    * preUrl：表示访问前缀
 
* @Service 业务层bean
    * value：表示bean的唯一标识，为空则默认是类名称
 
 
* @Dao 持久层bean(支持class interface两种类型，class类型Aop失效)
    * value：表示bean的唯一标识，为空则默认是类名称

### 2.FieldAnnotation 属性注解解释

* @Autowire bean注入
    * value：注入的bean的唯一标志，为空则根据fieldName获取
 
* @Value 获取ruixin.properties配置文件的值
    * value：要获取配置文件的属性的key
 
### 3.MethodAnnotation 方法注解解释

* @RequestMapping 响应所有http请求
    * value：响应的链接
 
* @PostMapping 响应Post请求
    * value：响应的链接
 
* @GetMapping 响应Get请求
    * value：响应的链接
  
* @PutMapping 响应Put请求
    * value：响应的链接
  
* @DeleteMapping 响应Delete请求
    * value：响应的链接
  
* @JsonReturn 返回json数据

### 4.ParameterAnnotation 方法参数注解解释

* @Args 根据value值接收前端传过来的值
    * value：前台传过来的属性值的Key
    * defaultValue：获取的值为空时，设置默认值
    * require：是否必须存在
    
* @PathVariable 接收Restful形式的参数值

## 四、web流程

### 1.涉及的类

* 1.前端控制器DispatcherServlet 

* 2.分流器RequestDispatch 

* 3.适配器HandlerAdaptor 

* 4.Mapping类管理HandlerMapping

* 5.handler控制器(HandlerMethod)

* 6.视图控制器ViewAdaptor

### 2.流程

http->DispatcherServlet->RequestDispatch->HandlerAdaptor->HandlerMapping->HandlerAdaptor->RequestDispatch

->ViewAdaptor(handler返回的是视图数据)

->DispatcherServlet(handler返回的是JSON数据)

* 1.DispatcherServlet 拦截所有的http请求

* 2.RequestDispatch 将DispatcherServlet拦截的请求转发到适配器HandlerAdaptor

* 3.HandlerAdaptor 将RequestDispatch中转发的请求转入handlerMapping

* 4.HandlerMapping 通过获取到的http请求，找到Mapping，并返回给HandlerAdaptor

* 5.HandlerAdaptor 将获取到的Mapping转化为HandlerMethod，并调用handler，返回handler的结果集

* 6.RequestDispatch 解析HandlerAdaptor返回的结果集，如果是视图层数据，则传入ViewAdaptor，如果是JSON数据则直接返回给DispatcherServlet

* 7.1.ViewAdaptor 解析HandlerAdaptor传入的结果集，跳转到界面

* 7.2.DispatcherServlet 返回handler的结果集

## 五、handler监听器（HandlerListener）

系统已内置handler监听器，如果需要监听拦截handler，直接实现HandlerListener接口即可。

* before handler调用之前调用

* after handler调用之后调用

## 六、Handler方法内置属性

* HttpServletRequest

* HttpServletResponse

* HttpSession

* ModelMap 支持向前端传值，设置view

* MultipartFile 文件上传类

## 七、文件上传(MultipartFile)

进行文件上传操作时，系统在handler参数中内置MultipartFile类，只需在handler中只需要调用MultipartFile的upload方法，方法的返回值是文件保存的路径集合，支持多文件上传。

## 八、ModelMap

ModelMap类是模仿Springmvc的ModelAndView类，内置HttpServletRequest对象，支持向前端传值，以及返回视图层数据。

如果handler没有@JsonReturn注解，且返回类型是String或者ModelMap，则默认handler返回是视图层数据，反之则返回的是Json数据。

## 九、Restful
Restful主要用占位符$表示，比如：
```java
//需要匹配 /a/b/c中的b
//Controller中
@GetMapping("/a/$b/c")
public String test(@PathVariable("b")int b){
    //逻辑代码
}
//即可匹配
```
## 十、事务处理
事务处理主要用的是@Transaction注解，允许作用于类或者方法上，作用于类上表示对该类所有的方法加上事务，作用于方法上表示只对这一个方法加事务。
### 1.注解
* @Transaction 事务注解，为此类所有方法加上事务
    * readOnly：是否只读
    * level：事务级别
 
## 十一、缓存
系统内置DefaultCache缓存，也已经把CacheManager注册为bean，可以通过@Autowire注解获取CacheManager对象，手动生成Cache。也可以通过@Autowire注解获取系统默认缓存DefaultCache对象。
## 十二、持久层
### 1.主要的注解
* @Insert 执行新增语句 
    * value：属性值为SQL语句
    * sql：与value等价，属性值是SQL语句
    * returnType：返回值类型
    * useGeneratedKeys：是否是自增主键
    * keyProperty：主键属性名
 
* @Select 执行查询语句
    * value：属性值为SQL语句
    * sql：与value等价，属性值是SQL语句
    * returnType：返回值类型
 
* @Update 执行更新语句
    * value：属性值为SQL语句
    * sql：与value等价，属性值是SQL语句
 
* @Delete 执行删除语句
    * value：属性值为SQL语句
    * sql：与value等价，属性值是SQL语句
 
* @QueryProvider 执行动态语句
    * type：动态语句操作类
    * method：动态语句操作类的方法
    * queryType：QueryType.SELECT、QueryType.INSERT、QueryType.UPDATE、QueryType.DELETE分别表示动态sql的类型
    * returnType：返回类型
    * useGeneratedKeys：主键是否自增
 
* @SelectKey 在执行增删改查操作时，执行其他操作
    * statement：sql语句
    * resultType：返回类型
    * order：Order.BEFORE、Order.AFTER表示在sql执行前还是执行后执行selectKey中的SQL
    * keyProperty：对象属性注入名称
 
* @Param 持久层属性值映射
    * value：属性值标识

