package br.ufms.cpcx.timetabling.repository;

import br.ufms.cpcx.timetabling.dto.DisciplinaDTO;
import br.ufms.cpcx.timetabling.entity.DisciplinaGradeHoraria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaGradeHorariaRepository extends JpaRepository<DisciplinaGradeHoraria, Long> {

    @Query("SELECT dgh FROM DisciplinaGradeHoraria dgh " +
            "LEFT JOIN FETCH dgh.gradeHoraria JOIN FETCH dgh.disciplina dis")
    List<DisciplinaGradeHoraria> buscarTodos();

    @Query("SELECT dgh FROM DisciplinaGradeHoraria dgh WHERE dgh.gradeHoraria.id = ?1")
    Page<DisciplinaDTO> findAllByGradeHorariaId(Long idGradeHoraria, Pageable pageable);

    Boolean existsByGradeHorariaIdAndDisciplinaId(Long idDisciplina, Long idGradeHoraria);

    Boolean existsByGradeHorariaIdAndProfessorId(Long idGradeHoraria,Long idProfessor);

    void deleteByGradeHorariaId(Long idGradeHoraria);

//    @Query("UPDATE DisciplinaGradeHoraria dgh SET dgh.professor.id = ?2 WHERE dgh.id = ?1 ")
//    DisciplinaGradeHoraria alterarProfessor(Long idDisciplinaGradeHoraria, Long idProfessor);
}
