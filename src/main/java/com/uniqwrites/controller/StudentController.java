package com.uniqwrites.controller;

import com.uniqwrites.dto.TutoringRequestDTO;
import com.uniqwrites.dto.UpdateProfileDTO;
import com.uniqwrites.model.TutoringRequest;
import com.uniqwrites.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/request-tutor")
    public ResponseEntity<?> requestTutor(@RequestBody TutoringRequestDTO request) {
        // TODO: Implement tutoring request submission logic
        return ResponseEntity.status(HttpStatus.CREATED).body("Tutoring request submitted");
    }

    @GetMapping("/progress")
    public ResponseEntity<?> getProgress() {
        // TODO: Implement progress report retrieval logic
        return ResponseEntity.ok("Progress report");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDTO updatedFields) {
        // TODO: Implement profile update logic
        return ResponseEntity.ok("Profile updated");
    }
}
