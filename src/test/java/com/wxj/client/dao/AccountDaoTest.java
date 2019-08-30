package com.wxj.client.dao;

import com.wxj.client.entity.User;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountDaoTest {
    private AccountDao accountDao = new AccountDao();

    @Test
    public void userReg() {
        User user = new User();
        user.setUserName("王雪皎");
        user.setPassword("111111");
        user.setBrief("hello world");
        boolean flag = accountDao.userReg(user);
        Assert.assertTrue(flag);
}

    @Test
    public void userLogin() {
        String userName = "王雪皎";
        String password = "111111";
        User user = accountDao.userLogin(userName,password);
        System.out.println(user);
        Assert.assertNotNull(user);
    }
}