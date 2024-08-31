package com.desh.learnSpringSecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

    @GetMapping("/")
    public String greetPage(HttpServletRequest request) {
        return "Hello People" + request.getSession().getId();
    }
}
