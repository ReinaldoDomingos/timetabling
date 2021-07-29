package br.ufms.cpcx.timetabling.dto;

import br.ufms.cpcx.timetabling.entity.DisciplinaGradeHoraria;
import lombok.AllArgsConstructor;
import lombok.Data;

import static java.util.Objects.nonNull;

@Data
@AllArgsConstructor
public class DisciplinaDTO {

    private Long id;
    private String nome;
    private String codigo;
    private TurmaDTO turma;
    private Long cargaHoraria;
    private ProfessorDTO professor;
    private Boolean usaLaboratorio;
    private Long cargaHorariaSemanal;
    private Long idDisciplinaGradeHoraria;

    public DisciplinaDTO() {
    }

    public DisciplinaDTO(DisciplinaGradeHoraria disciplinaGradeHoraria) {
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
