package br.ufms.cpcx.gradehoraria.repository;

import br.ufms.cpcx.gradehoraria.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsProfessorByNome(String nome);
}
