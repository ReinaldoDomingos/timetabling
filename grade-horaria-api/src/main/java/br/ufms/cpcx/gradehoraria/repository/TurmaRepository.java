package br.ufms.cpcx.gradehoraria.repository;

import br.ufms.cpcx.gradehoraria.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    boolean existsTurmaByCodigo(String codigo);
}
