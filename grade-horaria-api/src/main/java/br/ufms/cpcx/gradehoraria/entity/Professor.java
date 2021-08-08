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
@Table(name = "TB_PROFESSOR")
public class Professor {
    @Id
    @Column(name = "PRO_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PRO_NOME", length = 100)
    private String nome;

    @Column(name = "PRO_CODIGO", length = 14)
    private Long codigo;
}
