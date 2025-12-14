package com.sistema.eventsapi.service;

import com.sistema.eventsapi.dto.offline.OfflineSyncRequest;
import com.sistema.eventsapi.dto.offline.OfflineSyncResponse;

public interface OfflineSyncService {
    OfflineSyncResponse sincronizar(OfflineSyncRequest req, String tokenJwt);
}
