package com.sistema.eventsapi.dto.offline;

import lombok.Data;

@Data
public class OfflineInscricaoItem {
    private Long eventoId;
    private Long usuarioId;
    private String emailUsuario; // opcional (útil para envio de e-mail)
    private String clientUuid;   // opcional (idempotência no client)
}
