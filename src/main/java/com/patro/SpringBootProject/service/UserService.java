package com.patro.SpringBootProject.service;

import com.patro.SpringBootProject.dao.UserRepository;
import com.patro.SpringBootProject.model.User;
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
/*
    public UserService(){
        this.userRepository = new ;
    }
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
*/

    public void addUser(User user) {
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        System.out.println("Password: "+user.getPassword()+" Encoded Password: "+encryptedPassword);
//        System.out.println("Match???: "+bCryptPasswordEncoder.matches(user.getPassword(), encryptedpassword));
      //  user.setPassword(encryptedPassword);
        String userEncodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        System.out.println("Salt Password: " + userEncodedPassword);
        user.setPassword(userEncodedPassword);
        userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        Optional<User> backendUser = userRepository.findById(username);
        return backendUser;
    }

    public void updateUserDetails(User user) {
        userRepository.save(user);
    }
}
