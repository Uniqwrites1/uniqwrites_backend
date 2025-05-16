package com.uniqwrites.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a public endpoint that anyone can access");
        response.put("status", "success");
        response.put("code", 200);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a protected endpoint that requires authentication");
        response.put("status", "success");
        response.put("code", 200);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/echo")
    public ResponseEntity<?> echoEndpoint(@RequestBody(required = false) Object body) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Echo endpoint received your request");
        response.put("receivedData", body);
        response.put("status", "success");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cors-test")
    public ResponseEntity<?> corsTestEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "CORS is working correctly!");
        response.put("status", "success");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    public ResponseEntity<?> echoRequest(@RequestBody Object requestBody) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Echo endpoint received your request");
        response.put("requestBody", requestBody);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}
