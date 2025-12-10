package com.sistema.eventsapi.repository;

import com.sistema.eventsapi.entity.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByEventoId(Long eventoId);

    List<Inscricao> findByUsuarioId(Long usuarioId);

    boolean existsByEventoIdAndUsuarioId(Long eventoId, Long usuarioId);

    // 🚀 Usado pela GATE no modo offline (email + evento)
    Optional<Inscricao> findByEmailUsuarioAndEventoId(String emailUsuario, Long eventoId);
}
