package br.ufms.cpcx.gradehoraria.dto;

import br.ufms.cpcx.gradehoraria.entity.GradeHoraria;
import br.ufms.cpcx.gradehoraria.enumaration.ESemestre;
import lombok.Data;

@Data
public class GradeHorariaDTO {
    private Long id;
    private Integer ano;
    private ESemestre semestreAno;

    public GradeHorariaDTO() {
    }

    public GradeHorariaDTO(GradeHoraria gradeHoraria) {
        this.id = gradeHoraria.getId();
        this.ano = gradeHoraria.getAno();
        this.semestreAno = gradeHoraria.getSemestreAno();
    }

    public GradeHoraria getGradeHoraria(){
        GradeHoraria gradeHoraria = new GradeHoraria();

        gradeHoraria.setId(this.id);
        gradeHoraria.setAno(this.ano);
        gradeHoraria.setSemestreAno(this.semestreAno);

        return gradeHoraria;
    }
}
