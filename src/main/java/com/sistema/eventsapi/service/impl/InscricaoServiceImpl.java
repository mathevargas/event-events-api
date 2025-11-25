package com.sistema.eventsapi.service.impl;

import com.sistema.eventsapi.dto.InscricaoRequisicao;
import com.sistema.eventsapi.dto.InscricaoResposta;
import com.sistema.eventsapi.entity.Evento;
import com.sistema.eventsapi.entity.Inscricao;
import com.sistema.eventsapi.exception.ApiException;
import com.sistema.eventsapi.repository.EventoRepository;
import com.sistema.eventsapi.repository.InscricaoRepository;
import com.sistema.eventsapi.service.InscricaoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscricaoServiceImpl implements InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final EventoRepository eventoRepository;

    public InscricaoServiceImpl(InscricaoRepository inscricaoRepository,
                                EventoRepository eventoRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.eventoRepository = eventoRepository;
    }

    @Override
    public InscricaoResposta inscrever(InscricaoRequisicao requisicao, String emailUsuario) {
        Evento evento = eventoRepository.findById(requisicao.getEventoId())
                .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

        // Verifica duplicidade de inscrição
        if (inscricaoRepository.existsByEventoIdAndUsuarioId(requisicao.getEventoId(), requisicao.getUsuarioId())) {
            throw new ApiException("Usuário já inscrito neste evento", HttpStatus.BAD_REQUEST);
        }

        // Inscrição do usuário no evento
        Inscricao inscricao = new Inscricao();
        inscricao.setEventoId(requisicao.getEventoId());
        inscricao.setUsuarioId(requisicao.getUsuarioId());
        inscricao.setStatus("INSCRITO");

        inscricaoRepository.save(inscricao);

        return mapear(inscricao);
    }

    @Override
    public List<InscricaoResposta> listarPorEvento(Long eventoId) {
        return inscricaoRepository.findByEventoId(eventoId)
                .stream()
                .map(this::mapear)
                .toList();
    }

    @Override
    public List<InscricaoResposta> listarPorUsuario(Long usuarioId) {
        return inscricaoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapear)
                .toList();
    }

    private InscricaoResposta mapear(Inscricao inscricao) {
        InscricaoResposta resp = new InscricaoResposta();
        resp.setId(inscricao.getId());
        resp.setEventoId(inscricao.getEventoId());
        resp.setUsuarioId(inscricao.getUsuarioId());
        resp.setStatus(inscricao.getStatus());
        return resp;
    }

    @Override
    public void cancelar(Long idInscricao) {
        Inscricao inscricao = inscricaoRepository.findById(idInscricao)
                .orElseThrow(() -> new ApiException("Inscrição não encontrada", HttpStatus.NOT_FOUND));

        inscricao.setStatus("CANCELADO");
        inscricaoRepository.save(inscricao);
    }
}
