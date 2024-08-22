package com.puipui.tomato.service;

import org.springframework.stereotype.Service;

import com.puipui.tomato.model.CreateTimerDTO;
import com.puipui.tomato.model.CreateTimerForm;

/**
 * ビジネスロジック層
 * コントローラーから受け取ったデータで処理実施する。
 */
@Service
public class CreateTimerService {

    public CreateTimerDTO CreateTimer(CreateTimerForm createTimerForm) {

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

        var dto = new CreateTimerDTO();

        return dto;
    }

}