package com.desh.learnSpringSecurity.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.desh.learnSpringSecurity.model.Users;
import com.desh.learnSpringSecurity.service.UsersService;

@RestController
public class UserController {

    @Autowired
    UsersService service;

    @GetMapping("/users")
    public List<Users> getUser() {
        return service.getUsers();
    }

    @PostMapping("/users")
    public Users register(@RequestBody Users user) {
        return service.addUser(user);
    }
}
