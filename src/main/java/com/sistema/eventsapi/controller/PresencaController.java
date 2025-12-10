package com.sistema.eventsapi.controller;

import com.sistema.eventsapi.dto.PresencaRequisicao;
import com.sistema.eventsapi.dto.PresencaResposta;
import com.sistema.eventsapi.service.PresencaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/presencas")
@RequiredArgsConstructor
public class PresencaController {

    private final PresencaService presencaService;

    // Presença ONLINE (portal/admin)
    @PostMapping
    public ResponseEntity<PresencaResposta> registrar(@RequestBody PresencaRequisicao req) {
        return ResponseEntity.ok(presencaService.registrar(req));
    }

    // Listagem para consulta/admin
    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<PresencaResposta>> listarPorEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(presencaService.listarPorEvento(eventoId));
    }

    // 🚀 Presença OFFLINE (Gate sincroniza aqui usando EMAIL + ID_EVENTO)
    @PostMapping("/offline")
    public ResponseEntity<Void> registrarOffline(@RequestBody PresencaRequisicao req) {
        presencaService.registrarOffline(req); // método novo que vamos criar no Service
        return ResponseEntity.ok().build();
    }
}
