package br.ufms.cpcx.gradehoraria.entity;

import br.ufms.cpcx.gradehoraria.enumaration.ESemestre;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "TB_GRADE_HORARIA")
public class GradeHoraria {
    @Id
    @Column(name = "GRA_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "GRA_ANO", length = 100)
    private Integer ano;

    @Column(name = "GRA_SEMESTRE_ANO")
    private ESemestre semestreAno;
}
