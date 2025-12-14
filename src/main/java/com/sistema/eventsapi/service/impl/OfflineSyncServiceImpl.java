package com.sistema.eventsapi.service.impl;

import com.sistema.eventsapi.dto.offline.OfflineInscricaoItem;
import com.sistema.eventsapi.dto.offline.OfflinePresencaItem;
import com.sistema.eventsapi.dto.offline.OfflineSyncRequest;
import com.sistema.eventsapi.dto.offline.OfflineSyncResponse;
import com.sistema.eventsapi.entity.Evento;
import com.sistema.eventsapi.entity.Inscricao;
import com.sistema.eventsapi.entity.Presenca;
import com.sistema.eventsapi.exception.ApiException;
import com.sistema.eventsapi.repository.EventoRepository;
import com.sistema.eventsapi.repository.InscricaoRepository;
import com.sistema.eventsapi.repository.PresencaRepository;
import com.sistema.eventsapi.service.OfflineSyncService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class OfflineSyncServiceImpl implements OfflineSyncService {

    private final InscricaoRepository inscricaoRepository;
    private final PresencaRepository presencaRepository;
    private final EventoRepository eventoRepository;

    private final WebClient emailClient;

    public OfflineSyncServiceImpl(
            InscricaoRepository inscricaoRepository,
            PresencaRepository presencaRepository,
            EventoRepository eventoRepository,
            @Value("${email.api.url:http://localhost:8002}") String emailApiUrl
    ) {
        this.inscricaoRepository = inscricaoRepository;
        this.presencaRepository = presencaRepository;
        this.eventoRepository = eventoRepository;
        this.emailClient = WebClient.builder().baseUrl(emailApiUrl).build();
    }

    @Override
    public OfflineSyncResponse sincronizar(OfflineSyncRequest req, String tokenJwtHeader) {

        String tokenJwt = tokenJwtHeader;
        if (tokenJwt != null && tokenJwt.startsWith("Bearer ")) {
            tokenJwt = tokenJwt.substring(7);
        }

        int inscricoesOk = 0;
        int presencasOk = 0;

        // 1) Inscrições
        if (req.getInscricoes() != null) {
            for (OfflineInscricaoItem item : req.getInscricoes()) {
                if (item == null || item.getEventoId() == null || item.getUsuarioId() == null) continue;

                Evento evento = eventoRepository.findById(item.getEventoId())
                        .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

                boolean jaExiste = inscricaoRepository.existsByEventoIdAndUsuarioId(item.getEventoId(), item.getUsuarioId());
                if (jaExiste) continue;

                Inscricao insc = new Inscricao();
                insc.setEventoId(item.getEventoId());
                insc.setUsuarioId(item.getUsuarioId());
                insc.setStatus("INSCRITO");
                insc.setEmailUsuario(item.getEmailUsuario());
                inscricaoRepository.save(insc);

                // e-mail inscrição (se tiver email e token)
                enviarEmailAssincrono(
                        item.getEmailUsuario(),
                        "Inscrição confirmada",
                        "<p>Você está inscrito no evento <strong>" + evento.getTitulo() + "</strong>.</p>",
                        tokenJwt
                );

                inscricoesOk++;
            }
        }

        // 2) Presenças / Check-in
        if (req.getPresencas() != null) {
            for (OfflinePresencaItem item : req.getPresencas()) {
                if (item == null || item.getEventoId() == null || item.getUsuarioId() == null) continue;

                Evento evento = eventoRepository.findById(item.getEventoId())
                        .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

                boolean jaExiste = presencaRepository.existsByEventoIdAndUsuarioId(item.getEventoId(), item.getUsuarioId());
                if (jaExiste) continue;

                // garantir inscrição
                Inscricao inscricao = inscricaoRepository.findByEventoIdAndUsuarioId(item.getEventoId(), item.getUsuarioId())
                        .orElseGet(() -> {
                            Inscricao nova = new Inscricao();
                            nova.setEventoId(item.getEventoId());
                            nova.setUsuarioId(item.getUsuarioId());
                            nova.setStatus("INSCRITO");
                            nova.setEmailUsuario(item.getEmailUsuario());
                            return inscricaoRepository.save(nova);
                        });

                // marcar presente
                inscricao.setStatus("PRESENTE");
                if (inscricao.getEmailUsuario() == null) inscricao.setEmailUsuario(item.getEmailUsuario());
                inscricaoRepository.save(inscricao);

                Presenca pres = new Presenca();
                pres.setEventoId(item.getEventoId());
                pres.setUsuarioId(item.getUsuarioId());
                pres.setOffline(Boolean.TRUE);
                presencaRepository.save(pres);

                // e-mail comparecimento (se tiver email e token)
                enviarEmailAssincrono(
                        item.getEmailUsuario(),
                        "Check-in confirmado",
                        "<p>Seu comparecimento no evento <strong>" + evento.getTitulo() + "</strong> foi registrado.</p>",
                        tokenJwt
                );

                presencasOk++;
            }
        }

        return new OfflineSyncResponse(inscricoesOk, presencasOk);
    }

    private void enviarEmailAssincrono(String email, String assunto, String mensagem, String tokenJwt) {
        if (email == null || tokenJwt == null) return;

        emailClient.post()
                .uri("/emails/enviar-html")
                .header("Authorization", "Bearer " + tokenJwt)
                .bodyValue(Map.of(
                        "destinatario", email,
                        "assunto", assunto,
                        "mensagem", mensagem
                ))
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}
