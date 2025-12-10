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

    // =============================
    // ✔ ONLINE (portal / admin)
    // =============================
    @Override
    public PresencaResposta registrar(PresencaRequisicao req) {

        Evento evento = eventoRepository.findById(req.getEventoId())
                .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

        Inscricao inscricao = inscricaoRepository.findById(req.getInscricaoId())
                .orElseThrow(() -> new ApiException("Inscrição não encontrada", HttpStatus.NOT_FOUND));

        if (!inscricao.getEventoId().equals(evento.getId())) {
            throw new ApiException("Inscrição não pertence ao evento", HttpStatus.BAD_REQUEST);
        }

        marcarPresencaEAtualizarEvento(evento, inscricao, false);

        Presenca presenca = salvarPresenca(evento.getId(), inscricao.getUsuarioId(), false);

        return mapear(presenca);
    }

    // =============================
    // 🚀 OFFLINE (GATE sincroniza depois)
    // =============================
    @Override
    public void registrarOffline(PresencaRequisicao req) {

        Evento evento = eventoRepository.findById(req.getEventoId())
                .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

        // 🔍 Aqui a diferença: buscar pela combinação
        Inscricao inscricao = inscricaoRepository
                .findByEmailUsuarioAndEventoId(req.getEmailUsuario(), req.getEventoId())
                .orElseThrow(() -> new ApiException("Usuário não inscrito neste evento", HttpStatus.NOT_FOUND));

        marcarPresencaEAtualizarEvento(evento, inscricao, true);

        salvarPresenca(evento.getId(), inscricao.getUsuarioId(), true);
    }

    private void marcarPresencaEAtualizarEvento(Evento evento, Inscricao inscricao, boolean offline) {
        inscricao.setStatus("PRESENTE");
        inscricaoRepository.save(inscricao);
    }

    private Presenca salvarPresenca(Long eventoId, Long usuarioId, boolean offline) {
        Presenca presenca = new Presenca();
        presenca.setEventoId(eventoId);
        presenca.setUsuarioId(usuarioId);
        presenca.setOffline(offline);
        return presencaRepository.save(presenca);
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
