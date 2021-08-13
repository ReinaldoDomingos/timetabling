package br.ufms.cpcx.gradehoraria.service;

import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaDTO;
import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaEdicaoDTO;
import br.ufms.cpcx.gradehoraria.entity.Disciplina;
import br.ufms.cpcx.gradehoraria.entity.DisciplinaGradeHoraria;
import br.ufms.cpcx.gradehoraria.entity.GradeHoraria;
import br.ufms.cpcx.gradehoraria.entity.Professor;
import br.ufms.cpcx.gradehoraria.entity.Turma;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.repository.DisciplinaGradeHorariaRepository;
import br.ufms.cpcx.gradehoraria.repository.DisciplinaRepository;
import br.ufms.cpcx.gradehoraria.repository.GradeHorariaRepository;
import br.ufms.cpcx.gradehoraria.repository.ProfessorRepository;
import br.ufms.cpcx.gradehoraria.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public DisciplinaGradeHorariaEdicaoDTO salvar(Long idGradeHoraria, Long idDisciplina) {
        GradeHoraria gradeHoraria = gradeHorariaRepository.getOne(idGradeHoraria);
        Disciplina disciplina = disciplinaRepository.getOne(idDisciplina);

        DisciplinaGradeHoraria disciplinaGradeHoraria = new DisciplinaGradeHoraria();
        disciplinaGradeHoraria.setDisciplina(disciplina);
        disciplinaGradeHoraria.setGradeHoraria(gradeHoraria);

        return new DisciplinaGradeHorariaEdicaoDTO(salvarDisciplinaGradeHoraria(disciplinaGradeHoraria));
    }

    private DisciplinaGradeHoraria salvarDisciplinaGradeHoraria(DisciplinaGradeHoraria disciplinaGradeHoraria) {
        return disciplinaGradeHorariaRepository.save(disciplinaGradeHoraria);
    }

    public List<DisciplinaGradeHorariaDTO> buscarTodas() {
        return disciplinaGradeHorariaRepository.buscarTodos()
                .stream().map(DisciplinaGradeHorariaDTO::new)
                .collect(Collectors.toList());
    }

    public Page<DisciplinaGradeHorariaEdicaoDTO> buscarPorGradeHorariaId(GenericFilter filter, Long idGradeHoraria) {
        PageRequest pageRequest = filter.getPageRequest();
        return disciplinaGradeHorariaRepository.buscarPorGradeHorariaId(idGradeHoraria, pageRequest);
    }

    public List<DisciplinaGradeHorariaEdicaoDTO> buscarTodasPorGradeHorariaId(Long idGradeHoraria) {
        return disciplinaGradeHorariaRepository.buscarPorGradeHorariaId(idGradeHoraria);
    }

    public DisciplinaGradeHorariaDTO buscarPorId(Long id) {
        return disciplinaGradeHorariaRepository.findById(id).map(DisciplinaGradeHorariaDTO::new).orElse(null);
    }

    public void deletar(Long id) {
        disciplinaGradeHorariaRepository.deleteById(id);
    }

    public DisciplinaGradeHorariaEdicaoDTO alterar(Long id, DisciplinaGradeHorariaEdicaoDTO disciplinaGradeHorariaEdicaoDTO) {
        validarDisciplinaGradeHoraria(id, disciplinaGradeHorariaEdicaoDTO);

        DisciplinaGradeHoraria disciplinaGradeHoraria = disciplinaGradeHorariaRepository.getOne(id);
        disciplinaGradeHoraria.setCargaHorariaSemanal(disciplinaGradeHorariaEdicaoDTO.getCargaHorariaSemanal());

        setProfessorDisciplinaGradeHoraria(disciplinaGradeHorariaEdicaoDTO, disciplinaGradeHoraria);
        setTurmaDisciplinaGradeHoraria(disciplinaGradeHorariaEdicaoDTO, disciplinaGradeHoraria);

        return new DisciplinaGradeHorariaEdicaoDTO(salvarDisciplinaGradeHoraria(disciplinaGradeHoraria));
    }

    private void setTurmaDisciplinaGradeHoraria(DisciplinaGradeHorariaEdicaoDTO disciplinaGradeHorariaEdicaoDTO, DisciplinaGradeHoraria disciplinaGradeHoraria) {
        Long idTurma = nonNull(disciplinaGradeHorariaEdicaoDTO.getTurma()) ? disciplinaGradeHorariaEdicaoDTO.getTurma().getId() : null;
        if (nonNull(idTurma)) {
            Turma turma = turmaRepository.getOne(idTurma);
            disciplinaGradeHoraria.setTurma(turma);
        }
    }

    private void setProfessorDisciplinaGradeHoraria(DisciplinaGradeHorariaEdicaoDTO disciplinaGradeHorariaEdicaoDTO, DisciplinaGradeHoraria disciplinaGradeHoraria) {
        Long idProfessor = nonNull(disciplinaGradeHorariaEdicaoDTO.getProfessor()) ? disciplinaGradeHorariaEdicaoDTO.getProfessor().getId() : null;
        if (nonNull(idProfessor)) {
            Professor professor = professorRepository.getOne(idProfessor);
            disciplinaGradeHoraria.setProfessor(professor);
        }
    }

    private void validarDisciplinaGradeHoraria(Long id, DisciplinaGradeHorariaEdicaoDTO disciplinaGradeHorariaEdicaoDTO) {
        if (!id.equals(disciplinaGradeHorariaEdicaoDTO.getIdDisciplinaGradeHoraria())) {
            throw new GenericException("Erro ao atualizar o disciplina grade horária.");
        }
    }
}
