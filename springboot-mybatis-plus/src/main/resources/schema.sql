DROP TABLE IF EXISTS demo_user;

CREATE TABLE demo_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL,
    age INT,
    email VARCHAR(128),
    create_time TIMESTAMP
);
