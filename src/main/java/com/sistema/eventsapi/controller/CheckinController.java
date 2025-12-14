package com.sistema.eventsapi.controller;

import com.sistema.eventsapi.dto.CheckinRequest;
import com.sistema.eventsapi.dto.CheckinResponse;
import com.sistema.eventsapi.service.CheckinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkin")
@RequiredArgsConstructor
@Tag(name = "Check-in", description = "Endpoints de registro de presença (check-in)")
public class CheckinController {

    private final CheckinService checkinService;

    @PostMapping
    @Operation(summary = "Registrar check-in", description = "Endpoint para registrar o check-in no evento")
    public ResponseEntity<CheckinResponse> registrar(
            @RequestBody CheckinRequest req,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        String emailLogado = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(checkinService.registrar(req, emailLogado, token));
    }
}
