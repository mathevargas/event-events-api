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
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class InscricaoServiceImpl implements InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final EventoRepository eventoRepository;

    private final WebClient emailClient = WebClient.builder()
            .baseUrl("http://localhost:8002")
            .build();

    public InscricaoServiceImpl(InscricaoRepository inscricaoRepository,
                                EventoRepository eventoRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.eventoRepository = eventoRepository;
    }

    @Override
    public InscricaoResposta inscrever(InscricaoRequisicao requisicao, String emailUsuario, String tokenJwt) {

        if (tokenJwt != null && tokenJwt.startsWith("Bearer "))
            tokenJwt = tokenJwt.substring(7);

        Evento evento = eventoRepository.findById(requisicao.getEventoId())
                .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

        List<Inscricao> inscricoes = inscricaoRepository.findByEventoId(evento.getId())
                .stream()
                .filter(i -> i.getUsuarioId().equals(requisicao.getUsuarioId()))
                .toList();

        if (!inscricoes.isEmpty()) {
            Inscricao existente = inscricoes.get(0);

            if ("CANCELADO".equals(existente.getStatus())) {
                existente.setStatus("INSCRITO");
                existente.setEmailUsuario(emailUsuario);
                inscricaoRepository.save(existente);

                enviarEmailAssincrono(emailUsuario, evento.getTitulo(), "Inscrição confirmada",
                        "<p>Você está inscrito no evento <strong>" + evento.getTitulo() + "</strong>.</p>",
                        tokenJwt);

                return mapear(existente);
            }

            throw new ApiException("Usuário já inscrito neste evento", HttpStatus.BAD_REQUEST);
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setEventoId(requisicao.getEventoId());
        inscricao.setUsuarioId(requisicao.getUsuarioId());
        inscricao.setStatus("INSCRITO");
        inscricao.setEmailUsuario(emailUsuario);

        inscricaoRepository.save(inscricao);

        enviarEmailAssincrono(emailUsuario, evento.getTitulo(), "Inscrição confirmada",
                "<p>Você está inscrito no evento <strong>" + evento.getTitulo() + "</strong>.</p>",
                tokenJwt);

        return mapear(inscricao);
    }

    @Override
    public void cancelar(Long idInscricao, String tokenJwt) {

        if (tokenJwt != null && tokenJwt.startsWith("Bearer "))
            tokenJwt = tokenJwt.substring(7);

        Inscricao inscricao = inscricaoRepository.findById(idInscricao)
                .orElseThrow(() -> new ApiException("Inscrição não encontrada", HttpStatus.NOT_FOUND));

        Evento evento = eventoRepository.findById(inscricao.getEventoId())
                .orElseThrow(() -> new ApiException("Evento não encontrado", HttpStatus.NOT_FOUND));

        inscricao.setStatus("CANCELADO");
        inscricaoRepository.save(inscricao);

        enviarEmailAssincrono(inscricao.getEmailUsuario(), evento.getTitulo(), "Cancelamento de inscrição",
                "<p>Inscrição cancelada no evento <strong>" + evento.getTitulo() + "</strong>.</p>",
                tokenJwt);
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

    private void enviarEmailAssincrono(String email, String eventoTitulo, String assunto, String mensagem, String tokenJwt) {
        if (email == null || tokenJwt == null) {
            return;
        }

        // 🔥 Envio assíncrono — NÃO trava a resposta para o front!
        emailClient.post()
                .uri("/emails/enviar-html")
                .header("Authorization", "Bearer " + tokenJwt)
                .bodyValue(Map.of(
                        "destinatario", email,
                        "assunto", assunto,
                        "mensagem", mensagem
                ))
                .retrieve()
                .toBodilessEntity()  // não precisa processar retorno
                .subscribe(); // ⬅ dispara sem bloquear
    }
}
