package com.uniqwrites.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TUTOR', 'SCHOOL_ADMIN')")
    public ResponseEntity<?> getDashboard() {
        // Implementation will be role-specific
        return ResponseEntity.ok("Dashboard data");
    }
}
