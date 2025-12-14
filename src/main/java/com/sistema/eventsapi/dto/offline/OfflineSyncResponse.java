package com.sistema.eventsapi.dto.offline;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfflineSyncResponse {
    private int inscricoesProcessadas;
    private int presencasProcessadas;
}
