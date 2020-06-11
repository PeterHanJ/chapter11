package com.smart.withouttx.jdbc;

import javafx.application.Application;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service("userService")
public class UserJdbcWithoutTransManagerService {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addCredit(String userName, int toAdd) {
        String sql = "update t_user set credits = credits + ? where user_name = ?";
        jdbcTemplate.update(sql, toAdd, userName);
    }

    public static void main(String[] args) throws UnknownHostException {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:jdbcWithoutTx.xml");
        UserJdbcWithoutTransManagerService userService = context.getBean("userService", UserJdbcWithoutTransManagerService.class);
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        BasicDataSource basicDataSource = (BasicDataSource) jdbcTemplate.getDataSource();

        //check datasource auto commit
        System.out.println("auto commit:" + basicDataSource.getDefaultAutoCommit());

        //insert a record, credit = 0
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String currentDateTime = format.format(date);
        String insertSql = "insert into t_user(user_name,credits,password,last_visit,last_ip) " +
                "values ('tom',0,'123456','" + currentDateTime + "','" + Inet4Address.getLocalHost().getHostAddress() + "')";
        jdbcTemplate.execute(insertSql);

        //call no transaction method
        userService.addCredit("tom",10);

        //query the credits
        int credits = jdbcTemplate.queryForObject("select credits from t_user where user_name='tom'",Integer.class);
        System.out.println("credits : " + credits);

        //delete record
        jdbcTemplate.execute("delete from t_user where user_name='tom'");


    }
}
