INSERT INTO app_user (id, nickname, avatar_url, phone, status) VALUES
(1, '已有小程序用户', 'https://example.com/avatar/1.png', '13800000001', 1);

INSERT INTO wechat_user (id, app_user_id, login_type, openid, unionid, session_key, nickname) VALUES
(2, 1, 'mini_program', 'mini-openid-demo-code', 'unionid-demo-code', 'mock-session-key-demo-code', '已有小程序用户');
