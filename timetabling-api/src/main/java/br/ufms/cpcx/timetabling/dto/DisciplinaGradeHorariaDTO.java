package br.ufms.cpcx.timetabling.dto;

import br.ufms.cpcx.timetabling.entity.Disciplina;
import br.ufms.cpcx.timetabling.entity.DisciplinaGradeHoraria;
import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import br.ufms.cpcx.timetabling.entity.Professor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisciplinaGradeHorariaDTO {

    private Long id;
    private Integer numero;
    private Integer semestre;
    private Professor professor;
    private Disciplina disciplina;
    private Long cargaHorariaSemanal;
    private GradeHoraria gradeHoraria;

    public DisciplinaGradeHorariaDTO(DisciplinaGradeHoraria disciplinaGradeHoraria) {
        this.id = disciplinaGradeHoraria.getId();
        this.gradeHoraria = disciplinaGradeHoraria.getGradeHoraria();
        this.disciplina = disciplinaGradeHoraria.getDisciplina();
        this.semestre = disciplinaGradeHoraria.getSemestre();
        this.cargaHorariaSemanal = disciplinaGradeHoraria.getCargaHorariaSemanal();
    }
}
