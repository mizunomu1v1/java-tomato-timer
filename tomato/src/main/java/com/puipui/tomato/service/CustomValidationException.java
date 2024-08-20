package com.puipui.tomato.service;

public final class CustomValidationException extends RuntimeException {

    private final String errorMessage;

    public CustomValidationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

}