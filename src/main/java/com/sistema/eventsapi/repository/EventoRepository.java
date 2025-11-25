package com.sistema.eventsapi.repository;

import com.sistema.eventsapi.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}

