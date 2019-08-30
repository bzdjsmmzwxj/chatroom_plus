package com.wxj.util;

import com.wxj.client.entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.awt.datatransfer.StringSelection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class CommUtilsTest {

    @Test
    public void loadProperties() {
        String fileName = "datasource.properties";
        Properties properties = CommUtils.loadProperties(fileName);
        //System.out.println(properties);
        Assert.assertNotNull(properties);
    }

    @Test
    public void object2Json() {
        User user = new User();
        user.setId(1);
        user.setUserName("test");
        user.setPassword("123");
        user.setBrief("good");
        Set<String> strings = new HashSet<>();  //序列化是深拷贝
        strings.add("test2");
        strings.add("test3");
        strings.add("test4");
        user.setUserNames(strings);
        String str = CommUtils.object2Json(user);
        System.out.println(str);
    }

    @Test
    public void json2Object() {
        String str = "{\"id\":1,\"userName\":\"test\",\"password\":\"123\",\"brief\":\"good\",\"userNames\":[\"test4\",\"test2\",\"test3\"]}\n";
        User user = (User) CommUtils.json2Object(str,User.class);
        System.out.println(user);
    }
}