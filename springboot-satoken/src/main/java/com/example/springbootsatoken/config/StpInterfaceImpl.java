package com.example.springbootsatoken.config;

import cn.dev33.satoken.stp.StpInterface;
import com.example.springbootsatoken.service.DemoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private DemoUserService demoUserService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return demoUserService.getPermissionList(String.valueOf(loginId));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return demoUserService.getRoleList(String.valueOf(loginId));
    }
}
