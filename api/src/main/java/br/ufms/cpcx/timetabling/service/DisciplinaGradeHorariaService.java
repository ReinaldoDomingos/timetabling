package br.ufms.cpcx.timetabling.service;

import br.ufms.cpcx.timetabling.dto.DisciplinaGradeHorariaDTO;
import br.ufms.cpcx.timetabling.entity.Disciplina;
import br.ufms.cpcx.timetabling.entity.DisciplinaGradeHoraria;
import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import br.ufms.cpcx.timetabling.repository.DisciplinaGradeHorariaRepository;
import br.ufms.cpcx.timetabling.repository.DisciplinaRepository;
import br.ufms.cpcx.timetabling.repository.GradeHorariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisciplinaGradeHorariaService {

    @Autowired
    DisciplinaGradeHorariaRepository disciplinaGradeHorariaRepository;

    @Autowired
    GradeHorariaRepository gradeHorariaRepository;

    @Autowired
    DisciplinaRepository disciplinaRepository;

    public DisciplinaGradeHoraria salvar(Long idGradeHoraria, Long idDisciplina) {
        if (disciplinaGradeHorariaRepository.existsByGradeHorariaIdAndDisciplinaId(idGradeHoraria, idDisciplina))
            throw new RuntimeException("Disciplina já existe nessa Grade Horária.");

        GradeHoraria gradeHoraria = gradeHorariaRepository.getOne(idGradeHoraria);
        Disciplina disciplina = disciplinaRepository.getOne(idDisciplina);

        DisciplinaGradeHoraria disciplinaGradeHoraria = new DisciplinaGradeHoraria();
        disciplinaGradeHoraria.setDisciplina(disciplina);
        disciplinaGradeHoraria.setGradeHoraria(gradeHoraria);

        return disciplinaGradeHorariaRepository.save(disciplinaGradeHoraria);
    }

    public List<DisciplinaGradeHorariaDTO> buscarTodos() {
        return disciplinaGradeHorariaRepository.buscarTodos().stream().map(DisciplinaGradeHorariaDTO::new).collect(Collectors.toList());
    }

    public List<DisciplinaGradeHoraria> buscarPorGradeHorariaId(Long idGradeHoraria) {
        DisciplinaGradeHoraria disciplinaGradeHoraria = new DisciplinaGradeHoraria();

        GradeHoraria gradeHoraria = new GradeHoraria();
        gradeHoraria.setId(idGradeHoraria);
        disciplinaGradeHoraria.setGradeHoraria(gradeHoraria);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase();

        Example<DisciplinaGradeHoraria> exemplo = Example.of(disciplinaGradeHoraria, exampleMatcher);


        return disciplinaGradeHorariaRepository.findAll(exemplo);
    }

    public Optional<DisciplinaGradeHoraria> buscarPorId(Long id) {
        return disciplinaGradeHorariaRepository.findById(id);
    }

    public void deletar(Long id) {
        disciplinaGradeHorariaRepository.deleteById(id);
    }

    public Object alterar(Long id, DisciplinaGradeHoraria disciplinaGradeHoraria) {
        if (!id.equals(disciplinaGradeHoraria.getId()))
            throw new RuntimeException("Erro ao atualizar o registro.");

        return disciplinaGradeHorariaRepository.save(disciplinaGradeHoraria);
    }
}
