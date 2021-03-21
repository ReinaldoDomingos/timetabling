package br.ufms.cpcx.timetabling.entity;

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

    @Column(name = "DGH_SEMESTRE")
    private Integer semestre;

    @Column(name = "DGH_CARGA_HORARIA_SEMANAL", length = 14)
    private Long cargaHorariaSemanal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GradeHoraria getGradeHoraria() {
        return gradeHoraria;
    }

    public void setGradeHoraria(GradeHoraria gradeHoraria) {
        this.gradeHoraria = gradeHoraria;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Long getCargaHorariaSemanal() {
        return cargaHorariaSemanal;
    }

    public void setCargaHorariaSemanal(Long cargaHorariaSemanal) {
        this.cargaHorariaSemanal = cargaHorariaSemanal;
    }
}
