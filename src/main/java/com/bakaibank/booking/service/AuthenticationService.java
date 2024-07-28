package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.employee.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface AuthenticationService {
    void signIn(@Valid LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
}
