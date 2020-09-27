package br.ufms.cpcx.timetabling.repository;

import br.ufms.cpcx.timetabling.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    Boolean existsDisciplinaByCodigo(String codigo);
}
