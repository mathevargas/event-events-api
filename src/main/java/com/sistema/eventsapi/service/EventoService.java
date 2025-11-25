package com.sistema.eventsapi.service;

import com.sistema.eventsapi.dto.EventoCriarRequisicao;
import com.sistema.eventsapi.dto.EventoAtualizarRequisicao;
import com.sistema.eventsapi.dto.EventoResposta;

import java.util.List;

public interface EventoService {

    EventoResposta criar(EventoCriarRequisicao requisicao);

    List<EventoResposta> listarTodos();

    EventoResposta buscarPorId(Long id);

    EventoResposta atualizar(Long id, EventoAtualizarRequisicao requisicao);

    void excluir(Long id);
}
