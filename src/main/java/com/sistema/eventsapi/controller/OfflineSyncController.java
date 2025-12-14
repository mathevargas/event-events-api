package com.sistema.eventsapi.controller;

import com.sistema.eventsapi.dto.offline.OfflineSyncRequest;
import com.sistema.eventsapi.dto.offline.OfflineSyncResponse;
import com.sistema.eventsapi.service.OfflineSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offline")
@RequiredArgsConstructor
public class OfflineSyncController {

    private final OfflineSyncService offlineSyncService;

    /**
     * Sincronização usada pela Portaria (Portal/PWA).
     * Requer token JWT com ROLE_ADMIN ou ROLE_PORTEIRO (configurado no SegurancaConfig).
     */
    @PostMapping("/sync")
    public ResponseEntity<OfflineSyncResponse> sincronizar(
            @RequestBody OfflineSyncRequest request,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        return ResponseEntity.ok(offlineSyncService.sincronizar(request, token));
    }
}
