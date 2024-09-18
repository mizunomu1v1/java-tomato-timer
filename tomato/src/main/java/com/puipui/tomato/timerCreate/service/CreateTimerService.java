package com.puipui.tomato.timerCreate.service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.XsiNilLoader.Array;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Sets;

import com.puipui.tomato.model.CreateTimerDTO;
import com.puipui.tomato.model.CreateTimerForm;
import com.puipui.tomato.model.SetsDTO;

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
        // LocalDateTime tmpStartTime = LocalDateTime.now();
        // DateTimeFormatter formatStartTime = DateTimeFormatter.ofPattern("yyyy/MM/dd
        // HH:mm:ss.SSS");
        // String startTime = formatStartTime.format(tmpStartTime);
        String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));

        // 2. エラーチェック
        createTimerEntity.validate();

        // 3. 変数作成
        // sets: setCount分以下を繰り返し配列を作成する
        ArrayList<SetsDTO> setDto = CreateSets(startTime, createTimerEntity);

        // 4. 4. DB登録

        // 5. レスポンスを返却する
        var dto = new CreateTimerDTO();
        dto.setSets(setDto);
        return dto;
    }

    /**
     * 変数作成
     * 
     * @param startTime
     * @param createTimerEntity
     * @return
     */
    public ArrayList<SetsDTO> CreateSets(String startTime, CreateTimerEntity createTimerEntity) {

        // setCount分以下を繰り返し配列を作成する
        ArrayList<SetsDTO> setsDto = new ArrayList<SetsDTO>();
        Integer totalSets = createTimerEntity.getTotalSets();

        for (Integer i = 0; totalSets > i; i++) {
            String startWorkTime = startTime;
            SetsDTO set = new SetsDTO();
            set.setSet(i);
            set.setStartWorkTime(startWorkTime);
            set.setEndWorkTime(startWorkTime);
            set.endBreakTime(startWorkTime);
            setsDto.add(set);
        }

        return setsDto;
    }

}