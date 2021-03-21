package br.ufms.cpcx.timetabling.service;

import br.ufms.cpcx.timetabling.entity.Professor;
import br.ufms.cpcx.timetabling.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;

    public Professor salvar(Professor professor) {
        if (professorRepository.existsProfessorByNome(professor.getNome()))
            throw new RuntimeException("Professor já existe.");

        return professorRepository.save(professor);
    }

    public List<Professor> buscarTodos() {
        return professorRepository.findAll();
    }

    public List<Professor> buscarTodos(Long codigo, String nome) {
        Professor professor = new Professor();
        professor.setCodigo(codigo);
        professor.setNome(nome);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase();

        Example<Professor> exemplo = Example.of(professor, exampleMatcher);


        return professorRepository.findAll(exemplo);
    }

    public Optional<Professor> buscarPorId(Long id) {
        return professorRepository.findById(id);
    }

    public void deletar(Long id) {
        professorRepository.deleteById(id);
    }

    public Object alterar(Long id, Professor professor) {
        if (id.equals(professor.getId()))
            throw new RuntimeException("Erro ao atualizar o registro.");

        return professorRepository.save(professor);
    }
}
