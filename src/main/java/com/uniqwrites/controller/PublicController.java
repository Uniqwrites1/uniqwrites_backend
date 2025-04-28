package com.uniqwrites.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping("/api/public/info")
    public String getInfo() {
        return "Uniqwrites Platform public info: services, mission, and vision.";
    }

    @GetMapping("/api/public/resources")
    public String getResources() {
        return "List of free educational resources.";
    }

    @GetMapping("/api/public/blogs")
    public String getBlogs() {
        return "List of blog posts.";
    }

    @GetMapping("/api/public/tutoring-info")
    public String getTutoringInfo() {
        return "Tutoring services overview.";
    }

    @GetMapping("/api/public/initiatives")
    public String getInitiatives() {
        return "List of initiatives.";
    }
}
