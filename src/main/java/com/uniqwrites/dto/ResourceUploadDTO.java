package com.uniqwrites.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResourceUploadDTO {
    private MultipartFile file;
    private String type;
    private String description;
    private String youtubeUrl;
    private Long jobId;
}
