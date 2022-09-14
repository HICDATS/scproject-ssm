package com.icdats.text;

import com.icdats.mapper.CourseMapper;
import com.icdats.pojo.User;
import com.icdats.service.CourseService;
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
    private CourseMapper courseMapper;
    @Test
    public void text(){
        Integer hao = courseMapper.getCidByCname("hao");
        System.out.println(hao);
    }
}
