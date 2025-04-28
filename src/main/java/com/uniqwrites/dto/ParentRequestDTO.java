package com.uniqwrites.dto;

import lombok.Data;

@Data
public class ParentRequestDTO {
    private String parentName;
    private String childName;
    private String childAge;
    private String requestDetails;
}
