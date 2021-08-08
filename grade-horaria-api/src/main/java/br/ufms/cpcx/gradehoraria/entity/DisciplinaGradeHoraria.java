package br.ufms.cpcx.gradehoraria.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "TB_DISCIPLINA_GRADE_HORARIA")
public class DisciplinaGradeHoraria {
    @Id
    @Column(name = "DGH_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRA_ID")
    private GradeHoraria gradeHoraria;

    @Column(name = "DGH_NUMERO")
    private Integer numero;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIS_ID")
    private Disciplina disciplina;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRO_ID")
    private Professor professor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUR_ID")
    private Turma turma;

    @Column(name = "DGH_SEMESTRE")
    private Integer semestre;

    @Column(name = "DGH_CARGA_HORARIA_SEMANAL", length = 14)
    private Long cargaHorariaSemanal;

    @Column(name = "DGH_USA_LABORATORIO", length = 14)
    private Boolean usaLaboratorio;
}
