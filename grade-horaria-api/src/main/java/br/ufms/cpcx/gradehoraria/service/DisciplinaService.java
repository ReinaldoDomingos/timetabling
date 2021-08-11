package br.ufms.cpcx.gradehoraria.service;

import br.ufms.cpcx.gradehoraria.dto.DisciplinaDTO;
import br.ufms.cpcx.gradehoraria.entity.Disciplina;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    public DisciplinaDTO salvar(DisciplinaDTO disciplina) {
        if (disciplinaRepository.existsDisciplinaByCodigo(disciplina.getCodigo())) {
            throw new GenericException("Disciplina já existe.");
        }

        return salvarDisciplina(disciplina.getDisciplina());
    }

    public List<DisciplinaDTO> buscarTodas() {
        return disciplinaRepository.findAll().stream().map(DisciplinaDTO::new).collect(Collectors.toList());
    }

    public Page<DisciplinaDTO> buscarTodas(GenericFilter filter) {
        return disciplinaRepository.findAll(filter.getPageRequest()).map(DisciplinaDTO::new);
    }

    public DisciplinaDTO buscarPorId(Long id) {
        return disciplinaRepository.findById(id).map(DisciplinaDTO::new).orElse(null);
    }

    public void deletar(Long id) {
        disciplinaRepository.deleteById(id);
    }

    public DisciplinaDTO alterar(Long id, Disciplina disciplina) {
        if (!id.equals(disciplina.getId()))
            throw new GenericException("Erro ao atualizar o registro.");

        return salvarDisciplina(disciplina);
    }

    private DisciplinaDTO salvarDisciplina(Disciplina disciplina) {
        Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);

        return new DisciplinaDTO(disciplinaSalva);
    }
}
