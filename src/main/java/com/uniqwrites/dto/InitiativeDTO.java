package com.uniqwrites.dto;

import lombok.Data;

@Data
public class InitiativeDTO {
    private String initiativeName;
    private String submissionType; // SPONSOR or VOLUNTEER
    private String fullName;
    private String email;
    private String phone;
    private String message;
    private String sponsorshipType; // Only for sponsors: One-time or Recurring
    private String skills; // Only for volunteers
    private String availability; // Only for volunteers
}
