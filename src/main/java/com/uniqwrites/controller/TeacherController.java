package com.uniqwrites.controller;

import com.uniqwrites.dto.TeacherApplyDTO;
import com.uniqwrites.service.RequestFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private RequestFormService requestFormService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> applyAsTeacher(@RequestBody TeacherApplyDTO teacherApplyDTO) {
        requestFormService.handleTeacherApplication(teacherApplyDTO);
        return ResponseEntity.ok("Teacher application submitted successfully");
    }
}
