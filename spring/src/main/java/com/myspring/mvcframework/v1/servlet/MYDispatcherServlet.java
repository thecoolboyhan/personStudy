package com.myspring.mvcframework.v1.servlet;

import com.myspring.mvcframework.annotation.MYAutowired;
import com.myspring.mvcframework.annotation.MYController;
import com.myspring.mvcframework.annotation.MYRequestMapping;
import com.myspring.mvcframework.annotation.MYService;

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MYDispatcherServlet extends HttpServlet {
    private Map<String, Object> mapping=new HashMap<>();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        this.doPost(request,response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        try {
            doDispatch(request,response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest request,HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url=url.replace(contextPath,"").replaceAll("/+","/");
        if (!this.mapping.containsKey(url)){
            response.getWriter().write("404 Not found!");
            return;
        }
        Method method = (Method) this.mapping.get(url);
        Map<String, String[]> params = request.getParameterMap();
        method.invoke(this.mapping.get(method.getDeclaringClass().getName()),new Object[]{request,response,params.get("name")[0]});
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        InputStream inputStream=null;
        try {
            Properties configContext = new Properties();
            inputStream=this.getClass().getClassLoader().getResourceAsStream(config.getInitParameter("contextConfigLocation"));
            configContext.load(inputStream);
            String scanPackage = (String) configContext.get("scanPackage");
            doScanner(scanPackage);
            for (String className: mapping.keySet()){
                if (!className.contains("."))
                    continue;
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MYController.class)){
                    mapping.put(className,clazz.newInstance());
                    String baseUrl="";
                    if (clazz.isAnnotationPresent(MYRequestMapping.class)){
                        MYRequestMapping requestMapping = clazz.getAnnotation(MYRequestMapping.class);
                        baseUrl = requestMapping.value();
                    }
                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(MYRequestMapping.class))
                            continue;
                        MYRequestMapping requestMapping = method.getAnnotation(MYRequestMapping.class);
                        String url=(baseUrl+"/"+requestMapping.value()).replaceAll("/+","/");
                        mapping.put(url,method);
                        System.out.println("Mapped"+url+","+method);
                    }
                }else if (clazz.isAnnotationPresent(MYService.class)){
                    MYService myService = clazz.getAnnotation(MYService.class);
                    String beanName = myService.value();
                    if ("".equals(beanName))
                        beanName=clazz.getName();
                    Object instance = clazz.newInstance();
                    mapping.put(beanName,instance);
                    for (Class<?> anInterface : clazz.getInterfaces()) {
                        mapping.put(anInterface.getName(),instance);
                    }
                }
            }
            for (Object object : mapping.values()) {
                if (object==null)
                    continue;
                Class<?> clazz = object.getClass();
                if (clazz.isAnnotationPresent(MYController.class)){
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        if (!field.isAnnotationPresent(MYAutowired.class))
                            continue;
                        MYAutowired autowired = field.getAnnotation(MYAutowired.class);
                        String beanName = autowired.value();
                        if ("".equals(beanName))
                            beanName = field.getType().getName();
                        field.setAccessible(true);
                        field.set((mapping.get(clazz.getName())),mapping.get(beanName));
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("MY MVC Framework is init");
    }

    private void doScanner(String scanPackage){
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replace("\\", "/"));
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()){
                doScanner(scanPackage+"."+file.getName());
            }else {
                if (!file.getName().endsWith(".class")){
                    continue;
                }
                String clazzName=(scanPackage+"."+file.getName().replace(".class",""));
                mapping.put(clazzName,null);
            }
        }
    }
}
