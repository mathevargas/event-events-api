package com.sistema.eventsapi.repository;

import com.sistema.eventsapi.entity.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {

    List<Presenca> findByEventoId(Long eventoId);

    boolean existsByEventoIdAndUsuarioId(Long eventoId, Long usuarioId);
}

