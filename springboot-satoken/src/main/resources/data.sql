INSERT INTO sys_user (id, username, nickname, password_hash, password_salt, status, deleted) VALUES
(1, 'admin', '系统管理员', '8a83127d9caf2d1f8cf2055680688b4c9cb1a6fa51a167a0e5a08d70c3b0279c', 'satoken-demo-admin', 1, 0),
(2, 'user', '普通用户', 'c7a6c79d37ef24083b88a04e121f6526773fe87c1f71a59413350f340d293e4d', 'satoken-demo-user', 1, 0),
(3, 'disabled', '停用用户', '2297da3e9ae5a28fce5d8f948af2c9f8525b940b17edd7ba2416d6658b4765c4', 'satoken-demo-disabled', 0, 0);

INSERT INTO sys_role (id, role_code, role_name, status) VALUES
(1, 'admin', '管理员', 1),
(2, 'user', '普通用户', 1),
(3, 'auditor', '审计员', 1);

INSERT INTO sys_permission (id, permission_code, permission_name, resource_type, path, status) VALUES
(1, 'user:list', '用户列表', 'api', 'GET /permission/users', 1),
(2, 'user:add', '新增用户', 'api', 'POST /permission/users', 1),
(3, 'user:update', '修改用户', 'api', 'PUT /permission/users/{id}', 1),
(4, 'user:delete', '删除用户', 'api', 'DELETE /permission/users/{id}', 1),
(5, 'order:list', '订单列表', 'api', 'GET /permission/manual-check', 1),
(6, 'security:kickout', '踢人下线', 'api', 'POST /permission/kickout/{userId}', 1);

INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 2);

INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(2, 1),
(2, 5);
