package br.ufms.cpcx.timetabling.service;

import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import br.ufms.cpcx.timetabling.repository.GradeHorariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeHorariaService {

    @Autowired
    GradeHorariaRepository gradeHorariaRepository;

    public List<GradeHoraria> buscarTodos() {
        return gradeHorariaRepository.findAll();
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

    public void deletar(Long id) {
        gradeHorariaRepository.deleteById(id);
    }
}
