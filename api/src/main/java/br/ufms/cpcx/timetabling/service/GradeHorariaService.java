package br.ufms.cpcx.timetabling.service;

import br.ufms.cpcx.timetabling.GenericFilter;
import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import br.ufms.cpcx.timetabling.repository.DisciplinaGradeHorariaRepository;
import br.ufms.cpcx.timetabling.repository.GradeHorariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GradeHorariaService {

    @Autowired
    private GradeHorariaRepository gradeHorariaRepository;

    @Autowired
    private DisciplinaGradeHorariaRepository disciplinaGradeHorariaRepository;

    public Page<GradeHoraria> buscarTodos(GenericFilter filter) {
        return gradeHorariaRepository.findAll(filter.getPageRequest());
    }

    public Optional<GradeHoraria> buscarPorId(Long id) {
        return gradeHorariaRepository.findById(id);
    }

    public GradeHoraria salvar(GradeHoraria gradeHoraria) {
        if (gradeHorariaRepository.existsGradeHorariaByAnoAndSemestreAno(gradeHoraria.getAno(), gradeHoraria.getSemestreAno()))
            throw new RuntimeException("Grade Horária já existe.");

        return gradeHorariaRepository.save(gradeHoraria);
    }

    public Object alterar(Long id, GradeHoraria gradeHoraria) {
        if (!id.equals(gradeHoraria.getId()))
            throw new RuntimeException("Erro ao atualizar o registro.");

        return gradeHorariaRepository.save(gradeHoraria);
    }

    @Transactional("transactionManager")
    public void deletar(Long id) {
        disciplinaGradeHorariaRepository.deleteByGradeHorariaId(id);
        gradeHorariaRepository.deleteById(id);
    }
}
