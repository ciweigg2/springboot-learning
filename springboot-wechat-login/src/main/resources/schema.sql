DROP TABLE IF EXISTS login_session;
DROP TABLE IF EXISTS wechat_user;
DROP TABLE IF EXISTS app_user;
DROP SEQUENCE IF EXISTS demo_id_seq;

CREATE SEQUENCE demo_id_seq START WITH 1000 INCREMENT BY 1;

CREATE TABLE app_user (
    id BIGINT PRIMARY KEY,
    nickname VARCHAR(64),
    avatar_url VARCHAR(255),
    phone VARCHAR(32),
    status TINYINT NOT NULL DEFAULT 1,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wechat_user (
    id BIGINT PRIMARY KEY,
    app_user_id BIGINT NOT NULL,
    login_type VARCHAR(32) NOT NULL,
    openid VARCHAR(128) NOT NULL,
    unionid VARCHAR(128),
    session_key VARCHAR(255),
    access_token VARCHAR(255),
    nickname VARCHAR(64),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (login_type, openid)
);

CREATE TABLE login_session (
    token VARCHAR(64) PRIMARY KEY,
    app_user_id BIGINT NOT NULL,
    login_type VARCHAR(32) NOT NULL,
    expire_time TIMESTAMP NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
