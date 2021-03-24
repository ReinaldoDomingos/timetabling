package br.ufms.cpcx.timetabling.repository;

import br.ufms.cpcx.timetabling.entity.DisciplinaGradeHoraria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinaGradeHorariaRepository extends JpaRepository<DisciplinaGradeHoraria, Long> {
    @Query("SELECT dgh FROM DisciplinaGradeHoraria dgh " +
            "LEFT JOIN FETCH dgh.gradeHoraria JOIN FETCH dgh.disciplina dis")
    List<DisciplinaGradeHoraria> buscarTodos();

    Boolean existsByGradeHorariaIdAndDisciplinaId(Long idDisciplina, Long idGradeHoraria);
}
