package com.puipui.tomato.validator;

import com.puipui.tomato.timerCreate.controller.advice.CustomValidationException;

public interface CommonValidator<T> {

    void validate(T data) throws CustomValidationException;

}