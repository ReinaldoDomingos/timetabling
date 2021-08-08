package br.ufms.cpcx.gradehoraria.dto;

import br.ufms.cpcx.gradehoraria.entity.Disciplina;
import br.ufms.cpcx.gradehoraria.entity.DisciplinaGradeHoraria;
import br.ufms.cpcx.gradehoraria.entity.GradeHoraria;
import br.ufms.cpcx.gradehoraria.entity.Professor;
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
