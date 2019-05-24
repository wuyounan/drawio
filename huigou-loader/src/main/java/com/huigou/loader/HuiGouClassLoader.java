package com.huigou.loader;

import org.apache.catalina.loader.WebappClassLoader;

public class HuiGouClassLoader extends WebappClassLoader {

    public HuiGouClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        return HuiGouNative.findClass(this, name);
    }

}
