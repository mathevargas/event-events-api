package com.sistema.eventsapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoAtualizarRequisicao {
    private String titulo;
    private String descricao;
    private String local;
    private LocalDateTime data;
    private Integer limiteVagas;
    private String status; // ATIVO / ENCERRADO
}

