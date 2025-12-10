package com.sistema.eventsapi.service;

import com.sistema.eventsapi.dto.PresencaRequisicao;
import com.sistema.eventsapi.dto.PresencaResposta;

import java.util.List;

public interface PresencaService {

    // ✔ Online (portal/admin)
    PresencaResposta registrar(PresencaRequisicao req);

    // ✔ Listar para consulta
    List<PresencaResposta> listarPorEvento(Long eventoId);

    // 🚀 Novo — usado somente pela Gate na sincronização offline
    void registrarOffline(PresencaRequisicao req);
}
