package com.uniqwrites.controller;

import com.uniqwrites.dto.ApplicationRequestDTO;
import com.uniqwrites.dto.AvailabilityDTO;
import com.uniqwrites.service.TutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TutorControllerTest {

    @Mock
    private TutorService tutorService;

    @InjectMocks
    private TutorController tutorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApplyForJob() {
        ApplicationRequestDTO request = new ApplicationRequestDTO();
        doNothing().when(tutorService).applyForJob(request);

        ResponseEntity<?> response = tutorController.applyForJob(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(tutorService, times(1)).applyForJob(request);
    }

    @Test
    public void testUpdateAvailability() {
        AvailabilityDTO availability = new AvailabilityDTO();
        doNothing().when(tutorService).updateAvailability(availability);

        ResponseEntity<?> response = tutorController.updateAvailability(availability);

        assertEquals(200, response.getStatusCodeValue());
        verify(tutorService, times(1)).updateAvailability(availability);
    }
}
