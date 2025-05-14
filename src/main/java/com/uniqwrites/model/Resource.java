package com.uniqwrites.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // FILE, YOUTUBE_LINK, etc.

    @Column(nullable = false)
    private String url; // File path or YouTube URL

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    private Long jobId; // Optional - if resource is associated with a specific job

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
