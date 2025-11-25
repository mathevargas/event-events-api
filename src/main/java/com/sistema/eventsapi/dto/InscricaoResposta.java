package com.sistema.eventsapi.dto;

import lombok.Data;

@Data
public class InscricaoResposta {
    private Long id;
    private Long eventoId;
    private Long usuarioId;
    private String status;
}
