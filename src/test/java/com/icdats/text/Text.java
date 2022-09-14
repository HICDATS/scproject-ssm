package com.icdats.text;

import com.icdats.pojo.User;
import com.icdats.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class Text {
    @Autowired
    private UserService userService;
    @Test
    public void text(){
        User t0001 = userService.login("t0001", "123456");
        System.out.println(t0001);
    }
}
