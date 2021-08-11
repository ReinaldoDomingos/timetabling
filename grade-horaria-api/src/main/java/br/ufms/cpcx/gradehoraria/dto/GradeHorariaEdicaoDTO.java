package br.ufms.cpcx.gradehoraria.dto;

import br.ufms.cpcx.gradehoraria.entity.GradeHoraria;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GradeHorariaEdicaoDTO {
    private GradeHoraria gradeHoraria;
    private List<DisciplinaGradeHorariaEdicaoDTO> disciplinas;

    public GradeHorariaEdicaoDTO() {
    }

    public GradeHorariaEdicaoDTO(GradeHoraria gradeHoraria) {
        this.gradeHoraria = gradeHoraria;
    }
}
