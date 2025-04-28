package com.uniqwrites.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Uniqwrites Platform!";
    }

    @GetMapping("/api/public/test")
    public String publicApi() {
        return "This is a public API!";
    }

    @GetMapping("/api/private/test")
    public String privateApi() {
        return "This is a secured API, login required.";
    }
}
