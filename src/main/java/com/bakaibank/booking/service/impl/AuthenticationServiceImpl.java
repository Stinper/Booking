package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.employee.LoginRequest;
import com.bakaibank.booking.exceptions.AlreadyAuthenticatedException;
import com.bakaibank.booking.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Override
    public void signIn(@Valid LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response)
            throws BadCredentialsException, AlreadyAuthenticatedException {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

        if (existingAuth != null && existingAuth.isAuthenticated() && !(existingAuth instanceof AnonymousAuthenticationToken))
            throw new AlreadyAuthenticatedException("Пользователь уже аутентифицирован");

        UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                );

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        securityContextRepository.saveContext(context, request, response);
    }
}
