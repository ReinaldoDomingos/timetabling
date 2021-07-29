package br.ufms.cpcx.timetabling.dto;

import br.ufms.cpcx.timetabling.entity.Professor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorDTO {
    private Long id;
    private String nome;
    private Long codigo;

    public ProfessorDTO() {
    }

    public ProfessorDTO(Professor professor) {
        this.id = professor.getId();
        this.nome = professor.getNome();
        this.codigo = professor.getCodigo();
    }
}
