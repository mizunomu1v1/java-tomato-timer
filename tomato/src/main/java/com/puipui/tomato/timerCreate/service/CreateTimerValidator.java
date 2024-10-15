package com.puipui.tomato.timerCreate.service;

import com.puipui.tomato.model.CreateTimerFormDTO;
import com.puipui.tomato.timerCreate.controller.advice.CustomValidationException;
import com.puipui.tomato.validator.CommonValidator;

import lombok.Data;

// final=変更を許さないクラス
@Data
public final class CreateTimerValidator implements CommonValidator<CreateTimerFormDTO> {

    // エラーチェック
    public void validate(CreateTimerFormDTO createTimerFormDTO) throws CustomValidationException {
        // 桁数チェック:
        validateDigitsLength(createTimerFormDTO.getWorkDuration(), 1, 60, "作業時間は1分～60分の間で設定してください");
        validateDigitsLength(createTimerFormDTO.getBreakDuration(), 1, 60, "休憩時間は1分～60分の間で設定してください");
    }

    /**
     * 桁数チェック
     * 
     * @param value     項目
     * @param MinLength 最大桁数
     * @param maxLength 最小桁数
     * @param message   メッセージ
     * @return
     */
    public boolean validateDigitsLength(Integer value, Integer MinLength, Integer maxLength, String message) {
        if (value <= MinLength || value >= maxLength) {
            throw new CustomValidationException(message);
        }
        return true;
    }

}