package com.sistema.eventsapi.dto.offline;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OfflineSyncRequest {
    private List<OfflineInscricaoItem> inscricoes = new ArrayList<>();
    private List<OfflinePresencaItem> presencas = new ArrayList<>();
}
