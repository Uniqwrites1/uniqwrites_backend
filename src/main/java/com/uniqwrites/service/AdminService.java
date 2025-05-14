package com.uniqwrites.service;

import com.uniqwrites.dto.AssignTutorDTO;
import com.uniqwrites.model.User;
import com.uniqwrites.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void assignTutor(AssignTutorDTO assignTutorDTO) {
        // Example implementation: assign tutor to student logic
        // This is a placeholder; actual implementation depends on business rules and data model
        System.out.println("Assigning tutor with ID " + assignTutorDTO.getTutorId() +
                " to student with ID " + assignTutorDTO.getStudentId());
        // TODO: Add database update logic here
    }

    public Object getPendingRequests() {
        // Example implementation: retrieve all pending service requests
        // This is a placeholder; actual implementation depends on data model and repository
        System.out.println("Retrieving all pending service requests");
        // TODO: Query database for pending requests and return
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
