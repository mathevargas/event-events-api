package com.sistema.eventsapi.dto;

import lombok.Data;

@Data
public class PresencaResposta {
    private Long id;
    private Long eventoId;
    private Long usuarioId;
    private Boolean offline;
}

