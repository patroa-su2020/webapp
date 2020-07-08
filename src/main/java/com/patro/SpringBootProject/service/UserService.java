package com.patro.SpringBootProject.service;

import com.patro.SpringBootProject.dao.UserRepository;
import com.patro.SpringBootProject.model.User;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private StatsDClient statsDClient;

    private long endTime;
    private long startTime;

    public void addUser(User user) {
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        System.out.println("Password: "+user.getPassword()+" Encoded Password: "+encryptedPassword);

        String userEncodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        System.out.println("Salt Password: " + userEncodedPassword);
        user.setPassword(userEncodedPassword);
        startTime = System.currentTimeMillis();
        userRepository.save(user);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB: Add User", endTime-startTime);
    }

    public Optional<User> getUserByUsername(String username) {
        startTime = System.currentTimeMillis();
        Optional<User> backendUser = userRepository.findById(username);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB: Get User", endTime-startTime);
        return backendUser;
    }

    public void updateUserDetails(User user) {
        startTime = System.currentTimeMillis();
        userRepository.save(user);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB: Update User", endTime-startTime);
    }
}
