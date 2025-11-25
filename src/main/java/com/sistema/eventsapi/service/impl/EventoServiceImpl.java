package com.sistema.eventsapi.service.impl;

import com.sistema.eventsapi.dto.EventoCriarRequisicao;
import com.sistema.eventsapi.dto.EventoAtualizarRequisicao;
import com.sistema.eventsapi.dto.EventoResposta;
import com.sistema.eventsapi.entity.Evento;
import com.sistema.eventsapi.exception.ApiException;
import com.sistema.eventsapi.repository.EventoRepository;
import com.sistema.eventsapi.service.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;

    public EventoServiceImpl(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Override
    public EventoResposta criar(EventoCriarRequisicao requisicao) {
        Evento evento = new Evento();
        evento.setTitulo(requisicao.getTitulo());
        evento.setDescricao(requisicao.getDescricao());
        evento.setLocal(requisicao.getLocal());
        evento.setData(requisicao.getData());
        evento.setLimiteVagas(null);  // Limite de vagas removido
        evento.setStatus("ATIVO");

        eventoRepository.save(evento);

        return mapearParaResposta(evento);
    }

    @Override
    public List<EventoResposta> listarTodos() {
        return eventoRepository.findAll()
                .stream()
                .map(this::mapearParaResposta)
                .toList();
    }

    @Override
    public EventoResposta buscarPorId(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

        return mapearParaResposta(evento);
    }

    @Override
    public EventoResposta atualizar(Long id, EventoAtualizarRequisicao requisicao) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

        evento.setTitulo(requisicao.getTitulo());
        evento.setDescricao(requisicao.getDescricao());
        evento.setLocal(requisicao.getLocal());
        evento.setData(requisicao.getData());
        evento.setLimiteVagas(null);  // Limite de vagas removido
        evento.setStatus(requisicao.getStatus());

        eventoRepository.save(evento);

        return mapearParaResposta(evento);
    }

    @Override
    public void excluir(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND);
        }

        eventoRepository.deleteById(id);
    }

    private EventoResposta mapearParaResposta(Evento evento) {
        EventoResposta resp = new EventoResposta();
        resp.setId(evento.getId());
        resp.setTitulo(evento.getTitulo());
        resp.setDescricao(evento.getDescricao());
        resp.setLocal(evento.getLocal());
        resp.setData(evento.getData());
        resp.setLimiteVagas(evento.getLimiteVagas()); // Limite de vagas removido
        resp.setStatus(evento.getStatus());
        return resp;
    }
}
