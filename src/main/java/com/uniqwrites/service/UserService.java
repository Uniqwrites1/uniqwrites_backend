package com.uniqwrites.service;

import com.uniqwrites.dto.LoginRequestDTO;
import com.uniqwrites.dto.SignupRequestDTO;
import com.uniqwrites.model.Role;
import com.uniqwrites.model.User;
import com.uniqwrites.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(SignupRequestDTO signupRequest) throws Exception {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new Exception("Email is already in use");
        }

        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        try {
            Role role = Role.valueOf(signupRequest.getRole().toUpperCase());
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid role specified");
        }

        user.setStatus("ACTIVE");

        return userRepository.save(user);
    }

    public Optional<User> authenticateUser(LoginRequestDTO loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public User registerStudent(SignupRequestDTO studentRequest) throws Exception {
        studentRequest.setRole("STUDENT");
        return registerUser(studentRequest);
    }

    public User registerTutor(SignupRequestDTO tutorRequest) throws Exception {
        tutorRequest.setRole("TUTOR");
        return registerUser(tutorRequest);
    }

    public User registerSchool(SignupRequestDTO schoolRequest) throws Exception {
        schoolRequest.setRole("SCHOOL");
        return registerUser(schoolRequest);
    }

    public User registerSponsor(SignupRequestDTO sponsorRequest) throws Exception {
        sponsorRequest.setRole("SPONSOR");
        return registerUser(sponsorRequest);
    }
}
