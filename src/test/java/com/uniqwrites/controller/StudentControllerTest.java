package com.uniqwrites.controller;

import com.uniqwrites.dto.TutoringRequestDTO;
import com.uniqwrites.dto.UpdateProfileDTO;
import com.uniqwrites.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRequestTutor() {
        TutoringRequestDTO request = new TutoringRequestDTO();
        doNothing().when(studentService).submitTutoringRequest(request);

        ResponseEntity<?> response = studentController.requestTutor(request);

        assertEquals(201, response.getStatusCodeValue());
        verify(studentService, times(1)).submitTutoringRequest(request);
    }

    @Test
    public void testUpdateProfile() {
        UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO();
        doNothing().when(studentService).updateProfile(updateProfileDTO);

        ResponseEntity<?> response = studentController.updateProfile(updateProfileDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(studentService, times(1)).updateProfile(updateProfileDTO);
    }
}
