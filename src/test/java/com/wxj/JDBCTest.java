package com.wxj;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.wxj.util.CommUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.Properties;

public class JDBCTest {
    private static DruidDataSource dataSource;
    static {
        Properties properties = CommUtils.loadProperties("datasource.properties");
        try {
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQuery(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            connection = (Connection) dataSource.getPooledConnection();
            String sql = "select * from user where username = ? and password = ?";
            statement = connection.prepareStatement(sql);
            String user = "test' --";
            String pass = "123";
            statement.setString(1,user);
            statement.setString(2,pass);
            resultSet = statement.executeQuery();
            /*while(resultSet.next()){
                int id = resultSet.getInt("id");
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String brief = resultSet.getString("brief");
                System.out.println("id为:"+id+",用户名为:"+userName+",密码为:"+password+",简介为:"+brief);
            }*/
            if(resultSet.next()){
                System.out.println("登录成功");
            }else {
                System.out.println("登录失败");
            }
        }catch (SQLException e){

        }finally{
            closeResources(connection,statement,resultSet);
        }
    }

    @Test
    public void testInsert(){
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection = (Connection) dataSource.getPooledConnection();
            String password = DigestUtils.md5Hex("123");
            String sql = "insert into user (username, password, brief) values (?,?,?)";
            statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,"test1");
            statement.setString(2,password);
            statement.setString(3,"good");
            int rows = statement.executeUpdate();
            Assert.assertEquals(1,rows);

        }catch (SQLException e){

        }finally {
            closeResources(connection,statement);
        }
    }

    @Test
    public void testLogin(){
        String userName = "test";   //登录成功
        //String userName = "test' or 1=1"; //登录成功 SQL注入
        String password = "123";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            connection = (Connection) dataSource.getPooledConnection();
            String sql = "select * from user where username = '"+userName+"' and password = '"+password+"'";
            //String sql = "select * from user where username = '"+userName+" and password = '"+password+"'";
            System.out.println(sql);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                System.out.println("登录成功");
            }else {
                System.out.println("登录失败");
            }
        }catch (SQLException e){

        }finally {
            closeResources(connection,statement,resultSet);
        }
    }

    public void closeResources(Connection connection, Statement statement){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeResources(Connection connection, Statement statement, ResultSet resultSet){
        closeResources(connection,statement);
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
