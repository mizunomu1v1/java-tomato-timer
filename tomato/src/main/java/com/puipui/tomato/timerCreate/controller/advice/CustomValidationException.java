package com.puipui.tomato.timerCreate.controller.advice;

/**
 * 独自例外クラス
 */
public class CustomValidationException extends RuntimeException {

    private String errorMessage;

    public CustomValidationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

}