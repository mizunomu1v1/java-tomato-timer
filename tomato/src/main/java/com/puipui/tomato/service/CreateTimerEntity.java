package com.puipui.tomato.service;

import lombok.Data;

// final=変更を許さないクラス
@Data
public final class CreateTimerEntity extends ValidatableEntity {

    String breakDuration;
    String workDuration;
    String totalSets;

    // 必須チェック: 必須項目が存在すること
    public void validate() throws CustomValidationException {
        if (breakDuration == null ||
                workDuration == null ||
                totalSets == null) {
            throw new CustomValidationException("必要なフィールドがnullです");
        }

    }

}