package com.sistema.eventsapi.controller;


import com.sistema.eventsapi.dto.EventoCriarRequisicao;
import com.sistema.eventsapi.dto.EventoAtualizarRequisicao;
import com.sistema.eventsapi.dto.EventoResposta;
import com.sistema.eventsapi.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


import java.util.List;


@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/eventos")
@Tag(name = "Eventos", description = "Operações relacionadas aos eventos")
public class EventoController {


    private final EventoService eventoService;


    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }


    @PostMapping
    @Operation(summary = "Criar evento", description = "Endpoint para criar um novo evento")
    public EventoResposta criar(@RequestBody EventoCriarRequisicao req) {
        return eventoService.criar(req);
    }


    @GetMapping
    @Operation(summary = "Listar eventos", description = "Endpoint para listar todos os eventos")
    public List<EventoResposta> listar() {
        return eventoService.listarTodos();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento", description = "Endpoint para buscar evento por ID")
    public EventoResposta buscar(@PathVariable Long id) {
        return eventoService.buscarPorId(id);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento", description = "Endpoint para atualizar os dados de um evento")
    public EventoResposta atualizar(@PathVariable Long id,
                                    @RequestBody EventoAtualizarRequisicao req) {
        return eventoService.atualizar(id, req);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir evento", description = "Endpoint para excluir um evento")
    public void excluir(@PathVariable Long id) {
        eventoService.excluir(id);
    }
}
