package com.sistema.eventsapi.service;

import com.sistema.eventsapi.dto.InscricaoRequisicao;
import com.sistema.eventsapi.dto.InscricaoResposta;

import java.util.List;

public interface InscricaoService {

    InscricaoResposta inscrever(InscricaoRequisicao requisicao, String emailUsuario);

    void cancelar(Long idInscricao);

    List<InscricaoResposta> listarPorEvento(Long eventoId);

    List<InscricaoResposta> listarPorUsuario(Long usuarioId);
}
