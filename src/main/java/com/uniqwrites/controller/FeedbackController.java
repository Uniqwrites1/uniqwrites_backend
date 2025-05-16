package com.uniqwrites.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitFeedback(@RequestBody Object feedbackRequest) {
        // Implementation to be added
        return ResponseEntity.ok("Feedback submitted successfully");
    }
}
