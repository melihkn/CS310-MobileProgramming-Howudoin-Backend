package edu.sabanciuniv.service;

import edu.sabanciuniv.model.User;
import edu.sabanciuniv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.HashSet;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String firstName, String lastName, String email, String password) throws Exception {
        Optional<User> existingUser = userRepository.findByEmail(email);
        Optional<User> existingUsername = userRepository.findByUsername(username);
        if(existingUser.isPresent() || existingUsername.isPresent()){
            throw new Exception("User with email or username already exists");
        }

        User user = User.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .friends(new HashSet<>())
                .build();

        return userRepository.save(user);
    }

    public String getUserIdByUsername(String username) throws Exception {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new Exception("User not found with username: " + username));
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }
}
