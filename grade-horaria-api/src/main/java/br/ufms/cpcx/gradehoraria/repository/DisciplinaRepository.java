package br.ufms.cpcx.gradehoraria.repository;

import br.ufms.cpcx.gradehoraria.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    boolean existsDisciplinaByCodigo(String codigo);
}
