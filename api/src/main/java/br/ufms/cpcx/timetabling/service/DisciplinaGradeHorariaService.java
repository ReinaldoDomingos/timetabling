package br.ufms.cpcx.timetabling.service;

import br.ufms.cpcx.timetabling.dto.DisciplinaDTO;
import br.ufms.cpcx.timetabling.dto.DisciplinaGradeHorariaDTO;
import br.ufms.cpcx.timetabling.entity.Disciplina;
import br.ufms.cpcx.timetabling.entity.DisciplinaGradeHoraria;
import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import br.ufms.cpcx.timetabling.entity.Professor;
import br.ufms.cpcx.timetabling.entity.Turma;
import br.ufms.cpcx.timetabling.filter.GenericFilter;
import br.ufms.cpcx.timetabling.repository.DisciplinaGradeHorariaRepository;
import br.ufms.cpcx.timetabling.repository.DisciplinaRepository;
import br.ufms.cpcx.timetabling.repository.GradeHorariaRepository;
import br.ufms.cpcx.timetabling.repository.ProfessorRepository;
import br.ufms.cpcx.timetabling.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

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

    @Autowired
    private TurmaRepository turmaRepository;

    public DisciplinaDTO salvar(Long idGradeHoraria, Long idDisciplina) {
//        if (disciplinaGradeHorariaRepository.existsByGradeHorariaIdAndDisciplinaId(idGradeHoraria, idDisciplina))
//            throw new RuntimeException("Disciplina já existe nessa Grade Horária.");

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

    public List<DisciplinaDTO> buscarTodasPorGradeHorariaId(Long idGradeHoraria) {
        return disciplinaGradeHorariaRepository.findAllByGradeHorariaId(idGradeHoraria);
    }

    public Optional<DisciplinaGradeHoraria> buscarPorId(Long id) {
        return disciplinaGradeHorariaRepository.findById(id);
    }

    public void deletar(Long id) {
        disciplinaGradeHorariaRepository.deleteById(id);
    }

    public Object alterar(Long id, DisciplinaDTO disciplinaDTO) {
        if (!id.equals(disciplinaDTO.getIdDisciplinaGradeHoraria()))
            throw new RuntimeException("Erro ao atualizar o registro.");


        DisciplinaGradeHoraria disciplinaGradeHoraria = disciplinaGradeHorariaRepository.getOne(id);
        disciplinaGradeHoraria.setCargaHorariaSemanal(disciplinaDTO.getCargaHorariaSemanal());

        Long idProfessor = nonNull(disciplinaDTO.getProfessor()) ? disciplinaDTO.getProfessor().getId() : null;
        if (nonNull(idProfessor)) {
            Professor professor = professorRepository.getOne(idProfessor);
            disciplinaGradeHoraria.setProfessor(professor);
        }
        Long idTurma = nonNull(disciplinaDTO.getTurma()) ? disciplinaDTO.getTurma().getId() : null;
        if (nonNull(idTurma)) {
            Turma turma = turmaRepository.getOne(idTurma);
            disciplinaGradeHoraria.setTurma(turma);
        }

        return new DisciplinaDTO(disciplinaGradeHorariaRepository.save(disciplinaGradeHoraria));
    }
}
