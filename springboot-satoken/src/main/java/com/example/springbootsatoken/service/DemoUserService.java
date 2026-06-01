package com.example.springbootsatoken.service;

import com.example.springbootsatoken.model.AuthUser;
import com.example.springbootsatoken.util.PasswordUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class DemoUserService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<AuthUser> userRowMapper = (rs, rowNum) -> {
        AuthUser user = new AuthUser();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setNickname(rs.getString("nickname"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setPasswordSalt(rs.getString("password_salt"));
        user.setStatus(rs.getInt("status"));
        if (rs.getTimestamp("last_login_time") != null) {
            user.setLastLoginTime(rs.getTimestamp("last_login_time").toLocalDateTime());
        }
        return user;
    };

    public DemoUserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AuthUser login(String username, String password, String loginIp) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            saveLoginLog(username, loginIp, 0, "username or password is empty");
            return null;
        }
        AuthUser user = getByUsername(username);
        if (user == null) {
            saveLoginLog(username, loginIp, 0, "user not found");
            return null;
        }
        if (!Integer.valueOf(1).equals(user.getStatus())) {
            saveLoginLog(username, loginIp, 0, "user disabled");
            return null;
        }
        if (!PasswordUtils.matches(password, user.getPasswordSalt(), user.getPasswordHash())) {
            saveLoginLog(username, loginIp, 0, "password error");
            return null;
        }
        jdbcTemplate.update("UPDATE sys_user SET last_login_time = CURRENT_TIMESTAMP WHERE id = ?", user.getId());
        saveLoginLog(username, loginIp, 1, "login success");
        return user;
    }

    public AuthUser getById(String userId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, username, nickname, password_hash, password_salt, status, last_login_time FROM sys_user WHERE id = ? AND deleted = 0",
                    userRowMapper,
                    Long.valueOf(userId));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<String> getPermissionList(String userId) {
        AuthUser user = getById(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        return jdbcTemplate.queryForList(
                "SELECT DISTINCT p.permission_code " +
                        "FROM sys_permission p " +
                        "JOIN sys_role_permission rp ON rp.permission_id = p.id " +
                        "JOIN sys_user_role ur ON ur.role_id = rp.role_id " +
                        "JOIN sys_role r ON r.id = ur.role_id " +
                        "WHERE ur.user_id = ? AND p.status = 1 AND r.status = 1 " +
                        "ORDER BY p.permission_code",
                String.class,
                Long.valueOf(userId));
    }

    public List<String> getRoleList(String userId) {
        AuthUser user = getById(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        return jdbcTemplate.queryForList(
                "SELECT DISTINCT r.role_code " +
                        "FROM sys_role r " +
                        "JOIN sys_user_role ur ON ur.role_id = r.id " +
                        "WHERE ur.user_id = ? AND r.status = 1 " +
                        "ORDER BY r.role_code",
                String.class,
                Long.valueOf(userId));
    }

    private AuthUser getByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, username, nickname, password_hash, password_salt, status, last_login_time FROM sys_user WHERE username = ? AND deleted = 0",
                    userRowMapper,
                    username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private void saveLoginLog(String username, String loginIp, int success, String message) {
        jdbcTemplate.update(
                "INSERT INTO sys_login_log (username, login_ip, success, message, create_time) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)",
                username,
                loginIp,
                success,
                message);
    }
}
