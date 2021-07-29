package br.ufms.cpcx.timetabling.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TB_DISCIPLINA")
public class Disciplina {
    @Id
    @Column(name = "DIS_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DIS_NOME", length = 100)
    private String nome;

    @Column(name = "DIS_CODIGO", length = 14)
    private String codigo;

    @Column(name = "DIS_CARGA_HORARIA", length = 14)
    private Long cargaHoraria;
}
