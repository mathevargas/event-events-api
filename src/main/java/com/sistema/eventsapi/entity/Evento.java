package com.sistema.eventsapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String local;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false)
    private Integer limiteVagas;

    @Column(nullable = false)
    private String status; // "ATIVO", "ENCERRADO"
}
