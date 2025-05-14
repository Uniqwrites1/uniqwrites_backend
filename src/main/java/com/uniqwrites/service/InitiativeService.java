package com.uniqwrites.service;

import com.uniqwrites.dto.InitiativeDTO;
import com.uniqwrites.model.InitiativeSubmission;
import com.uniqwrites.repository.InitiativeSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InitiativeService {

    @Autowired
    private InitiativeSubmissionRepository initiativeSubmissionRepository;

    @Autowired
    private EmailService emailService; // Assuming you have an EmailService

    @Transactional
    public InitiativeSubmission submitInitiative(InitiativeDTO dto) {
        validateSubmission(dto);

        InitiativeSubmission submission = new InitiativeSubmission();
        submission.setInitiativeName(dto.getInitiativeName());
        submission.setSubmissionType(dto.getSubmissionType());
        submission.setFullName(dto.getFullName());
        submission.setEmail(dto.getEmail());
        submission.setPhone(dto.getPhone());
        submission.setMessage(dto.getMessage());
        submission.setSponsorshipType(dto.getSponsorshipType());
        submission.setSkills(dto.getSkills());
        submission.setAvailability(dto.getAvailability());

        InitiativeSubmission saved = initiativeSubmissionRepository.save(submission);
        
        // Send confirmation email
        sendConfirmationEmail(saved);
        
        return saved;
    }

    private void validateSubmission(InitiativeDTO dto) {
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (dto.getEmail() == null || !dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (dto.getSubmissionType() == null || 
            (!dto.getSubmissionType().equals("SPONSOR") && !dto.getSubmissionType().equals("VOLUNTEER"))) {
            throw new IllegalArgumentException("Valid submission type (SPONSOR or VOLUNTEER) is required");
        }
        if (dto.getInitiativeName() == null || dto.getInitiativeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Initiative name is required");
        }

        // Specific validations based on submission type
        if (dto.getSubmissionType().equals("SPONSOR")) {
            if (dto.getSponsorshipType() == null || 
                (!dto.getSponsorshipType().equals("One-time") && !dto.getSponsorshipType().equals("Recurring"))) {
                throw new IllegalArgumentException("Valid sponsorship type is required for sponsors");
            }
        } else { // VOLUNTEER
            if (dto.getSkills() == null || dto.getSkills().trim().isEmpty()) {
                throw new IllegalArgumentException("Skills are required for volunteers");
            }
            if (dto.getAvailability() == null || dto.getAvailability().trim().isEmpty()) {
                throw new IllegalArgumentException("Availability information is required for volunteers");
            }
        }
    }

    private void sendConfirmationEmail(InitiativeSubmission submission) {
        String subject = "Thank you for supporting " + submission.getInitiativeName();
        String message = String.format(
            "Dear %s,\n\nThank you for your interest in %s as a %s. " +
            "We have received your submission and will contact you shortly.\n\n" +
            "Best regards,\nUniqwrites Team",
            submission.getFullName(),
            submission.getInitiativeName(),
            submission.getSubmissionType().toLowerCase()
        );
        
        emailService.sendEmail(submission.getEmail(), subject, message);
    }

    public List<InitiativeSubmission> getSubmissionsByType(String type) {
        return initiativeSubmissionRepository.findBySubmissionType(type);
    }

    public List<InitiativeSubmission> getSubmissionsByInitiative(String initiativeName) {
        return initiativeSubmissionRepository.findByInitiativeName(initiativeName);
    }
}
