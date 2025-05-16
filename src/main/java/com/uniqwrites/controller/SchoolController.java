package com.uniqwrites.controller;

import com.uniqwrites.dto.SchoolRequestDTO;
import com.uniqwrites.service.RequestFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    private RequestFormService requestFormService;

    @PostMapping("/request")
    @PreAuthorize("hasRole('SCHOOL_ADMIN')")
    public ResponseEntity<?> submitSchoolRequest(@RequestBody SchoolRequestDTO schoolRequestDTO) {
        requestFormService.handleSchoolRequest(schoolRequestDTO);
        return ResponseEntity.ok("School request submitted successfully");
    }
}
