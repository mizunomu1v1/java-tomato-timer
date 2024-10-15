package com.puipui.tomato.timerCreate.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puipui.tomato.common.DateUtils;
import com.puipui.tomato.constants.AppConstants;
import com.puipui.tomato.constants.TimerStatusConstants;
import com.puipui.tomato.model.CreateTimerDTO;
import com.puipui.tomato.model.CreateTimerForm;
import com.puipui.tomato.model.CreateTimerFormDTO;
import com.puipui.tomato.model.SetDetail;
import com.puipui.tomato.model.TimerInformationEntity;
import com.puipui.tomato.timerCreate.mapper.TimerInformationMapper;

/**
 * ビジネスロジック層
 * コントローラーから受け取ったデータで処理実施する。
 */
@Service
public class CreateTimerService {

    @Autowired
    private TimerInformationMapper timerInformationMapper;

    public CreateTimerDTO CreateTimer(CreateTimerForm createTimerForm) {

        // リクエストボディから各パラメータを取得する
        CreateTimerFormDTO createTimerFormDTO = new CreateTimerFormDTO();
        createTimerFormDTO.setWorkDuration(createTimerForm.getWorkDuration());
        createTimerFormDTO.setBreakDuration(createTimerForm.getBreakDuration());
        createTimerFormDTO.setTotalSetCount(createTimerForm.getTotalSetCount());

        // エラーチェック
        CreateTimerValidator createTimerValidator = new CreateTimerValidator();
        createTimerValidator.validate(createTimerFormDTO);

        // レスポンス作成
        var createTimerDTO = new CreateTimerDTO();
        createTimerDTO.setTimerId(UUID.randomUUID().toString());
        createTimerDTO.setSetList(CreateSets(createTimerFormDTO));

        // DB登録
        TimerInformationInsert(createTimerForm, createTimerDTO);

        return createTimerDTO;
    }

    public List<SetDetail> CreateSets(CreateTimerFormDTO createTimerFormDTO) {

        LocalDateTime startWorkLocalDateTime = LocalDateTime.now();
        LocalDateTime endWorkTimeLocalDateTime;
        LocalDateTime endbreakTimeLocalDateTime = startWorkLocalDateTime;

        // setCount分以下を繰り返し配列を作成する
        List<SetDetail> setDetailList = new ArrayList<SetDetail>();
        Integer totalSets = createTimerFormDTO.getTotalSetCount();

        for (Integer i = 0; totalSets > i; i++) {

            SetDetail setDetail = new SetDetail();
            setDetail.setCurrentSetCount(i + 1);

            // 初回以外はendbreakTimeLocalDateTimeをセット
            if (i != 0) {
                startWorkLocalDateTime = endbreakTimeLocalDateTime;
                setDetail.setStatusTypeCode(TimerStatusConstants.TIMER_STATUS_RUNNING);
            }
            endWorkTimeLocalDateTime = startWorkLocalDateTime.plusMinutes(createTimerFormDTO.getWorkDuration());
            endbreakTimeLocalDateTime = endWorkTimeLocalDateTime.plusMinutes(createTimerFormDTO.getBreakDuration());

            setDetail.setStartWorkDatetime(DateUtils.DateTimeformat(startWorkLocalDateTime));
            setDetail.setEndWorkDatetime(DateUtils.DateTimeformat(endWorkTimeLocalDateTime));
            setDetail.setEndBreakDatetime(DateUtils.DateTimeformat(endbreakTimeLocalDateTime));
            setDetail.setStatusTypeCode(TimerStatusConstants.TIMER_STATUS_RUNNING);
            setDetailList.add(setDetail);
        }

        return setDetailList;
    }

    /**
     * 
     * @param createTimerForm
     * @param createTimerDTO
     */
    private void TimerInformationInsert(CreateTimerForm createTimerForm, CreateTimerDTO createTimerDTO) {

        TimerInformationEntity timerInformationEntity = new TimerInformationEntity();

        // セット共通情報取得
        timerInformationEntity.setTimerId(createTimerDTO.getTimerId());
        timerInformationEntity.setTotalSetCount(createTimerForm.getTotalSetCount());

        // セット個別情報取得
        for (SetDetail setDetail : createTimerDTO.getSetList()) {
            timerInformationEntity.setCurrentSetCount(setDetail.getCurrentSetCount());
            timerInformationEntity.setStartWorkDatetime(setDetail.getStartWorkDatetime());
            timerInformationEntity.setEndWorkDatetime(setDetail.getEndWorkDatetime());
            timerInformationEntity.setEndBreakDatetime(setDetail.getEndBreakDatetime());
            timerInformationEntity.setStatusTypeCode(setDetail.getStatusTypeCode());
            timerInformationEntity.setInsertDatetime(DateUtils.DateTimeformat(LocalDateTime.now()));
            timerInformationEntity.setInsertFunction(AppConstants.APP_NAME_TIMER_CREATE);
            timerInformationEntity.setUpdateDatetime(DateUtils.DateTimeformat(LocalDateTime.now()));
            timerInformationEntity.setUpdateFunction(AppConstants.APP_NAME_TIMER_CREATE);
            timerInformationEntity.setUpdateCount(0);
        }
        // DB登録処理
        timerInformationMapper.insertTimerInformation(timerInformationEntity);
    }

}
