package com.example.springbootmybatisplus;

import com.example.springbootmybatisplus.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMybatisPlusApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        assertFalse(userService.list().isEmpty());
    }
}
