package com.uniqwrites.controller;

import com.uniqwrites.dto.TutoringRequestDTO;
import com.uniqwrites.dto.UpdateProfileDTO;
import com.uniqwrites.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@Tag(name = "Student", description = "Student (Parent) related endpoints")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Operation(summary = "Get parent dashboard data")
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getDashboard() {
        // TODO: Implement parent dashboard data retrieval
        return ResponseEntity.ok("Parent dashboard data");
    }

    @Operation(summary = "Book a service")
    @PostMapping("/book-service")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> requestTutor(@RequestBody TutoringRequestDTO request) {
        // Implement tutoring request submission logic
        studentService.submitTutoringRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tutoring request submitted");
    }

    @Operation(summary = "Get progress report")
    @GetMapping("/progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getProgress() {
        // Implement progress report retrieval logic
        String report = studentService.getProgressReport();
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Get feedback")
    @GetMapping("/feedback")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getFeedback() {
        // TODO: Implement feedback retrieval
        return ResponseEntity.ok("Feedback data");
    }

    @Operation(summary = "Get payments")
    @GetMapping("/payments")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getPayments() {
        // TODO: Implement payments retrieval
        return ResponseEntity.ok("Payments data");
    }

    @Operation(summary = "Update parent settings")
    @PutMapping("/settings")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDTO updatedFields) {
        // Implement profile update logic
        studentService.updateProfile(updatedFields);
        return ResponseEntity.ok("Profile updated");
    }
}
