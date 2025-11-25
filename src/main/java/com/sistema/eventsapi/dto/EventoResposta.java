package com.sistema.eventsapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoResposta {
    private Long id;
    private String titulo;
    private String descricao;
    private String local;
    private LocalDateTime data;
    private Integer limiteVagas;
    private String status;
}


