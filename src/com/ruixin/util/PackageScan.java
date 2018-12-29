package com.ruixin.util;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.ruixin.config.HandleException;
/**
 * @Author:ruixin
 * @Date: 2018年11月12日 下午2:10:12
 * @Description:包扫描
 */
public class PackageScan {
	
	public List<Class<?>> classes=new ArrayList<Class<?>>();
	public static PackageScan instance=new PackageScan();
	
	private PackageScan(){}
	
	 /**
     * 通过包名获取文件夹下所有的class对象
     * @param pack
     */
    public void packageScan(String pack) {
        String packageDirName = pack.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    getClassFile(pack, filePath, true, classes);
                }
            }
        } catch (Exception e) {
        	HandleException.getInstance().handler(e);
        }
    }

    /**
     * @param packageName 包名
     * @param filePath 路径
     * @param recursive 是否遍历子目录
     * @param classes 
     * @Description: 获取路径下所有class文件
     */
    private void getClassFile(String packageName, String filePath, final boolean recursive,List<Class<?>> classes) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 在给定的目录下找到所有的文件，并且进行条件过滤
        File[] dirFiles = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
                boolean acceptClass = file.getName().endsWith("class");// 接受class文件
                return acceptDir || acceptClass;
            }
        });

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                getClassFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                	classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (Exception e) {
                	HandleException.getInstance().handler(e);
                }
            }
        }
    }
}
