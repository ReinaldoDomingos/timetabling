package br.ufms.cpcx.timetabling.repository;

import br.ufms.cpcx.timetabling.entity.Disciplina;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    Boolean existsDisciplinaByCodigo(String codigo);

    @Query("SELECT di FROM Disciplina di")
    Page<Disciplina> bucarTodos(Pageable pageable);
}
