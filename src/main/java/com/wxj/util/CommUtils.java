package com.wxj.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 封装公共工具方法，如加载配置文件、json序列化等
 */
public class CommUtils {
    //创建Json的格式
    private static final Gson GSON = new GsonBuilder().create();
    /**
     * 加载配置文件
     * @param fileName 要加载的配置文件名称
     * @return
     */
    //ctrl+shift+T -单元测试
    public static Properties loadProperties(String fileName){
        Properties properties = new Properties();
        InputStream in = CommUtils.class.getClassLoader()
                .getResourceAsStream(fileName);
        try {
            properties.load(in);
        } catch (IOException e) {
            return null;
        }
        return properties;
    }

    /**
     * 将任意对象序列化为json 字符串
     * @param object
     * @return
     */
    public static String object2Json(Object object){
        return GSON.toJson(object);
    }

    /**
     * 将任意json字符串反序列化为对象
     * @param jsonStr json字符串
     * @param objClass 反序列化的类反射对象
     * @return
     */
    public static Object json2Object(String jsonStr,Class objClass){
        return GSON.fromJson(jsonStr,objClass);
    }
}
