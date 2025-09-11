package org.example.springboot.classLoadChange;

import org.example.springboot.Application;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;

public class PluginClassLoader extends ClassLoader {


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        PluginClassLoader pluginClassLoader = new PluginClassLoader("/target/classes", Application.class.getClassLoader());
        Class<?> aClass = pluginClassLoader.loadClass("org.example.springboot.classLoadChange.String");
        Constructor<?> constructor = aClass.getConstructor(char[].class);
        org.example.springboot.classLoadChange.String string = (org.example.springboot.classLoadChange.String) constructor.newInstance(new char[]{'b', 'b', 'c'});
        string.bb();
    }
    private String pluginDir;
    private Map<String, Class<?>> loadedClasses = new HashMap<>();

    public PluginClassLoader(String pluginDir, ClassLoader parent) {
        super(parent);
        this.pluginDir = pluginDir;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // 优先从插件目录加载类
        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            try {
                clazz = findClass(name);
            } catch (ClassNotFoundException e) {
                // 如果插件目录中找不到，再委派给父加载器
                clazz = super.loadClass(name, resolve);
            }
        }
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 将类名转为文件路径
        String fileName = pluginDir + File.separator + name.replace('.', File.separatorChar) + ".class";
        File classFile = new File(fileName);
        if (!classFile.exists()) {
            throw new ClassNotFoundException("Class " + name + " not found in plugin directory.");
        }
        try {
            byte[] classBytes = Files.readAllBytes(classFile.toPath());
            Class<?> clazz = defineClass(name, classBytes, 0, classBytes.length);
            loadedClasses.put(name, clazz);
            return clazz;
        } catch (IOException e) {
            throw new ClassNotFoundException("Error reading class " + name, e);
        }
    }

    // 支持卸载插件：清空缓存，允许重新加载
    public void unloadPlugin() {
        loadedClasses.clear();
    }
}