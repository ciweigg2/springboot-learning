package com.example.springbootsatoken.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.example.springbootsatoken.model.AuthUser;
import com.example.springbootsatoken.service.DemoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public class PermissionDemoController {

    @Autowired
    private DemoUserService demoUserService;

    @GetMapping("/profile")
    public Map<String, Object> profile() {
        AuthUser user = demoUserService.getById(String.valueOf(StpUtil.getLoginId()));
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("loginId", StpUtil.getLoginId());
        result.put("username", user == null ? null : user.getUsername());
        result.put("nickname", user == null ? null : user.getNickname());
        result.put("lastLoginTime", user == null ? null : user.getLastLoginTime());
        result.put("roles", StpUtil.getRoleList());
        result.put("permissions", StpUtil.getPermissionList());
        return result;
    }

    @GetMapping("/users")
    @SaCheckPermission("user:list")
    public String listUsers() {
        return "user:list permission pass";
    }

    @PostMapping("/users")
    @SaCheckPermission("user:add")
    public String addUser() {
        return "user:add permission pass";
    }

    @GetMapping("/admin")
    @SaCheckRole("admin")
    public String adminOnly() {
        return "admin role pass";
    }

    @PostMapping("/kickout/{userId}")
    @SaCheckRole("admin")
    @SaCheckPermission("security:kickout")
    public String kickout(@PathVariable String userId) {
        StpUtil.kickout(userId);
        return "kickout userId=" + userId;
    }

    @GetMapping("/manual-check")
    public String manualCheck() {
        StpUtil.checkPermission("order:list");
        return "manual order:list permission pass";
    }
}
