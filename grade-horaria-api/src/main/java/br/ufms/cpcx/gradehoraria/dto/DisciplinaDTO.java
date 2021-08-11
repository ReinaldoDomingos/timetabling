package br.ufms.cpcx.gradehoraria.dto;

import br.ufms.cpcx.gradehoraria.entity.Disciplina;
import lombok.Data;

@Data
public class DisciplinaDTO {
    private Long id;
    private String nome;
    private String codigo;
    private Long cargaHoraria;

    public DisciplinaDTO() {
    }

    public DisciplinaDTO(Disciplina disciplina) {
        this.id = disciplina.getId();
        this.nome = disciplina.getNome();
        this.codigo = disciplina.getCodigo();
        this.cargaHoraria = disciplina.getCargaHoraria();
    }

    public Disciplina getDisciplina() {
        Disciplina disciplina = new Disciplina();

        disciplina.setId(this.id);
        disciplina.setNome(this.nome);
        disciplina.setCodigo(this.codigo);
        disciplina.setCargaHoraria(this.cargaHoraria);

        return disciplina;
    }
}
