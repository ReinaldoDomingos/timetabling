package br.ufms.cpcx.gradehoraria.repository;

import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaEdicaoDTO;
import br.ufms.cpcx.gradehoraria.entity.DisciplinaGradeHoraria;
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
    Page<DisciplinaGradeHorariaEdicaoDTO> findAllByGradeHorariaId(Long idGradeHoraria, Pageable pageable);

    @Query("SELECT dgh FROM DisciplinaGradeHoraria dgh WHERE dgh.gradeHoraria.id = ?1")
    List<DisciplinaGradeHorariaEdicaoDTO> findAllByGradeHorariaId(Long idGradeHoraria);

    Boolean existsByGradeHorariaIdAndDisciplinaId(Long idDisciplina, Long idGradeHoraria);

    boolean existsByGradeHorariaIdAndProfessorId(Long idGradeHoraria, Long idProfessor);

    void deleteByGradeHorariaId(Long idGradeHoraria);

    boolean existsByGradeHorariaIdAndTurmaId(Long idGradeHoraria, Long idTurma);
}
