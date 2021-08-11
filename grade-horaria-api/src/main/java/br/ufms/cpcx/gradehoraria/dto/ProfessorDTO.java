package br.ufms.cpcx.gradehoraria.dto;

import br.ufms.cpcx.gradehoraria.entity.Professor;
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

    public Professor getProfessor() {
        Professor professor = new Professor();

        professor.setId(this.id);
        professor.setNome(this.nome);
        professor.setCodigo(this.codigo);

        return professor;
    }
}
