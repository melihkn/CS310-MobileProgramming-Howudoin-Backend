package edu.sabanciuniv.controller;

import edu.sabanciuniv.config.JwtTokenUtil;
import edu.sabanciuniv.model.User;
import edu.sabanciuniv.repository.UserRepository;
import edu.sabanciuniv.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Data
    static class RegisterRequest {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String password;


    }

    @Data
    static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    static class JwtAuthenticationResponse {
        private String accessToken;
        private String tokenType = "Bearer";

        public JwtAuthenticationResponse(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        try{
            User user = userService.registerUser(
                    registerRequest.getUsername(),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword()
            );
            return ResponseEntity.ok("User registered successfully");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository.findByEmail(loginRequest.getEmail()).get();
            String token = jwtTokenUtil.generateToken(user.getId());

            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }


}
