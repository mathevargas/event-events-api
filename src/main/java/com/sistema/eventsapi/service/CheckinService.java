package com.sistema.eventsapi.service;

import com.sistema.eventsapi.dto.CheckinRequest;
import com.sistema.eventsapi.dto.CheckinResponse;

public interface CheckinService {
    CheckinResponse registrar(CheckinRequest req, String emailUsuario, String tokenJwt);
}
