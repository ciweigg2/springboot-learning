package com.example.springbootsatoken;

import com.example.springbootsatoken.model.AuthUser;
import com.example.springbootsatoken.service.DemoUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootSatokenApplicationTests {

    @Autowired
    private DemoUserService demoUserService;

    @Test
    public void loginWithDatabaseRbac() {
        AuthUser admin = demoUserService.login("admin", "123456", "127.0.0.1");

        assertNotNull(admin);
        assertFalse(demoUserService.getRoleList(String.valueOf(admin.getId())).isEmpty());
        assertFalse(demoUserService.getPermissionList(String.valueOf(admin.getId())).isEmpty());
        assertNull(demoUserService.login("disabled", "123456", "127.0.0.1"));
    }
}
