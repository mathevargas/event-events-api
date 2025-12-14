package com.sistema.eventsapi.dto.offline;

import lombok.Data;

@Data
public class OfflinePresencaItem {
    private Long eventoId;
    private Long usuarioId;
    private String emailUsuario; // para enviar e-mail de comparecimento
    private String clientUuid;   // opcional (idempotência no client)
}
