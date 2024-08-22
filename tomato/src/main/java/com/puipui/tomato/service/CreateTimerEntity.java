package com.puipui.tomato.service;

import lombok.Data;

// final=変更を許さないクラス
@Data
public final class CreateTimerEntity extends ValidatableEntity {

    Integer workDuration;
    Integer breakDuration;
    Integer totalSets;

    // エラーチェック
    public void validate() throws CustomValidationException {
        // 桁数チェック:
        validateDigitsLength(workDuration, 1, 60, "作業時間は1分～60分の間で設定してね！");
        validateDigitsLength(breakDuration, 1, 60, "休憩時間は1分～60分の間で設定してね！");
        validateDigitsLength(totalSets, 1, 10, "セット数は1分～60分の間で設定してね！");
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