package com.uniqwrites.controller;

import com.uniqwrites.model.User;
import com.uniqwrites.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Collections.emptyList();
        when(adminService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = adminController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
        verify(adminService, times(1)).getAllUsers();
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        doNothing().when(adminService).deleteUser(userId);

        ResponseEntity<?> response = adminController.deleteUser(userId);

        assertEquals(200, response.getStatusCodeValue());
        verify(adminService, times(1)).deleteUser(userId);
    }
}
