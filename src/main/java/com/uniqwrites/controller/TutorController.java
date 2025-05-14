package com.uniqwrites.controller;

import com.uniqwrites.dto.ApplicationRequestDTO;
import com.uniqwrites.dto.AvailabilityDTO;
import com.uniqwrites.model.TutoringRequest;
import com.uniqwrites.service.TutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutor")
@Tag(name = "Tutor", description = "Tutor related endpoints")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @Operation(summary = "Get tutor dashboard data")
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> getDashboard() {
        // TODO: Implement tutor dashboard data retrieval
        return ResponseEntity.ok("Tutor dashboard data");
    }

    @Operation(summary = "Get training status")
    @GetMapping("/training-status")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> getTrainingStatus() {
        // TODO: Implement training status retrieval
        return ResponseEntity.ok("Training status");
    }

    @Operation(summary = "Get available tutoring jobs")
    @GetMapping("/jobs")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> getAvailableRequests() {
        // TODO: Implement retrieval of open tutoring requests
        return ResponseEntity.ok("List of open tutoring requests");
    }    @Operation(summary = "Apply for a tutoring job")
    @PostMapping("/apply")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> applyForJob(@RequestBody ApplicationRequestDTO request) {
        tutorService.applyForJob(request);
        return ResponseEntity.ok("Application submitted");
    }

    @Operation(summary = "Get assigned students")
    @GetMapping("/assigned-students")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> getAssignedStudents() {
        // TODO: Implement retrieval of assigned students
        return ResponseEntity.ok("List of assigned students");
    }    @Operation(summary = "Update tutor availability")
    @PutMapping("/settings")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> updateAvailability(@RequestBody AvailabilityDTO availability) {
        tutorService.updateAvailability(availability);
        return ResponseEntity.ok("Updated availability");
    }

    @Operation(summary = "Get tutor resources")
    @GetMapping("/resources")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> getResources() {
        // TODO: Implement retrieval of tutor resources
        return ResponseEntity.ok("Tutor resources");
    }

    @Operation(summary = "Get tutor feedback")
    @GetMapping("/feedback")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> getFeedback() {
        // TODO: Implement retrieval of tutor feedback
        return ResponseEntity.ok("Tutor feedback");
    }

    @Operation(summary = "Get tutor earnings")
    @GetMapping("/earnings")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> getEarnings() {
        // TODO: Implement retrieval of tutor earnings
        return ResponseEntity.ok("Tutor earnings");
    }
}
