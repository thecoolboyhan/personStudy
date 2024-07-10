package com.代理.dongDJ.myJDKdong;

import java.io.*;

/**
 * 自定义类动态代理类加载器
 * @author rose
 */
public class GPClassLoader extends ClassLoader {

    //存放需要加载  的文件
    private File classPathFile;
    public GPClassLoader(){
        String path = GPClassLoader.class.getResource("").getPath();
//        String path = "/home/rose/IdeaProjects/personStudy/spring/target/classes/com/代理/dongDJ/myJDKdong";
        path="C:\\Users\\tom\\IdeaProjects\\personStudy\\spring\\src\\main\\java\\com\\代理\\dongDJ\\myJDKdong";
        //确定要新建文件的目录
        this.classPathFile=new File(path);
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        //通过刚刚编译的class文件地址，用自定义类加载器来加载那个class文件
        String className =GPClassLoader.class.getPackage().getName()+"."+name;
        if (classPathFile!=null){
            File classFile = new File(this.classPathFile, name.replaceAll("\\.", "/") + ".class");
            //如果文件存在
            if (classFile.exists()){
                FileInputStream in=null;
                ByteArrayOutputStream out= null;

                try {
                    in =new FileInputStream(classFile);
                    out=new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = in.read(bytes)) != -1) {
                        out.write(bytes,0,length);
                    }
                    return defineClass(className,out.toByteArray(),0,out.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (null!=in){
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (out !=null){
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}
