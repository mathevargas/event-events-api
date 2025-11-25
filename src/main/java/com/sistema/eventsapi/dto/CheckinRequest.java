package com.sistema.eventsapi.dto;

import lombok.Data;

@Data
public class CheckinRequest {

    private Long eventoId;
    private Long inscricaoId;
    private Boolean offline; // true = veio do modo offline
}
