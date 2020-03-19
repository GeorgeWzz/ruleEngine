package com.wuzizhuo;


import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.Script;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wuzizhuo
 * @created: 2019/11/21.
 * @updater:
 * @description:
 */
public class GroovyExecutor {
    /**
     * 上下文环境
     */
    private GroovyClassLoader gcl = new GroovyClassLoader();
    /**
     * groovy生成的类缓存
     */
    private Map<String, GroovyCacheBO> classPool = new ConcurrentHashMap<String, GroovyCacheBO>();
//    private static Map<String, Class<?>> classPool = new ConcurrentHashMap<String, Class<?>>();

    public void cacheClass(String key, File groovyFile) throws IOException, IllegalAccessException, InstantiationException {
        Class<?> clazz = gcl.parseClass(groovyFile);
        GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
        classPool.put(key, new GroovyCacheBO(groovyObject, clazz));
    }

    /**
     * 考虑到groovy文件可能删除的问题，把新的fileKey中不存在的缓存去掉，以免造成影响
     *
     * @param newKeyList
     */
    public void clearNotExistsKey(List<String> newKeyList) {
        for (String key : classPool.keySet()) {
            if (newKeyList.contains(key)) {
                continue;
            }

            classPool.remove(key);
        }
    }

    /**
     * 从缓存中获取class执行
     */
    public Object runFunctionFromCache(String key, String methodName, Object... arguments) {
        GroovyCacheBO groovyCacheBO = classPool.get(key);
        if (groovyCacheBO == null) {
            throw new RuntimeException("缓存中获取不到对应的GroovyCacheBO，key: " + key);
        }
        GroovyObject groovyObject = groovyCacheBO.groovyObject;
        Object object = InvokerHelper.invokeMethod(groovyObject, methodName, arguments);
        // 可以使用job定时清理，主要怕加载过多的class，正常情况不需要clear
        gcl.clearCache();
        return object;
    }

//    /**
//     * 执行groovy脚本，使用自动md5的key缓存
//     */
//    public static Object runScript(Binding binding, String scriptStr) {
//        return runScript(null, binding, scriptStr);
//    }

//    /**
//     * 执行groovy函数
//     */
//    public static Object runFunction(String scriptStr, String methodName, Object... arguments) throws InstantiationException, IllegalAccessException {
//        Class<?> clazz = getGroovyClass(null, scriptStr);
//        GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
//        Object object = InvokerHelper.invokeMethod(groovyObject, methodName, arguments);
//        // 可以使用job定时清理，主要怕加载过多的class，正常情况不需要clear
//        gcl.clearCache();
//        return object;
//    }
//
//    /**
//     * 执行groovy脚本
//     */
//    public static Object runScript(String className, Binding binding, String scriptStr) {
//        Class<?> clazz = getGroovyClass(className, scriptStr);
//        Script script = InvokerHelper.createScript(clazz, binding);
//        Object object = script.run();
//        // 可以使用job定时清理，主要怕加载过多的class，正常情况不需要clear
//        gcl.clearCache();
//        return object;
//    }
//
//    private static Class<?> getGroovyClass(String className, String scriptStr) {
//        String classKey =
//                StringUtil.isEmpty(className) ? genClassName(scriptStr) : className;
//        /**
//         * 非常关键，classKey将作为groovy脚本编译后的name，java不允许className以数字开头
//         */
//        classKey = "c" + classKey;
//        Class<?> clazz = classKey == null ? null : classPool.get(classKey);
//        if (clazz == null) {
//            clazz = gcl.parseClass(scriptStr, classKey);
//            classPool.put(classKey, clazz);
//        }
//        return clazz;
//    }

//    /**
//     * md5作为脚本的key缓存
//     *
//     * @param scriptStr
//     * @return
//     */
//    private static String genClassName(String scriptStr) {
//        return DigestUtils.stringToMD5(scriptStr, "UTF-8");
//    }

    protected class GroovyCacheBO {
        GroovyObject groovyObject;
        Class<?> clazz;

        GroovyCacheBO(GroovyObject groovyObject, Class<?> clazz) {
            this.groovyObject = groovyObject;
            this.clazz = clazz;
        }
    }

}
