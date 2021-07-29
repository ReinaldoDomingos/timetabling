package br.ufms.cpcx.timetabling.repository;

import br.ufms.cpcx.timetabling.entity.Turma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    Boolean existsTurmaByCodigo(String codigo);

    @Query("SELECT tu FROM Turma tu")
    Page<Turma> bucarTodos(Pageable pageable);
}
