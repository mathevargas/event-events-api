package com.sistema.eventsapi.service.impl;

import com.sistema.eventsapi.dto.PresencaRequisicao;
import com.sistema.eventsapi.dto.PresencaResposta;
import com.sistema.eventsapi.entity.Evento;
import com.sistema.eventsapi.entity.Inscricao;
import com.sistema.eventsapi.entity.Presenca;
import com.sistema.eventsapi.exception.ApiException;
import com.sistema.eventsapi.repository.EventoRepository;
import com.sistema.eventsapi.repository.InscricaoRepository;
import com.sistema.eventsapi.repository.PresencaRepository;
import com.sistema.eventsapi.service.PresencaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresencaServiceImpl implements PresencaService {

    private final PresencaRepository presencaRepository;
    private final InscricaoRepository inscricaoRepository;
    private final EventoRepository eventoRepository;

    @Override
    public PresencaResposta registrar(PresencaRequisicao req) {

        Evento evento = eventoRepository.findById(req.getEventoId())
                .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

        Inscricao inscricao = inscricaoRepository.findById(req.getInscricaoId())
                .orElseThrow(() -> new ApiException("Inscrição não encontrada", HttpStatus.NOT_FOUND));

        if (!inscricao.getEventoId().equals(evento.getId())) {
            throw new ApiException("Inscrição não pertence ao evento", HttpStatus.BAD_REQUEST);
        }

        // Aqui podemos ou não mudar status; normalmente o checkin já cuida disso
        inscricao.setStatus("PRESENTE");
        inscricaoRepository.save(inscricao);

        // Cria presença
        Presenca presenca = new Presenca();
        presenca.setEventoId(evento.getId());
        presenca.setUsuarioId(inscricao.getUsuarioId());
        presenca.setOffline(req.getOffline() != null ? req.getOffline() : Boolean.FALSE);

        presencaRepository.save(presenca);

        return mapear(presenca);
    }

    @Override
    public List<PresencaResposta> listarPorEvento(Long eventoId) {
        return presencaRepository.findByEventoId(eventoId)
                .stream()
                .map(this::mapear)
                .toList();
    }

    private PresencaResposta mapear(Presenca p) {
        PresencaResposta resp = new PresencaResposta();
        resp.setId(p.getId());
        resp.setEventoId(p.getEventoId());
        resp.setUsuarioId(p.getUsuarioId());
        resp.setOffline(p.getOffline());
        return resp;
    }
}
