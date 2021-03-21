package br.ufms.cpcx.timetabling.entity;

import br.ufms.cpcx.timetabling.enumaration.ESemestre;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public ESemestre getSemestreAno() {
        return semestreAno;
    }

    public void setSemestreAno(ESemestre semestreAno) {
        this.semestreAno = semestreAno;
    }
}
