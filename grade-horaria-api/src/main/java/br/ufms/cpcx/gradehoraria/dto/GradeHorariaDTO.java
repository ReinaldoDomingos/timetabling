package br.ufms.cpcx.gradehoraria.dto;

import br.ufms.cpcx.gradehoraria.entity.GradeHoraria;
import br.ufms.cpcx.gradehoraria.enumaration.ESemestre;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Setter
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public ESemestre getSemestreAno() {
        return semestreAno;
    }

    public void setSemestreAno(ESemestre semestreAno) {
        this.semestreAno = semestreAno;
    }

    public static GradeHoraria toMapGradeHoraria(GradeHorariaDTO gradeHorariaDTO) {
        ModelMapper mapper = new ModelMapper();

        return mapper.map(gradeHorariaDTO, GradeHoraria.class);
    }
}
