package br.ufms.cpcx.gradehoraria.dto;

import br.ufms.cpcx.gradehoraria.entity.Turma;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;

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

    public static Turma toMapTurma(TurmaDTO turmaDTO) {
        ModelMapper mapper = new ModelMapper();

        return mapper.map(turmaDTO, Turma.class);
    }
}
