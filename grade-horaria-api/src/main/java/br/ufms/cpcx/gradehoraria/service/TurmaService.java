package br.ufms.cpcx.gradehoraria.service;

import br.ufms.cpcx.gradehoraria.dto.TurmaDTO;
import br.ufms.cpcx.gradehoraria.entity.Turma;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    public TurmaDTO salvar(TurmaDTO turmaDTO) {
        if (turmaRepository.existsTurmaByCodigo(turmaDTO.getCodigo()))
            throw new GenericException("Turma já existe.");

        return salvarTurma(turmaDTO);
    }

    public List<Turma> buscarTodos() {
        return turmaRepository.findAll();
    }

    public Page<TurmaDTO> buscarTodos(GenericFilter filter) {
        return turmaRepository.findAll(filter.getPageRequest()).map(TurmaDTO::new);
    }

    public TurmaDTO buscarPorId(Long id) {
        return turmaRepository.findById(id).map(TurmaDTO::new).orElse(null);
    }

    public void deletar(Long id) {
        turmaRepository.deleteById(id);
    }

    public TurmaDTO alterar(Long id, TurmaDTO turmaDTO) {
        if (!id.equals(turmaDTO.getId()))
            throw new GenericException("Erro ao atualizar o registro.");

        return salvarTurma(turmaDTO);
    }

    private TurmaDTO salvarTurma(TurmaDTO turmaDTO) {
        Turma turmaSalva = turmaRepository.save(TurmaDTO.toMapTurma(turmaDTO));

        return new TurmaDTO(turmaSalva);
    }
}
