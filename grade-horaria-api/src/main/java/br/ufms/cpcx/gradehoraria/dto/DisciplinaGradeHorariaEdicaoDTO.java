package br.ufms.cpcx.gradehoraria.dto;

import br.ufms.cpcx.gradehoraria.entity.DisciplinaGradeHoraria;
import lombok.AllArgsConstructor;
import lombok.Data;

import static java.util.Objects.nonNull;

@Data
@AllArgsConstructor
public class DisciplinaGradeHorariaEdicaoDTO {

    private Long id;
    private String nome;
    private String codigo;
    private TurmaDTO turma;
    private Long cargaHoraria;
    private ProfessorDTO professor;
    private Boolean usaLaboratorio;
    private Long cargaHorariaSemanal;
    private Long idDisciplinaGradeHoraria;

    public DisciplinaGradeHorariaEdicaoDTO() {
    }

    public DisciplinaGradeHorariaEdicaoDTO(DisciplinaGradeHoraria disciplinaGradeHoraria) {
        this.id = disciplinaGradeHoraria.getDisciplina().getId();
        this.nome = disciplinaGradeHoraria.getDisciplina().getNome();
        this.idDisciplinaGradeHoraria = disciplinaGradeHoraria.getId();
        this.codigo = disciplinaGradeHoraria.getDisciplina().getCodigo();
        this.cargaHoraria = disciplinaGradeHoraria.getDisciplina().getCargaHoraria();
        this.cargaHorariaSemanal = disciplinaGradeHoraria.getCargaHorariaSemanal();
        this.usaLaboratorio = disciplinaGradeHoraria.getUsaLaboratorio();

        if (nonNull(disciplinaGradeHoraria.getProfessor())) {
            disciplinaGradeHoraria.getProfessor().getId();
            this.professor = new ProfessorDTO(disciplinaGradeHoraria.getProfessor());
        }

        if (nonNull(disciplinaGradeHoraria.getTurma())) {
            disciplinaGradeHoraria.getTurma().getId();
            this.turma = new TurmaDTO(disciplinaGradeHoraria.getTurma());
        }
    }
}
