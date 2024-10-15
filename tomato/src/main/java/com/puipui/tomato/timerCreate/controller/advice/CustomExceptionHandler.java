package com.puipui.tomato.timerCreate.controller.advice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.puipui.tomato.model.BadRequestErrorDTO;
import com.puipui.tomato.model.InvaildDetail;

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
    public ResponseEntity<BadRequestErrorDTO> handleNotFoundException(
            CustomValidationException ex) {
        BadRequestErrorDTO error = new BadRequestErrorDTO();

        List<InvaildDetail> invaildDetail = new ArrayList<>();
        invaildDetail.add(null);

        error.setDetail(ex.getMessage());
        error.setInvalidParams(invaildDetail);
        return ResponseEntity.status(NOT_FOUND).body(error);

    }
}