package com.uniqwrites.controller;

import com.uniqwrites.dto.ApplicationRequestDTO;
import com.uniqwrites.dto.AvailabilityDTO;
import com.uniqwrites.model.TutoringRequest;
import com.uniqwrites.service.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutor")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @GetMapping("/available-requests")
    public ResponseEntity<?> getAvailableRequests() {
        // TODO: Implement retrieval of open tutoring requests
        return ResponseEntity.ok("List of open tutoring requests");
    }

    @PostMapping("/apply-for-job")
    public ResponseEntity<?> applyForJob(@RequestBody ApplicationRequestDTO request) {
        // TODO: Implement application submission logic
        return ResponseEntity.ok("Application submitted");
    }

    @GetMapping("/assigned-students")
    public ResponseEntity<?> getAssignedStudents() {
        // TODO: Implement retrieval of assigned students
        return ResponseEntity.ok("List of assigned students");
    }

    @PutMapping("/update-availability")
    public ResponseEntity<?> updateAvailability(@RequestBody AvailabilityDTO availability) {
        // TODO: Implement availability update logic
        return ResponseEntity.ok("Updated availability");
    }
}
