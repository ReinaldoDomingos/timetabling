package br.ufms.cpcx.gradehoraria.service;

import br.ufms.cpcx.gradehoraria.dto.GradeHorariaDTO;
import br.ufms.cpcx.gradehoraria.entity.GradeHoraria;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.repository.DisciplinaGradeHorariaRepository;
import br.ufms.cpcx.gradehoraria.repository.GradeHorariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GradeHorariaService {

    @Autowired
    private GradeHorariaRepository gradeHorariaRepository;

    @Autowired
    private DisciplinaGradeHorariaRepository disciplinaGradeHorariaRepository;

    public Page<GradeHorariaDTO> buscarTodos(GenericFilter filter) {
        return gradeHorariaRepository.findAll(filter.getPageRequest()).map(GradeHorariaDTO::new);
    }

    public GradeHorariaDTO buscarPorId(Long id) {
        GradeHoraria gradeHorariaSalva = gradeHorariaRepository.buscarPorId(id);

        return new GradeHorariaDTO(gradeHorariaSalva);
    }

    public GradeHorariaDTO salvar(GradeHorariaDTO gradeHorariaDTO) {
        if (existeGradeHorariaIgual(GradeHorariaDTO.toMapGradeHoraria(gradeHorariaDTO)))
            throw new GenericException("Grade Horária já existe.");

        return salvarGradeHoraria(gradeHorariaDTO);
    }

    public GradeHorariaDTO alterar(Long id, GradeHorariaDTO gradeHorariaDTO) {
        GradeHoraria gradeHoraria = GradeHorariaDTO.toMapGradeHoraria(gradeHorariaDTO);

        if (!id.equals(gradeHoraria.getId())) {
            throw new GenericException("Erro ao atualizar o registro.");
        }

        return salvarGradeHoraria(gradeHorariaDTO);
    }

    private GradeHorariaDTO salvarGradeHoraria(GradeHorariaDTO gradeHoraria) {
        GradeHoraria gradeHorariaSalva = gradeHorariaRepository.save(GradeHorariaDTO.toMapGradeHoraria(gradeHoraria));

        return new GradeHorariaDTO(gradeHorariaSalva);
    }

    @Transactional("transactionManager")
    public void deletar(Long id) {
        disciplinaGradeHorariaRepository.deleteByGradeHorariaId(id);
        gradeHorariaRepository.deleteById(id);
    }

    private boolean existeGradeHorariaIgual(GradeHoraria gradeHoraria) {
        return gradeHorariaRepository.existsGradeHorariaByAnoAndSemestreAno(gradeHoraria.getAno(), gradeHoraria.getSemestreAno());
    }
}
