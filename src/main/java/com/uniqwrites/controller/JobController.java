package com.uniqwrites.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @PostMapping("/{id}/apply")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> applyForJob(@PathVariable Long id, @RequestBody Object application) {
        // Implementation to be added
        return ResponseEntity.ok("Applied for job " + id);
    }
}
