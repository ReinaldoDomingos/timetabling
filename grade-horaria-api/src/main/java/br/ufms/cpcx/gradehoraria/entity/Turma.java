package br.ufms.cpcx.gradehoraria.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "TB_TURMA")
public class Turma {
    @Id
    @Column(name = "TUR_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TUR_NOME", length = 100)
    private String nome;

    @Column(name = "TUR_CODIGO", length = 14)
    private String codigo;

    @Column(name = "TUR_SEMESTRE")
    private Integer semestre;
}
