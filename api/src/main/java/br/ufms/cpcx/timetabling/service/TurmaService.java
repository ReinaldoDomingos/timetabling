package br.ufms.cpcx.timetabling.service;

import br.ufms.cpcx.timetabling.entity.Turma;
import br.ufms.cpcx.timetabling.filter.GenericFilter;
import br.ufms.cpcx.timetabling.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurmaService {

    @Autowired
    TurmaRepository turmaRepository;

    public Turma salvar(Turma turma) {
        if (turmaRepository.existsTurmaByCodigo(turma.getCodigo()))
            throw new RuntimeException("Turma já existe.");

        return turmaRepository.save(turma);
    }

    public List<Turma> buscarTodos() {
        return turmaRepository.findAll();
    }

    public Page<Turma> buscarTodos(GenericFilter filter) {
        return turmaRepository.findAll(filter.getPageRequest());
    }

    public Optional<Turma> buscarPorId(Long id) {
        return turmaRepository.findById(id);
    }

    public void deletar(Long id) {
        turmaRepository.deleteById(id);
    }

    public Object alterar(Long id, Turma turma) {
        if (!id.equals(turma.getId()))
            throw new RuntimeException("Erro ao atualizar o registro.");

        return turmaRepository.save(turma);
    }
}
