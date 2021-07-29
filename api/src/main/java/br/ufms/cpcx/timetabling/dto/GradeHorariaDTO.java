package br.ufms.cpcx.timetabling.dto;

import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GradeHorariaDTO {
    private GradeHoraria gradeHoraria;
    private List<DisciplinaDTO> disciplinas;

    public GradeHorariaDTO() {
    }
}
