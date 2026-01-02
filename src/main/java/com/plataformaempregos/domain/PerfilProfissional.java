package com.plataformaempregos.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "perfil_profissional")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilProfissional {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "resumo_profissional", columnDefinition = "TEXT")
    private String resumoProfissional;
    
    @Column(name = "area_atuacao")
    private String areaAtuacao;
    
    @Column(name = "pretensao_salarial")
    private Double pretensaoSalarial;
    
    @Column(name = "disponibilidade")
    private String disponibilidade; // REMOTO, PRESENCIAL, HIBRIDO
    
    @OneToMany(mappedBy = "perfilProfissional", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExperienciaProfissional> experiencias;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "perfil_competencia",
        joinColumns = @JoinColumn(name = "perfil_id"),
        inverseJoinColumns = @JoinColumn(name = "competencia_id")
    )
    private List<Competencia> competencias;
    
    @Column(name = "score_ia")
    private Double scoreIA; // Score calculado pela IA baseado no perfil
}
