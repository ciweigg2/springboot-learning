package com.example.springbootwechatlogin.service;

import com.example.springbootwechatlogin.dto.LoginResponse;
import com.example.springbootwechatlogin.model.WechatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class WechatLoginService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public LoginResponse login(String loginType, WechatSession session) {
        Map<String, Object> wechatUser = findWechatUser(loginType, session.getOpenid());
        boolean newUser = false;
        Long wechatUserId;
        Long appUserId;

        if (wechatUser == null) {
            appUserId = createAppUser(session);
            wechatUserId = createWechatUser(appUserId, loginType, session);
            newUser = true;
        } else {
            wechatUserId = ((Number) wechatUser.get("id")).longValue();
            appUserId = ((Number) wechatUser.get("app_user_id")).longValue();
            updateWechatUser(wechatUserId, session);
            updateAppUser(appUserId, session);
        }

        String token = createLoginSession(appUserId, loginType);
        Map<String, Object> appUser = findAppUser(appUserId);

        LoginResponse response = new LoginResponse();
        response.setUserId(appUserId);
        response.setWechatUserId(wechatUserId);
        response.setOpenid(session.getOpenid());
        response.setUnionid(session.getUnionid());
        response.setNickname(String.valueOf(appUser.get("nickname")));
        response.setPhone(appUser.get("phone") == null ? null : String.valueOf(appUser.get("phone")));
        response.setToken(token);
        response.setNewUser(newUser);
        return response;
    }

    @Transactional
    public LoginResponse bindPhone(String token, String phone) {
        Map<String, Object> loginSession = findLoginSession(token);
        Long appUserId = ((Number) loginSession.get("app_user_id")).longValue();
        jdbcTemplate.update("UPDATE app_user SET phone = ?, update_time = CURRENT_TIMESTAMP WHERE id = ?", phone, appUserId);
        Map<String, Object> appUser = findAppUser(appUserId);

        LoginResponse response = new LoginResponse();
        response.setUserId(appUserId);
        response.setNickname(String.valueOf(appUser.get("nickname")));
        response.setPhone(String.valueOf(appUser.get("phone")));
        response.setToken(token);
        response.setNewUser(false);
        return response;
    }

    public Map<String, Object> me(String token) {
        Map<String, Object> loginSession = findLoginSession(token);
        Long appUserId = ((Number) loginSession.get("app_user_id")).longValue();
        return jdbcTemplate.queryForMap(
                "SELECT id, nickname, avatar_url, phone, status, create_time, update_time FROM app_user WHERE id = ?",
                appUserId);
    }

    private Long createAppUser(WechatSession session) {
        Long id = nextId();
        jdbcTemplate.update(
                "INSERT INTO app_user (id, nickname, avatar_url, status, create_time, update_time) VALUES (?, ?, ?, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                id,
                session.getNickname(),
                "https://example.com/avatar/" + id + ".png");
        return id;
    }

    private Long createWechatUser(Long appUserId, String loginType, WechatSession session) {
        Long id = nextId();
        jdbcTemplate.update(
                "INSERT INTO wechat_user (id, app_user_id, login_type, openid, unionid, session_key, access_token, nickname, create_time, update_time) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                id,
                appUserId,
                loginType,
                session.getOpenid(),
                session.getUnionid(),
                session.getSessionKey(),
                session.getAccessToken(),
                session.getNickname());
        return id;
    }

    private void updateWechatUser(Long id, WechatSession session) {
        jdbcTemplate.update(
                "UPDATE wechat_user SET unionid = ?, session_key = ?, access_token = ?, nickname = ?, update_time = CURRENT_TIMESTAMP WHERE id = ?",
                session.getUnionid(),
                session.getSessionKey(),
                session.getAccessToken(),
                session.getNickname(),
                id);
    }

    private void updateAppUser(Long id, WechatSession session) {
        jdbcTemplate.update(
                "UPDATE app_user SET nickname = ?, update_time = CURRENT_TIMESTAMP WHERE id = ?",
                session.getNickname(),
                id);
    }

    private String createLoginSession(Long appUserId, String loginType) {
        String token = UUID.randomUUID().toString().replace("-", "");
        jdbcTemplate.update(
                "INSERT INTO login_session (token, app_user_id, login_type, expire_time, create_time) VALUES (?, ?, ?, DATEADD('DAY', 7, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP)",
                token,
                appUserId,
                loginType);
        return token;
    }

    private Map<String, Object> findWechatUser(String loginType, String openid) {
        try {
            return jdbcTemplate.queryForMap(
                    "SELECT id, app_user_id, login_type, openid, unionid FROM wechat_user WHERE login_type = ? AND openid = ?",
                    loginType,
                    openid);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Map<String, Object> findAppUser(Long appUserId) {
        return jdbcTemplate.queryForMap("SELECT id, nickname, phone FROM app_user WHERE id = ?", appUserId);
    }

    private Map<String, Object> findLoginSession(String token) {
        return jdbcTemplate.queryForMap(
                "SELECT token, app_user_id, login_type FROM login_session WHERE token = ? AND expire_time > CURRENT_TIMESTAMP",
                token);
    }

    private Long nextId() {
        return jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR demo_id_seq", Long.class);
    }
}
