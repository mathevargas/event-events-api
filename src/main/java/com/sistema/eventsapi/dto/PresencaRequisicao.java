package com.sistema.eventsapi.dto;

import lombok.Data;

@Data
public class PresencaRequisicao {

    private Long eventoId;        // usado online e offline

    // ONLINE (Portal/Admin)
    private Long inscricaoId;     // portal manda o ID da inscrição

    // OFFLINE (Gate sincroniza)
    private String emailUsuario;  // Gate envia somente o e-mail do participante

    private Boolean offline;      // Gate seta TRUE, Portal FALSE (ou nulo)
}
