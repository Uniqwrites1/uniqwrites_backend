package com.uniqwrites.controller;

import com.uniqwrites.dto.ParentRequestDTO;
import com.uniqwrites.dto.TeacherApplyDTO;
import com.uniqwrites.dto.SchoolRequestDTO;
import com.uniqwrites.service.RequestFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request-form")
@Tag(name = "RequestForm", description = "Request form related endpoints")
public class RequestFormController {

    @Autowired
    private RequestFormService requestFormService;

    @Operation(summary = "Submit parent request")
    @PostMapping("/parent")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitParentRequest(@RequestBody ParentRequestDTO parentRequestDTO) {
        requestFormService.handleParentRequest(parentRequestDTO);
        return ResponseEntity.ok("Parent request submitted successfully");
    }

    @Operation(summary = "Submit teacher application")
    @PostMapping("/teacher")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> submitTeacherApplication(@RequestBody TeacherApplyDTO teacherApplyDTO) {
        requestFormService.handleTeacherApplication(teacherApplyDTO);
        return ResponseEntity.ok("Teacher application submitted successfully");
    }

    @Operation(summary = "Submit school request")
    @PostMapping("/school")
    @PreAuthorize("hasRole('SCHOOL_ADMIN')")
    public ResponseEntity<?> submitSchoolRequest(@RequestBody SchoolRequestDTO schoolRequestDTO) {
        requestFormService.handleSchoolRequest(schoolRequestDTO);
        return ResponseEntity.ok("School request submitted successfully");
    }
}
