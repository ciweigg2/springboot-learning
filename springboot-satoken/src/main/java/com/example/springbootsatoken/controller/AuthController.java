package com.example.springbootsatoken.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.example.springbootsatoken.dto.LoginRequest;
import com.example.springbootsatoken.dto.LoginResponse;
import com.example.springbootsatoken.model.AuthUser;
import com.example.springbootsatoken.service.DemoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private DemoUserService demoUserService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        AuthUser user = demoUserService.login(request.getUsername(), request.getPassword(), getClientIp(servletRequest));
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password error, or user disabled");
        }
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        LoginResponse result = new LoginResponse();
        result.setUserId(user.getId());
        result.setUsername(user.getUsername());
        result.setNickname(user.getNickname());
        result.setTokenName(tokenInfo.getTokenName());
        result.setTokenValue(tokenInfo.getTokenValue());
        result.setRoles(demoUserService.getRoleList(String.valueOf(user.getId())));
        result.setPermissions(demoUserService.getPermissionList(String.valueOf(user.getId())));
        return result;
    }

    @PostMapping("/logout")
    public String logout() {
        StpUtil.logout();
        return "logout success";
    }

    @GetMapping("/is-login")
    public Boolean isLogin() {
        return StpUtil.isLogin();
    }

    @GetMapping("/token-info")
    public SaTokenInfo tokenInfo() {
        return StpUtil.getTokenInfo();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
