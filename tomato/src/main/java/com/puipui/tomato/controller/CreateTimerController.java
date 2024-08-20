package com.puipui.tomato.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.puipui.tomato.model.CreateTimerDTO;
import com.puipui.tomato.model.CreateTimerForm;
import com.puipui.tomato.service.CreateTimerEntity;
import com.puipui.tomato.service.CreateTimerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CreateTimerController implements TomatoApi {

    private final CreateTimerService createTimerService;

    @Override
    public ResponseEntity<CreateTimerDTO> createTimer(CreateTimerForm createTimerForm) {

        // 1. パラメータ取得
        // リクエストボディから各パラメータを取得する
        CreateTimerEntity createTimerEntity = new CreateTimerEntity();
        createTimerEntity.setWorkDuration(createTimerForm.getWorkDuration());
        createTimerEntity.setBreakDuration(createTimerForm.getBreakDuration());
        createTimerEntity.setTotalSets(createTimerForm.getTotalSets());

        // システム日時を取得する
        // TODO

        // 2. エラーチェック
        createTimerEntity.validate();
        ;

        var dto = new CreateTimerDTO();
        // dto.setId(entity.getId());
        // dto.setTitle(entity.getTitle());
        // dto.setTimerId(0);
        return ResponseEntity.ok(dto);
    }
}