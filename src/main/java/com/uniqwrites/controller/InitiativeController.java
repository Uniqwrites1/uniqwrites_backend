package com.uniqwrites.controller;

import com.uniqwrites.dto.InitiativeDTO;
import com.uniqwrites.model.InitiativeSubmission;
import com.uniqwrites.service.InitiativeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/initiatives")
@Tag(name = "Initiatives", description = "Initiative related endpoints for sponsors and volunteers")
public class InitiativeController {

    @Autowired
    private InitiativeService initiativeService;

    @Operation(summary = "Submit initiative application (sponsor or volunteer)")
    @PostMapping("/submit")
    public ResponseEntity<?> submitInitiative(@RequestBody InitiativeDTO initiativeDTO) {
        try {
            InitiativeSubmission submission = initiativeService.submitInitiative(initiativeDTO);
            return ResponseEntity.ok(submission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while processing your submission");
        }
    }
}
