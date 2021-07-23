package br.ufms.cpcx.timetabling.service;

import br.ufms.cpcx.timetabling.GenericFilter;
import br.ufms.cpcx.timetabling.entity.Disciplina;
import br.ufms.cpcx.timetabling.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaService {

    @Autowired
    DisciplinaRepository disciplinaRepository;

    public Disciplina salvar(Disciplina disciplina) {
        if (disciplinaRepository.existsDisciplinaByCodigo(disciplina.getCodigo()))
            throw new RuntimeException("Disciplina já existe.");

        return disciplinaRepository.save(disciplina);
    }

    public List<Disciplina> buscarTodos() {
        return disciplinaRepository.findAll();
    }

    //    public Page<Disciplina> buscarTodos(String codigo, String nome, Long cargaHoraria) {
    public Page<Disciplina> buscarTodos(GenericFilter filter) {
//        Disciplina disciplina = new Disciplina();
//        disciplina.setCodigo(codigo);
//        disciplina.setNome(nome);
//        disciplina.setCargaHoraria(cargaHoraria);
//
//        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase();
//
//        Example<Disciplina> exemplo = Example.of(disciplina, exampleMatcher);

        return disciplinaRepository.findAll(filter.getPageRequest());
    }

    public Optional<Disciplina> buscarPorId(Long id) {
        return disciplinaRepository.findById(id);
    }

    public void deletar(Long id) {
        disciplinaRepository.deleteById(id);
    }

    public Object alterar(Long id, Disciplina disciplina) {
        if (!id.equals(disciplina.getId()))
            throw new RuntimeException("Erro ao atualizar o registro.");

        return disciplinaRepository.save(disciplina);
    }
}
