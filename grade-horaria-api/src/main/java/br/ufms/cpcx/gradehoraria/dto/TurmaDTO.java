package br.ufms.cpcx.gradehoraria.dto;

import br.ufms.cpcx.gradehoraria.entity.Turma;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TurmaDTO {
    private Long id;
    private String nome;
    private String codigo;
    private Integer semestre;

    public TurmaDTO() {
    }

    public TurmaDTO(Turma turma) {
        this.id = turma.getId();
        this.nome = turma.getNome();
        this.codigo = turma.getCodigo();
        this.semestre = turma.getSemestre();
    }
}