package com.代理.dongDJ.myJDKdong;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来生成代理类的源代码
 * @author rose
 */
public class GPProxy {

    //换行
    public static final String ln="\r\n";
    public static Object newProxyInstance(GPClassLoader classLoader,Class<?>[] interfaces,GPInvocationHandler h){


        try {
            //todo 需要先动态生成代理后的代码文件.java
            String src = generateSrc(interfaces);

            // TODO: 2022/2/8 把java文件输出到硬盘
            String filepath = GPProxy.class.getResource("").getPath();
            //这里是为了删除我的中文乱码
            filepath="/home/rose/IdeaProjects/personStudy/spring/target/classes/com/代理/dongDJ/myJDKdong/";
            //先新建一个空文件
            File f = new File(filepath + "$Proxy0.java");
            FileWriter fw = new FileWriter(f);
            //把代码写入到上面的文件中
            fw.write(src);
            fw.flush();
            fw.close();

            // TODO: 2022/2/8 把生成好的java文件编译成.class 文件
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            //把上面创建的java文件编译成class文件
            Iterable<? extends JavaFileObject> iterable = manager.getJavaFileObjects(f);
            JavaCompiler.CompilationTask task=compiler.getTask(null,manager,null,null,null,iterable);
            task.call();
            manager.close();

            // TODO: 2022/2/8 把编译成的.class文件加载到JVM中
            Class proxyClass = classLoader.findClass("$Proxy0");
            Constructor constructor = proxyClass.getConstructor(GPInvocationHandler.class);
            //删除掉java文件
            f.delete();
            //生成一个实现了被代理类所实现的接口的类
            //此类对应的方法，执行我们自定义的GPInvocationHandler接口的invoke方法
            //接口会找到我们定义的实现了GPInvocationHandler接口的GPMeipo实例的invoke方法
            return constructor.newInstance(h);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    };

    private static String generateSrc(Class<?>[] interfaces){
        StringBuffer sb = new StringBuffer();
        //// TODO: 2022/2/8 就是在生成java文件的内容，下面是这个java文件需要导入的包
        sb.append("package com.代理.dongDJ.myJDKdong;"+ln);
        //需要加载外面的Person接口
        sb.append("import com.代理.dongDJ.Person;"+ln);
        //加载反射包下的所有方法
        sb.append("import java.lang.reflect.*;"+ln);

        //生成类名
        sb.append("public class $Proxy0 implements "+interfaces[0].getName()+"{"+ln);
            sb.append("GPInvocationHandler h;"+ln);
            sb.append("public $Proxy0(GPInvocationHandler h) {"+ln);
                sb.append("this.h=h;");
        sb.append("}"+ln);
        //遍历此接口的所有方法
        for (Method method : interfaces[0].getMethods()) {
            Class<?>[] params = method.getParameterTypes();
            StringBuffer paramNames = new StringBuffer();
            StringBuffer paramValues = new StringBuffer();
            StringBuffer paramClasses = new StringBuffer();

            for (int i = 0; i < params.length; i++) {
                Class clazz = params[i];
                String type = clazz.getName();
                String paramName = toLowerFirstCase(clazz.getSimpleName());
                paramNames.append(type+" "+paramName);
                paramValues.append(paramName);
                paramClasses.append(clazz.getName()+".class");

                if (i>0&&i<params.length-1){
                    paramNames.append(",");
                    paramClasses.append(",");
                    paramValues.append(",");
                }
            }

            //写方法第一行
            sb.append("public "+method.getReturnType().getName()+" "+method.getName()+"("+ paramNames.toString()+") {"+ln);
                sb.append("try{"+ln);
                    //通过反射来获取此方法原对象。
                    sb.append("Method m= "+interfaces[0].getName()+".class.getMethod(\""+method.getName()+"\",new Class[]{"+ paramClasses +"});"+ln);
                    //GPInvocationHandler让上面自定义的JDK动态代理来执行此方法，
                    // 同时执行实现了GPInvocationHandler接口的代理工具中的invoke方法
                    sb.append((hasReturnValue(method.getReturnType())?"return ":"")+getCaseCode("this.h.invoke(this,m,new Object[]{"+paramValues+"})",method.getReturnType())+";"+ln);
                sb.append("}catch(Error _ex) { }");
                sb.append("catch(Throwable e){"+ln);
                sb.append("throw new UndeclaredThrowableException(e);"+ln);
                sb.append("}");
                sb.append(getReturnEmptyCode(method.getReturnType()));
            sb.append("}");
        }
        sb.append("}"+ln);
        return sb.toString();
    }

    private static Map<Class,Class> mappings= new HashMap<Class,Class>();
    static {
        mappings.put(int.class,Integer.class);
    }

    //确认放回参数
    private static String getReturnEmptyCode(Class<?> returnClass){
        if (mappings.containsKey(returnClass)){
            return "return 0;";
        }else if (returnClass==void.class){
            return "";
        }else {
            return "return null;";
        }
    }

    private static String getCaseCode(String code,Class<?> returnClass){
        if (mappings.containsKey(returnClass)){
            return "(("+ mappings.get(returnClass).getName()+")"+code+")."+returnClass.getSimpleName()+"Value()";
        }
        return code;
    }

    //判断是否有返回值
    private static boolean hasReturnValue(Class<?> clazz){
        return clazz!=void.class;
    }
    //把首字母大小的字符串，首字母变成小写
    private static String toLowerFirstCase(String src){
        char[] chars = src.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }
}
