package com.uniqwrites.controller;

import com.uniqwrites.dto.ParentRequestDTO;
import com.uniqwrites.dto.TeacherApplyDTO;
import com.uniqwrites.dto.SchoolRequestDTO;
import com.uniqwrites.service.RequestFormService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RequestFormControllerTest {

    @Mock
    private RequestFormService requestFormService;

    @InjectMocks
    private RequestFormController requestFormController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitParentRequest() {
        ParentRequestDTO parentRequestDTO = new ParentRequestDTO();
        doNothing().when(requestFormService).handleParentRequest(parentRequestDTO);

        ResponseEntity<?> response = requestFormController.submitParentRequest(parentRequestDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(requestFormService, times(1)).handleParentRequest(parentRequestDTO);
    }

    @Test
    public void testSubmitTeacherApplication() {
        TeacherApplyDTO teacherApplyDTO = new TeacherApplyDTO();
        doNothing().when(requestFormService).handleTeacherApplication(teacherApplyDTO);

        ResponseEntity<?> response = requestFormController.submitTeacherApplication(teacherApplyDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(requestFormService, times(1)).handleTeacherApplication(teacherApplyDTO);
    }

    @Test
    public void testSubmitSchoolRequest() {
        SchoolRequestDTO schoolRequestDTO = new SchoolRequestDTO();
        doNothing().when(requestFormService).handleSchoolRequest(schoolRequestDTO);

        ResponseEntity<?> response = requestFormController.submitSchoolRequest(schoolRequestDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(requestFormService, times(1)).handleSchoolRequest(schoolRequestDTO);
    }
}
