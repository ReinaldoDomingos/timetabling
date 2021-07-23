package br.ufms.cpcx.timetabling.dto;

import br.ufms.cpcx.timetabling.entity.Disciplina;
import br.ufms.cpcx.timetabling.entity.DisciplinaGradeHoraria;
import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import br.ufms.cpcx.timetabling.entity.Professor;

public class DisciplinaGradeHorariaDTO {

    private Long id;

    private GradeHoraria gradeHoraria;
    private Integer numero;


    private Disciplina disciplina;

    private Professor professor;

    private Integer semestre;

    private Long cargaHorariaSemanal;

    public DisciplinaGradeHorariaDTO(DisciplinaGradeHoraria disciplinaGradeHoraria) {
        this.id = disciplinaGradeHoraria.getId();
        this.gradeHoraria = disciplinaGradeHoraria.getGradeHoraria();
        this.disciplina = disciplinaGradeHoraria.getDisciplina();
        this.semestre = disciplinaGradeHoraria.getSemestre();
        this.cargaHorariaSemanal = disciplinaGradeHoraria.getCargaHorariaSemanal();
    }

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
