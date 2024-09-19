package com.puipui.tomato.timerCreate.service;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;

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

        // リクエストボディから各パラメータを取得する
        CreateTimerEntity createTimerEntity = new CreateTimerEntity();
        createTimerEntity.setWorkDuration(createTimerForm.getWorkDuration());
        createTimerEntity.setBreakDuration(createTimerForm.getBreakDuration());
        createTimerEntity.setTotalSets(createTimerForm.getTotalSets());
        // エラーチェック
        createTimerEntity.validate();

        // DB登録

        // レスポンスを返却する
        var dto = new CreateTimerDTO();
        dto.setTimerId(UUID.randomUUID().toString());
        dto.setSets(CreateSets(createTimerEntity));
        return dto;
    }

    /**
     * 日付フォーマット
     * 
     * @param localDateTime
     * @return
     */
    public String DateTimeformat(LocalDateTime localDateTime) {
        return localDateTime.format(
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    /**
     * セットリスト作成
     * 
     * @param createTimerEntity
     * @return
     */
    public List<SetsDTO> CreateSets(CreateTimerEntity createTimerEntity) {

        LocalDateTime startWorkLocalDateTime = LocalDateTime.now();
        LocalDateTime endWorkTimeLocalDateTime;
        LocalDateTime endbreakTimeLocalDateTime = startWorkLocalDateTime;

        // setCount分以下を繰り返し配列を作成する
        List<SetsDTO> setsDto = new ArrayList<SetsDTO>();
        Integer totalSets = createTimerEntity.getTotalSets();

        for (Integer i = 0; totalSets > i; i++) {

            SetsDTO setDto = new SetsDTO();
            setDto.setSet(i + 1);

            // 初回以外はendbreakTimeLocalDateTimeをセット
            if (i != 0) {
                startWorkLocalDateTime = endbreakTimeLocalDateTime;
            }
            endWorkTimeLocalDateTime = startWorkLocalDateTime.plusMinutes(createTimerEntity.workDuration);
            endbreakTimeLocalDateTime = endWorkTimeLocalDateTime.plusMinutes(createTimerEntity.breakDuration);

            setDto.setStartWorkTime(DateTimeformat(startWorkLocalDateTime));
            setDto.setEndWorkTime(DateTimeformat(endWorkTimeLocalDateTime));
            setDto.setEndBreakTime(DateTimeformat(endbreakTimeLocalDateTime));
            setsDto.add(setDto);
        }

        return setsDto;
    }

}