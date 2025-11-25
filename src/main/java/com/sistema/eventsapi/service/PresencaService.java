package com.sistema.eventsapi.service;

import com.sistema.eventsapi.dto.CheckinResponse;
import com.sistema.eventsapi.dto.PresencaRequisicao;
import com.sistema.eventsapi.dto.PresencaResposta;
import java.util.List;

public interface PresencaService {

    PresencaResposta registrar(PresencaRequisicao req);


    List<PresencaResposta> listarPorEvento(Long eventoId);

}
