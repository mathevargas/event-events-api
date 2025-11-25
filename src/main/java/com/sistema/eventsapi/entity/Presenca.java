package com.sistema.eventsapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "presencas")
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eventoId;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Boolean offline; // true = veio do modo offline

}
