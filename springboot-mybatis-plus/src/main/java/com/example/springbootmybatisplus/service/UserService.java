package com.example.springbootmybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmybatisplus.dto.UserCreateRequest;
import com.example.springbootmybatisplus.dto.UserUpdateRequest;
import com.example.springbootmybatisplus.entity.User;
import com.example.springbootmybatisplus.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public User create(UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());
        user.setCreateTime(LocalDateTime.now());
        save(user);
        return user;
    }

    public User update(Long id, UserUpdateRequest request) {
        User user = getById(id);
        if (user == null) {
            return null;
        }
        user.setUsername(request.getUsername());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());
        updateById(user);
        return user;
    }

    public IPage<User> pageUsers(long pageNum, long pageSize, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .like(StringUtils.hasText(keyword), User::getUsername, keyword)
                .or(StringUtils.hasText(keyword), item -> item.like(User::getEmail, keyword))
                .orderByDesc(User::getId);
        return page(new Page<User>(pageNum, pageSize), wrapper);
    }

    public List<User> listAdults() {
        return list(new LambdaQueryWrapper<User>()
                .ge(User::getAge, 18)
                .orderByAsc(User::getAge));
    }
}
