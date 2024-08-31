package com.desh.learnSpringSecurity.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.desh.learnSpringSecurity.model.Users;
import com.desh.learnSpringSecurity.repo.UserRepo;

@Service
public class UsersService {

    @Autowired
    UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<Users> getUsers() {
        return repo.findAll();
    }

    public Users addUser(Users user) {

        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

}
