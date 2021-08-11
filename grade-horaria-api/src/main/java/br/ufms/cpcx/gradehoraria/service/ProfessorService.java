package br.ufms.cpcx.gradehoraria.service;

import br.ufms.cpcx.gradehoraria.dto.ProfessorDTO;
import br.ufms.cpcx.gradehoraria.entity.Professor;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public ProfessorDTO salvar(ProfessorDTO professorDTO) {
        if (professorRepository.existsProfessorByNome(professorDTO.getNome()))
            throw new GenericException("Professor já existe.");

        return salvarProfessor(professorDTO);
    }

    public List<ProfessorDTO> buscarTodos() {
        return professorRepository.findAll().stream().map(ProfessorDTO::new)
                .sorted(Comparator.comparing(ProfessorDTO::getNome))
                .collect(Collectors.toList());
    }

    public Page<ProfessorDTO> buscarTodos(GenericFilter filter) {
        return professorRepository.findAll(filter.getPageRequest()).map(ProfessorDTO::new);
    }

    public ProfessorDTO buscarPorId(Long id) {
        return professorRepository.findById(id).map(ProfessorDTO::new).orElse(null);
    }

    public void deletar(Long id) {
        professorRepository.deleteById(id);
    }

    public ProfessorDTO alterar(Long id, ProfessorDTO professorDTO) {
        if (!id.equals(professorDTO.getId()))
            throw new GenericException("Erro ao atualizar o registro.");

        return salvarProfessor(professorDTO);
    }

    private ProfessorDTO salvarProfessor(ProfessorDTO professor) {
        Professor professorSalvo = professorRepository.save(professor.getProfessor());

        return new ProfessorDTO(professorSalvo);
    }
}
