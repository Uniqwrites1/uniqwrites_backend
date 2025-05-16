package com.uniqwrites.controller;

import com.uniqwrites.dto.ParentRequestDTO;
import com.uniqwrites.service.RequestFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private RequestFormService requestFormService;

    @PostMapping("/requests")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitParentRequest(@RequestBody ParentRequestDTO parentRequestDTO) {
        requestFormService.handleParentRequest(parentRequestDTO);
        return ResponseEntity.ok("Parent request submitted successfully");
    }
    
    @GetMapping("/resources/{teacherId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getTeacherResources(@PathVariable Long teacherId) {
        // Implementation to be added
        return ResponseEntity.ok("Resources for teacher " + teacherId);
    }
}
