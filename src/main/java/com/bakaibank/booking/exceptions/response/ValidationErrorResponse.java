package com.bakaibank.booking.exceptions.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ValidationErrorResponse {
    private final List<Violation> violations;
}
