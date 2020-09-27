package br.ufms.cpcx.timetabling.repository;

import br.ufms.cpcx.timetabling.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
