package com.puipui.tomato.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.puipui.tomato.model.TomatoDTO;
import com.puipui.tomato.model.TomatoForm;
import com.puipui.tomato.service.TomatoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TomatoController implements TomatoApi {

    private final TomatoService tomatoService;

    @Override
    public ResponseEntity<TomatoDTO> createTimer(TomatoForm tomatoForm) {
        var entity = tomatoService.find(tomatoForm.getPomodoroTime());
        var dto = new TomatoDTO();

        // dto.setId(entity.getId());
        // dto.setTitle(entity.getTitle());
        dto.setStartTime(0);
        dto.setEndTime(1);
        return ResponseEntity.ok(dto);
    }
}