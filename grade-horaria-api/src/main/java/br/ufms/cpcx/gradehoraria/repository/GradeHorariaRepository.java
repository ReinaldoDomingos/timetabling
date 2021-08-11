package br.ufms.cpcx.gradehoraria.repository;

import br.ufms.cpcx.gradehoraria.entity.GradeHoraria;
import br.ufms.cpcx.gradehoraria.enumaration.ESemestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeHorariaRepository extends JpaRepository<GradeHoraria, Long> {
    boolean existsGradeHorariaByAnoAndSemestreAno(Integer ano, ESemestre semestreAno);

    @Query("SELECT gh FROM GradeHoraria gh WHERE gh.id = ?1")
    GradeHoraria buscarPorId(Long id);
}
