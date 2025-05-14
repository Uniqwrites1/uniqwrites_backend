package com.uniqwrites.repository;

import com.uniqwrites.model.InitiativeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InitiativeSubmissionRepository extends JpaRepository<InitiativeSubmission, Long> {
    List<InitiativeSubmission> findByInitiativeName(String initiativeName);
    List<InitiativeSubmission> findBySubmissionType(String submissionType);
}
