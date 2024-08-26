package com.puipui.tomato.timerCreate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.puipui.tomato.controller.TomatoApi;
import com.puipui.tomato.model.CreateTimerDTO;
import com.puipui.tomato.model.CreateTimerForm;
import com.puipui.tomato.timerCreate.service.CreateTimerService;

import lombok.RequiredArgsConstructor;

/**
 * プレゼンテーション層
 * 画面から受け取ったデータをビジネスロジックサービスに渡す
 * 
 */
@RestController
@RequiredArgsConstructor
public class CreateTimerController implements TomatoApi {

    private final CreateTimerService createTimerService;

    @Override
    public ResponseEntity<CreateTimerDTO> createTimer(CreateTimerForm createTimerForm) {
        CreateTimerDTO createTimerDTO = createTimerService.CreateTimer(createTimerForm);
        return ResponseEntity.ok(createTimerDTO);
    }
}