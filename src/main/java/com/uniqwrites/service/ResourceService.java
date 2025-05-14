package com.uniqwrites.service;

import com.uniqwrites.dto.ResourceUploadDTO;
import com.uniqwrites.model.Resource;
import com.uniqwrites.model.User;
import com.uniqwrites.repository.ResourceRepository;
import com.uniqwrites.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ResourceService {

    @Value("${app.upload.dir:${user.home}/uniqwrites/uploads}")
    private String uploadDir;

    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.ms-powerpoint",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "text/plain",
        "image/jpeg",
        "image/png"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    public Resource uploadResource(ResourceUploadDTO dto) throws IOException {
        // Get current user
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalStateException("User not found"));

        if (dto.getType().equals("FILE")) {
            validateFile(dto.getFile());
            return saveFileResource(dto, user);
        } else if (dto.getType().equals("YOUTUBE_LINK")) {
            validateYoutubeUrl(dto.getYoutubeUrl());
            return saveYoutubeResource(dto, user);
        } else {
            throw new IllegalArgumentException("Invalid resource type");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String contentType = file.getContentType();
        if (!ALLOWED_FILE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("File type not allowed. Allowed types: PDF, Word, PowerPoint, Text, JPEG, PNG");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 10MB");
        }
    }

    private void validateYoutubeUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("YouTube URL is required");
        }

        String youtubeUrlPattern = "^(https?://)?(www\\.)?(youtube\\.com/watch\\?v=|youtu\\.be/)[A-Za-z0-9_-]{11}$";
        if (!url.matches(youtubeUrlPattern)) {
            throw new IllegalArgumentException("Invalid YouTube URL format");
        }
    }

    private Resource saveFileResource(ResourceUploadDTO dto, User user) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(dto.getFile().getOriginalFilename());
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(dto.getFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Resource resource = new Resource();
        resource.setName(dto.getFile().getOriginalFilename());
        resource.setType("FILE");
        resource.setUrl(fileName);
        resource.setDescription(dto.getDescription());
        resource.setUploadedBy(user);
        resource.setJobId(dto.getJobId());

        return resourceRepository.save(resource);
    }

    private Resource saveYoutubeResource(ResourceUploadDTO dto, User user) {
        Resource resource = new Resource();
        resource.setName("YouTube Video");
        resource.setType("YOUTUBE_LINK");
        resource.setUrl(dto.getYoutubeUrl());
        resource.setDescription(dto.getDescription());
        resource.setUploadedBy(user);
        resource.setJobId(dto.getJobId());

        return resourceRepository.save(resource);
    }

    public List<Resource> getResourcesByJobId(Long jobId) {
        return resourceRepository.findByJobId(jobId);
    }

    public List<Resource> getResourcesByUser(Long userId) {
        return resourceRepository.findByUploadedById(userId);
    }

    public void deleteResource(Long resourceId) throws IOException {
        Resource resource = resourceRepository.findById(resourceId)
            .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        if (resource.getType().equals("FILE")) {
            Path filePath = Paths.get(uploadDir).resolve(resource.getUrl());
            Files.deleteIfExists(filePath);
        }

        resourceRepository.delete(resource);
    }
}
