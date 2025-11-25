package com.sistema.eventsapi.service.impl;

import com.sistema.eventsapi.dto.CheckinRequest;
import com.sistema.eventsapi.dto.CheckinResponse;
import com.sistema.eventsapi.entity.Evento;
import com.sistema.eventsapi.entity.Inscricao;
import com.sistema.eventsapi.entity.Presenca;
import com.sistema.eventsapi.exception.ApiException;
import com.sistema.eventsapi.repository.EventoRepository;
import com.sistema.eventsapi.repository.InscricaoRepository;
import com.sistema.eventsapi.repository.PresencaRepository;
import com.sistema.eventsapi.service.CheckinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckinServiceImpl implements CheckinService {

    private final PresencaRepository presencaRepository;
    private final InscricaoRepository inscricaoRepository;
    private final EventoRepository eventoRepository;

    @Override
    public CheckinResponse registrar(CheckinRequest req, String emailUsuario) {

        // Valida evento
        Evento evento = eventoRepository.findById(req.getEventoId())
                .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

        // Valida inscrição
        Inscricao inscricao = inscricaoRepository.findById(req.getInscricaoId())
                .orElseThrow(() -> new ApiException("Inscrição não encontrada", HttpStatus.NOT_FOUND));

        if (!inscricao.getEventoId().equals(evento.getId())) {
            throw new ApiException("Inscrição não pertence ao evento", HttpStatus.BAD_REQUEST);
        }

        // Marca inscrição como presente
        inscricao.setStatus("PRESENTE");
        inscricaoRepository.save(inscricao);

        // Cria registro de presença
        Presenca presenca = new Presenca();
        presenca.setEventoId(evento.getId());
        presenca.setUsuarioId(inscricao.getUsuarioId());
        presenca.setOffline(req.getOffline() != null ? req.getOffline() : Boolean.FALSE);

        presencaRepository.save(presenca);

        // Monta resposta
        CheckinResponse resp = new CheckinResponse();
        resp.setPresencaId(presenca.getId());
        resp.setEventoId(presenca.getEventoId());
        resp.setUsuarioId(presenca.getUsuarioId());
        resp.setOffline(presenca.getOffline());
        resp.setStatus("PRESENTE");

        return resp;
    }
}
