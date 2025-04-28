package com.uniqwrites.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sponsor")
public class SponsorController {

    @PostMapping("/donate")
    public ResponseEntity<?> donate(@RequestBody Object donationRequest) {
        // TODO: Implement donation processing logic
        return ResponseEntity.ok("Donation received");
    }
}
