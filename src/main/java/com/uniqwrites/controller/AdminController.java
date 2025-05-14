package com.uniqwrites.controller;

import com.uniqwrites.dto.AssignTutorDTO;
import com.uniqwrites.model.User;
import com.uniqwrites.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin related endpoints")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        // Implement retrieval of all users
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Assign tutor to student")
    @PostMapping("/assign-job")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignTutor(@RequestBody AssignTutorDTO assignTutorDTO) {
        // Implement tutor assignment logic
        adminService.assignTutor(assignTutorDTO);
        return ResponseEntity.ok("Tutor assigned to student");
    }

    @Operation(summary = "Review all pending service requests")
    @GetMapping("/requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reviewRequests() {
        // Implement retrieval of all pending service requests
        Object requests = adminService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }

    @Operation(summary = "Delete a user by ID")
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Implement user deletion logic
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

    @Operation(summary = "Approve a teacher")
    @PostMapping("/approve-teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveTeacher(@RequestBody Long teacherId) {
        // TODO: Implement teacher approval logic
        return ResponseEntity.ok("Teacher approved");
    }

    @Operation(summary = "Get admin dashboard data")
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getDashboard() {
        // TODO: Implement admin dashboard data retrieval
        return ResponseEntity.ok("Admin dashboard data");
    }

    @Operation(summary = "Get payments")
    @GetMapping("/payments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPayments() {
        // TODO: Implement payments retrieval
        return ResponseEntity.ok("Payments data");
    }

    @Operation(summary = "Get feedback")
    @GetMapping("/feedback")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFeedback() {
        // TODO: Implement feedback retrieval
        return ResponseEntity.ok("Feedback data");
    }

    @Operation(summary = "Post an initiative")
    @PostMapping("/post-initiative")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> postInitiative(@RequestBody String initiative) {
        // TODO: Implement posting initiative
        return ResponseEntity.ok("Initiative posted");
    }

    @Operation(summary = "Update user by ID")
    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        // TODO: Implement user update logic
        return ResponseEntity.ok("User updated");
    }
}
