package com.uniqwrites.service;

import com.uniqwrites.dto.AssignTutorDTO;
import com.uniqwrites.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    public List<User> getAllUsers() {
        // TODO: Implement retrieval of all users
        return null;
    }

    public void assignTutor(AssignTutorDTO assignTutorDTO) {
        // TODO: Implement tutor assignment logic
    }

    public Object getPendingRequests() {
        // TODO: Implement retrieval of all pending service requests
        return null;
    }

    public void deleteUser(Long id) {
        // TODO: Implement user deletion logic
    }
}
