package com.sistema.eventsapi.dto;

import lombok.Data;

@Data
public class CheckinResponse {
    private Long presencaId;
    private Long eventoId;
    private Long usuarioId;
    private String status; // sempre "PRESENTE"
    private Boolean offline;
}


