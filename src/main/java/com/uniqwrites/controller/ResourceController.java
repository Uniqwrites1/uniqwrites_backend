package com.uniqwrites.controller;

import com.uniqwrites.dto.ResourceUploadDTO;
import com.uniqwrites.model.Resource;
import com.uniqwrites.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Resources", description = "Resource management endpoints")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Operation(summary = "Upload a new resource (file or YouTube link)")
    @PostMapping(value = "/teacher/resources/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> uploadResource(
            @RequestPart(required = false) MultipartFile file,
            @RequestParam String type,
            @RequestParam(required = false) String youtubeUrl,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long jobId) {
        try {
            ResourceUploadDTO dto = new ResourceUploadDTO();
            dto.setFile(file);
            dto.setType(type);
            dto.setYoutubeUrl(youtubeUrl);
            dto.setDescription(description);
            dto.setJobId(jobId);

            Resource resource = resourceService.uploadResource(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error uploading resource: " + e.getMessage());
        }
    }

    @Operation(summary = "Get resources by job ID")
    @GetMapping("/resources/job/{jobId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT', 'ADMIN')")
    public ResponseEntity<List<Resource>> getResourcesByJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(resourceService.getResourcesByJobId(jobId));
    }

    @Operation(summary = "Get resources by user ID")
    @GetMapping("/resources/user/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<List<Resource>> getResourcesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(resourceService.getResourcesByUser(userId));
    }

    @Operation(summary = "Delete a resource")
    @DeleteMapping("/resources/{resourceId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> deleteResource(@PathVariable Long resourceId) {
        try {
            resourceService.deleteResource(resourceId);
            return ResponseEntity.ok("Resource deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting resource: " + e.getMessage());
        }    }
}
