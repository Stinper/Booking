package com.bakaibank.booking.exceptions.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Violation {
    private final String fieldName;
    private final String message;
}
