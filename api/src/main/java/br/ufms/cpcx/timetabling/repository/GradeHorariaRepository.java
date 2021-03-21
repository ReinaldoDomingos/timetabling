package br.ufms.cpcx.timetabling.repository;

import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import br.ufms.cpcx.timetabling.enumaration.ESemestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeHorariaRepository extends JpaRepository<GradeHoraria, Long> {
    Boolean existsGradeHorariaByAnoAndSemestreAno(Integer ano, ESemestre semestreAno);
}
