package br.ufms.cpcx.timetabling.service;

import br.ufms.cpcx.timetabling.GenericFilter;
import br.ufms.cpcx.timetabling.dto.DisciplinaDTO;
import br.ufms.cpcx.timetabling.dto.DisciplinaGradeHorariaDTO;
import br.ufms.cpcx.timetabling.entity.Disciplina;
import br.ufms.cpcx.timetabling.entity.DisciplinaGradeHoraria;
import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import br.ufms.cpcx.timetabling.entity.Professor;
import br.ufms.cpcx.timetabling.exception.DisciplinaJaAlocadaNaGradaHorariaException;
import br.ufms.cpcx.timetabling.repository.DisciplinaGradeHorariaRepository;
import br.ufms.cpcx.timetabling.repository.DisciplinaRepository;
import br.ufms.cpcx.timetabling.repository.GradeHorariaRepository;
import br.ufms.cpcx.timetabling.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisciplinaGradeHorariaService {

    @Autowired
    private DisciplinaGradeHorariaRepository disciplinaGradeHorariaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private GradeHorariaRepository gradeHorariaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

//    public DisciplinaGradeHoraria alterarProfessor(Long idDisciplinaGradeHoraria, Long idProfessor) {
//       return disciplinaGradeHorariaRepository.alterarProfessor(idDisciplinaGradeHoraria, idProfessor);
//    }

    public DisciplinaDTO salvar(Long idGradeHoraria, Long idDisciplina) {
        if (disciplinaGradeHorariaRepository.existsByGradeHorariaIdAndDisciplinaId(idGradeHoraria, idDisciplina))
            throw new RuntimeException("Disciplina já existe nessa Grade Horária.");

        GradeHoraria gradeHoraria = gradeHorariaRepository.getOne(idGradeHoraria);
        Disciplina disciplina = disciplinaRepository.getOne(idDisciplina);

        DisciplinaGradeHoraria disciplinaGradeHoraria = new DisciplinaGradeHoraria();
        disciplinaGradeHoraria.setDisciplina(disciplina);
        disciplinaGradeHoraria.setGradeHoraria(gradeHoraria);

        return new DisciplinaDTO(disciplinaGradeHorariaRepository.save(disciplinaGradeHoraria));
    }

    public List<DisciplinaGradeHorariaDTO> buscarTodos() {
        return disciplinaGradeHorariaRepository.buscarTodos().stream().map(DisciplinaGradeHorariaDTO::new).collect(Collectors.toList());
    }

    public Page<DisciplinaDTO> buscarPorGradeHorariaId(GenericFilter filter, Long idGradeHoraria) {
        return disciplinaGradeHorariaRepository.findAllByGradeHorariaId(idGradeHoraria, filter.getPageRequest());
    }

    public Optional<DisciplinaGradeHoraria> buscarPorId(Long id) {
        return disciplinaGradeHorariaRepository.findById(id);
    }

    public void deletar(Long id) {
        disciplinaGradeHorariaRepository.deleteById(id);
    }

    public Object alterarProfessor(Long id, DisciplinaDTO disciplinaDTO) {
        if (!id.equals(disciplinaDTO.getIdDisciplinaGradeHoraria()))
            throw new RuntimeException("Erro ao atualizar o registro.");

        Long idProfessor = disciplinaDTO.getProfessor().getId();

        DisciplinaGradeHoraria disciplinaGradeHoraria = disciplinaGradeHorariaRepository.getOne(id);
        Professor professor = professorRepository.getOne(idProfessor);

        if (disciplinaGradeHorariaRepository.existsByGradeHorariaIdAndProfessorId(disciplinaGradeHoraria.getGradeHoraria().getId(),idProfessor)) {
            throw new DisciplinaJaAlocadaNaGradaHorariaException();
        }

        disciplinaGradeHoraria.setProfessor(professor);

        return new DisciplinaDTO(disciplinaGradeHorariaRepository.save(disciplinaGradeHoraria));
    }
}
