package com.sistema.eventsapi.dto;

import lombok.Data;

@Data
public class PresencaRequisicao {
    private Long eventoId;
    private Long inscricaoId;
    private Boolean offline; // sempre false no portal
}
