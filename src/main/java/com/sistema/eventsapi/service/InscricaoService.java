package com.sistema.eventsapi.service;

import com.sistema.eventsapi.dto.InscricaoRequisicao;
import com.sistema.eventsapi.dto.InscricaoResposta;

import java.util.List;

public interface InscricaoService {

    // Agora recebe também o token JWT
    InscricaoResposta inscrever(InscricaoRequisicao requisicao, String emailUsuario, String tokenJwt);

    // Cancelamento também precisa do token
    void cancelar(Long idInscricao, String tokenJwt);

    List<InscricaoResposta> listarPorEvento(Long eventoId);

    List<InscricaoResposta> listarPorUsuario(Long usuarioId);
}
