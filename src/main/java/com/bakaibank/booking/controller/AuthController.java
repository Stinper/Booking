package com.bakaibank.booking.controller;

import com.bakaibank.booking.dto.employee.LoginRequest;
import com.bakaibank.booking.exceptions.AlreadyAuthenticatedException;
import com.bakaibank.booking.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response)
            throws BadCredentialsException, AlreadyAuthenticatedException {
        authenticationService.signIn(loginRequest, request, response);
        return ResponseEntity.ok().build();
    }
}
