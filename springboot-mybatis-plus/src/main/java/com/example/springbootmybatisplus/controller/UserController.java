package com.example.springbootmybatisplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.springbootmybatisplus.dto.UserCreateRequest;
import com.example.springbootmybatisplus.dto.UserUpdateRequest;
import com.example.springbootmybatisplus.entity.User;
import com.example.springbootmybatisplus.mapper.UserMapper;
import com.example.springbootmybatisplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping
    public User create(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public IPage<User> page(@RequestParam(defaultValue = "1") long pageNum,
                            @RequestParam(defaultValue = "10") long pageSize,
                            @RequestParam(required = false) String keyword) {
        return userService.pageUsers(pageNum, pageSize, keyword);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return userService.removeById(id);
    }

    @GetMapping("/adults")
    public List<User> listAdults() {
        return userService.listAdults();
    }

    @GetMapping("/wrapper-demo")
    public List<User> wrapperDemo(@RequestParam(defaultValue = "18") Integer minAge) {
        return userMapper.selectList(new QueryWrapper<User>()
                .select("id", "username", "age", "email")
                .ge("age", minAge)
                .orderByDesc("id"));
    }
}
