package com.plataformaempregos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "competencia")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Competencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nome", unique = true)
    private String nome;
    
    @Column(name = "categoria")
    private String categoria; // TECNICA, COMPORTAMENTAL, IDIOMA
    
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
}
