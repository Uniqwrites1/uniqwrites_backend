package com.uniqwrites.controller;

import com.uniqwrites.dto.ParentRequestDTO;
import com.uniqwrites.dto.TeacherApplyDTO;
import com.uniqwrites.dto.SchoolRequestDTO;
import com.uniqwrites.service.RequestFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request-form")
public class RequestFormController {

    @Autowired
    private RequestFormService requestFormService;

    @PostMapping("/parent")
    public ResponseEntity<?> submitParentRequest(@RequestBody ParentRequestDTO parentRequestDTO) {
        requestFormService.handleParentRequest(parentRequestDTO);
        return ResponseEntity.ok("Parent request submitted successfully");
    }

    @PostMapping("/teacher")
    public ResponseEntity<?> submitTeacherApplication(@RequestBody TeacherApplyDTO teacherApplyDTO) {
        requestFormService.handleTeacherApplication(teacherApplyDTO);
        return ResponseEntity.ok("Teacher application submitted successfully");
    }

    @PostMapping("/school")
    public ResponseEntity<?> submitSchoolRequest(@RequestBody SchoolRequestDTO schoolRequestDTO) {
        requestFormService.handleSchoolRequest(schoolRequestDTO);
        return ResponseEntity.ok("School request submitted successfully");
    }
}
