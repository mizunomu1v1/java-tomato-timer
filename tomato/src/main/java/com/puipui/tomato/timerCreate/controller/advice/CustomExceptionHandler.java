package com.puipui.tomato.timerCreate.controller.advice;

import org.apache.catalina.connector.Response;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.puipui.tomato.model.BadRequestError;
import com.puipui.tomato.model.InvaildParam;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.*;

/**
 * 例外ハンドリング
 * 【引用】
 * Spring Boot では ResponseEntityExceptionHandlerクラスで
 * 『どの例外が発生したときになにを返すか』のハンドリングを行っています。
 * このクラスを継承して例外ごとに定義されているメソッドを
 * オーバーライドすることで、処理を好きなように行うことができます。
 * https://qiita.com/ke_suke0215/items/2f152cf859990e39923c
 * 
 */
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * エラーレスポンス
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<BadRequestError> handleNotFoundException(
            CustomValidationException ex) {
        BadRequestError error = new BadRequestError();

        List<InvaildParam> invaildParam = new ArrayList<>();
        invaildParam.add(null);

        error.setDetail(ex.getMessage());
        error.setInvalidParams(invaildParam);
        return ResponseEntity.status(NOT_FOUND).body(error);

    }
}