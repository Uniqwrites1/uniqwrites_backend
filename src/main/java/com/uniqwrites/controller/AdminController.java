package com.uniqwrites.controller;

import com.uniqwrites.dto.AssignTutorDTO;
import com.uniqwrites.model.User;
import com.uniqwrites.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        // TODO: Implement retrieval of all users
        return ResponseEntity.ok(null);
    }

    @PostMapping("/assign-tutor")
    public ResponseEntity<?> assignTutor(@RequestBody AssignTutorDTO assignTutorDTO) {
        // TODO: Implement tutor assignment logic
        return ResponseEntity.ok("Tutor assigned to student");
    }

    @GetMapping("/review-requests")
    public ResponseEntity<?> reviewRequests() {
        // TODO: Implement retrieval of all pending service requests
        return ResponseEntity.ok("All pending service requests");
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // TODO: Implement user deletion logic
        return ResponseEntity.ok("User deleted");
    }
}
