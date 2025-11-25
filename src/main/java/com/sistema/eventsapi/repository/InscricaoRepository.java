package com.sistema.eventsapi.repository;

import com.sistema.eventsapi.entity.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByEventoId(Long eventoId);

    List<Inscricao> findByUsuarioId(Long usuarioId);

    boolean existsByEventoIdAndUsuarioId(Long eventoId, Long usuarioId);
}
