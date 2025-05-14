package com.uniqwrites.repository;

import com.uniqwrites.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByJobId(Long jobId);
    List<Resource> findByUploadedById(Long userId);
    List<Resource> findByType(String type);
}
