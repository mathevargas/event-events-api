package com.sistema.eventsapi.controller;

import com.sistema.eventsapi.dto.InscricaoRequisicao;
import com.sistema.eventsapi.dto.InscricaoResposta;
import com.sistema.eventsapi.service.InscricaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscricoes")
@RequiredArgsConstructor
@Tag(name = "Inscrições", description = "Operações relacionadas às inscrições nos eventos")
public class InscricaoController {

    private final InscricaoService inscricaoService;

    @PostMapping
    @Operation(summary = "Inscrever-se em evento", description = "Endpoint para realizar inscrição no evento")
    public ResponseEntity<InscricaoResposta> inscrever(@RequestBody InscricaoRequisicao req) {
        String emailLogado = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(inscricaoService.inscrever(req, emailLogado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar inscrição", description = "Endpoint para cancelar a inscrição no evento")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        inscricaoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/evento/{idEvento}")
    @Operation(summary = "Listar inscrições por evento", description = "Endpoint para listar todas as inscrições de um evento")
    public ResponseEntity<List<InscricaoResposta>> listarPorEvento(@PathVariable Long idEvento) {
        return ResponseEntity.ok(inscricaoService.listarPorEvento(idEvento));
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar inscrições por usuário", description = "Endpoint para listar todas as inscrições de um usuário")
    public ResponseEntity<List<InscricaoResposta>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(inscricaoService.listarPorUsuario(idUsuario));
    }
}
