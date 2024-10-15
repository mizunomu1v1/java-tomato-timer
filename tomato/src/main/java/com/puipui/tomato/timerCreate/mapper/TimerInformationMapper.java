package com.puipui.tomato.timerCreate.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.puipui.tomato.model.TimerInformationEntity;

@Mapper
public interface TimerInformationMapper {

    /**
     * タイマー情報登録
     * @param createTimerFormDTO
     */
    void insertTimerInformation(TimerInformationEntity timerInformationEntity);

}
