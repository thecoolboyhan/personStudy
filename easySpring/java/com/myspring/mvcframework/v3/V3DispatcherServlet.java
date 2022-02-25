package com.myspring.mvcframework.v3;

import com.myspring.mvcframework.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rose
 */
public class V3DispatcherServlet extends HttpServlet {

    /** todo 全局成员变量，IoC容器是注册单例形式 */
    //保存application.properties配置文件中的内容
    private Properties contextConfig=new Properties();

    //保存扫描的所有的类名
    private List<String> classNames=new ArrayList<>();

    /**
     * 传说中的IOC容器，为了简化程序，暂时不考虑ConcurrentHashMap
     */
    private Map<String,Object> ioc=new HashMap<>();

    //保存url和Method的对应关系
//    private Map<String, Method> handlerMapping=new HashMap<>();
    private List<Handler> handlerMapping=new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        //2.扫描相关的类
        doScanner(contextConfig.getProperty("scanPackage"));

        //3.初始化扫描到的类，并把它们放到IOC容器中
        doInstance();

        //4.完成依赖注入
        doAutowired();

        //5.初始化HandlerMapping
        initHandlerMapping();

        System.out.println("GP Spring framework is init.");
    }

    //加载配置文件
    private void doLoadConfig(String contextConfigLocation){
        //通过类路径找到Spring主配置文件所在的路径
        //把它读出来放到properties对象中
        //把scanPackage保存到内存中
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doScanner(String scanPackage){
        //scanPackage保存包路径
        //转换为文件路径，实际上就是把.替换成/
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()){
                doScanner(scanPackage+"."+file.getName());
            }else {
                if (!file.getName().endsWith(".class"))
                    continue;
                String className=(scanPackage+"."+file.getName().replace(".class",""));
                classNames.add(className);

            }
        }
    }

    //利用工厂模式
    private void doInstance(){
        //初始化，为DI做准备
        if (classNames.isEmpty())
            return;
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                //什么样的类才需要初始化
                //加了注释的类才需要初始化，
                //为了简化，只用@Controller 和@Service注释的初始化，其他不举例
                if (clazz.isAnnotationPresent(MYController.class)){
                    Object instance = clazz.newInstance();
                    //Spring 默认类名首字母小写
                    String beanName = toLowerFirstCase(clazz.getName());
                    ioc.put(beanName,instance);
                }else if (clazz.isAnnotationPresent(MYService.class)){
                    MYService service = clazz.getAnnotation(MYService.class);
                    String beanName = service.value();
                    //默认类名首字母小写
                    //删除字符串的头尾空白符
                    if ("".equals(beanName.trim())){
                        beanName=toLowerFirstCase(clazz.getSimpleName());
                    }

                    Object instance = clazz.newInstance();
                    ioc.put(beanName,instance);
                    //3.根据类型自动赋值，这是投机取巧的方式
                    for (Class<?> i : clazz.getInterfaces()) {
                        if (ioc.containsKey(i.getName())){
                            throw new Exception("The"+i.getName()+"is exists!!");
                        }
                        //把接口的类型直接当成key
                        ioc.put(i.getName(),instance);
                    }
                }else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 类名首字母小写
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName){
        char[] chars = simpleName.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }


    private void doAutowired(){
        if (ioc.isEmpty())
            return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //获取所有的字段，包括private等
            //普通的oop只能获取public的字段
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(MYAutowired.class))
                    continue;
                MYAutowired autowired = field.getAnnotation(MYAutowired.class);

                //如果用户没有自定义beanName，默认根据类型来注入
                String beanName = autowired.value().trim();
                if("".equals(beanName))
                {
                    //获得接口的类型，作为key，然根据这个类型去ioc容器中取值
                    beanName=field.getType().getName();
                }else {
                    //判断类名首字母小写的情况，首字母变大写
                    beanName=smallToBig(beanName);
                }

                //如果是public以外的类型，只要加了@Auutowired就要强制赋值
                //利用反射的暴力访问
                field.setAccessible(true);
                try {
                    //利用反射机制，动态给字段赋值
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private String smallToBig(String simpleString){
        char[] chars = simpleString.toCharArray();
        if (chars[0]>=97&&chars[0]<=122)
            chars[0]-=32;
        return String.valueOf(chars);
    }

    /**
     * 初始化method和url的一对一关系
     */
    private void initHandlerMapping(){
        if (ioc.isEmpty())
            return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(MYController.class))
                continue;

            //保存写在类上的@MYRequestMapping("/demo")
            String baseUrl="";
            //获取Controller的url配置
            if (clazz.isAnnotationPresent(MYRequestMapping.class)){
                MYRequestMapping requestMapping = clazz.getAnnotation(MYRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            //默认获取所有的public类型的方法
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(MYRequestMapping.class))
                    continue;
                //映射url
                MYRequestMapping requestMapping = method.getAnnotation(MYRequestMapping.class);
                //优化
                String url=("/"+baseUrl+"/"+requestMapping.value()).replaceAll("/+","/");
                //添加正则匹配
                Pattern pattern = Pattern.compile(url);
//                handlerMapping.put(url,method);
                handlerMapping.add(new Handler(pattern,entry.getValue(),method));
                System.out.println("Mapping:"+url+","+method);
            }
        }
    }

    private Handler getHandler(HttpServletRequest req){
        if (handlerMapping.isEmpty())
            return null;
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url=url.replace(contextPath,"").replaceAll("/+","/");
        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.pattern.matcher(url);
            //如果没有匹配上，继续匹配下一个
            if (!matcher.matches())
                continue;
            return handler;
        }
        return null;
    }

    //url传过来的都是string类型的，由于HTTP基于字符串协议
    //只需要把string转换为任意类型
    private Object convert(Class<?> type,String value){
        if (Integer.class==type)
            return Integer.valueOf(value);
        //这里可以使用策略模式
        return value;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //运行阶段
        try {
            //委派模式
            doDispatch(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doDispatch(HttpServletRequest req,HttpServletResponse resp)throws Exception{
        Handler handler = getHandler(req);
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url=url.replaceAll(contextPath,"").replaceAll("/+","/");
        if (handler==null){
//        if (!this.handlerMapping.containsKey(url)){
            resp.getWriter().write("404 Not Found!!");
            return;
        }

//        Method method = this.handlerMapping.get(url);
        //第一个参数，方法所有的实例
        //第二个参数，调用时所需要的实例
        Map<String, String[]> params = req.getParameterMap();
        //获取方法的行参列表
        Class<?>[] parameterTypes = handler.method.getParameterTypes();
        //保存请求的url参数列表
        Map<String, String[]> parameterMap = req.getParameterMap();
        //保存赋值参数的位置
        Object[] paramValues = new Object[parameterTypes.length];
        for (Map.Entry<String, String[]> parm : params.entrySet()) {
            String value = Arrays.toString(parm.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", "");
            if (handler.paramIndexMapping.containsKey(parm.getKey()))
                continue;
            Integer index = handler.paramIndexMapping.get(parm.getKey());
            parameterTypes[index] = (Class<?>) convert(parameterTypes[index], value);
        }
        //根据参数位置动态赋值
        if (handler.paramIndexMapping.containsKey(HttpServletRequest.class.getName())){
            Integer reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex]=req;
        }
        if (handler.paramIndexMapping.containsKey(HttpServletResponse.class.getName())){
            Integer respIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex]=resp;
        }
        Object returnValue = handler.method.invoke(handler.controller, paramValues);
        if (returnValue==null||returnValue instanceof Void)
            return;
        resp.getWriter().write(returnValue.toString());
    }

    /**
     * Handler 记录Controller中的RequestMapping和Method的对应关系
     * 内部类
     */
    private class Handler{
        protected Object controller;    //保存方分对应的实例
        protected Method method;        //保存映射的方法
        protected Pattern pattern;      //正则工具类
        protected Map<String,Integer> paramIndexMapping; //参数顺序

        /**
         * 创造一个handler的基本参数
         * @param pattern
         * @param controller
         * @param method
         */
        protected Handler(Pattern pattern,Object controller, Method method){
            this.controller=controller;
            this.method= method;
            this.pattern=pattern;
            paramIndexMapping = new HashMap<>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method){
            //提取方法中加了注解的参数
            Annotation[][] pa = method.getParameterAnnotations();
            for (int i = 0; i < pa.length; i++) {
                for (Annotation a : pa[i]) {
                    if (a instanceof MYRequestParam){
                        String paramName = ((MYRequestParam) a).value();
                        if (!"".equals(paramName.trim())){
                            paramIndexMapping.put(paramName,i);
                        }
                    }
                }
            }

            //提取方法中的request和response参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                if (type==HttpServletRequest.class||type==HttpServletResponse.class)
                    paramIndexMapping.put(type.getName(),i);
            }


        }



    }
}
