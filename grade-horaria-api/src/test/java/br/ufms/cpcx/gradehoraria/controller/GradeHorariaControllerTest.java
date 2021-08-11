package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.GradeHorariaApplicationTest;
import br.ufms.cpcx.gradehoraria.dto.DisciplinaDTO;
import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaEdicaoDTO;
import br.ufms.cpcx.gradehoraria.dto.GradeHorariaDTO;
import br.ufms.cpcx.gradehoraria.dto.GradeHorariaEdicaoDTO;
import br.ufms.cpcx.gradehoraria.enumaration.ESemestre;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.repository.GradeHorariaRepository;
import br.ufms.cpcx.gradehoraria.service.DisciplinaGradeHorariaService;
import br.ufms.cpcx.gradehoraria.service.GradeHorariaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GradeHorariaApplicationTest.class)
class GradeHorariaControllerTest {

    @InjectMocks
    private GradeHorariaController gradeHorariaController;

    @Mock
    private GradeHorariaService gradeHorariaService;

    @Mock
    private DisciplinaGradeHorariaService disciplinaGradeHorariaService;

    @Mock
    private GradeHorariaRepository gradeHorariaRepository;

    @Test
    void buscarTest() {
        when(gradeHorariaService.buscarTodos(any(GenericFilter.class))).thenReturn(new PageImpl<>(new ArrayList<>()));

        Assertions.assertNotNull(gradeHorariaController.buscarTodas(new HashMap<>()));

        verify(gradeHorariaService, times(1)).buscarTodos(any(GenericFilter.class));
    }

    @Test
    void buscarPorIdTest() {
        when(gradeHorariaService.buscarPorId(anyLong())).thenReturn(new GradeHorariaDTO());

        Assertions.assertNotNull(gradeHorariaController.buscarPorId(1L));

        verify(gradeHorariaService, times(1)).buscarPorId(anyLong());
    }

    @Test
    void salvarTest() {
        when(gradeHorariaService.salvar(any(GradeHorariaDTO.class))).thenReturn(new GradeHorariaDTO());

        Assertions.assertNotNull(gradeHorariaController.salvar(new GradeHorariaDTO()));

        verify(gradeHorariaService, times(1)).salvar(any(GradeHorariaDTO.class));
    }

    @Test
    void salvarDisciplinaExistenteExceptionTest() {
        when(gradeHorariaRepository.existsGradeHorariaByAnoAndSemestreAno(any(Integer.class), any(ESemestre.class))).thenThrow(GenericException.class);
        when(gradeHorariaService.salvar(any(GradeHorariaDTO.class))).thenThrow(GenericException.class);

        try {
            Assertions.assertNotNull(gradeHorariaController.salvar(new GradeHorariaDTO()));
        } catch (Exception exception) {
            Assertions.assertEquals(exception.getClass(), GenericException.class);
        }
    }

    @Test
    void deletarTest() {
        gradeHorariaController.deletar(1L);

        verify(gradeHorariaService, times(1)).deletar(anyLong());
    }

    @Test
    void alterarTest() {
        when(gradeHorariaService.alterar(anyLong(), any(GradeHorariaDTO.class))).thenReturn(new GradeHorariaDTO());

        Assertions.assertNotNull(gradeHorariaController.alterar(1L, new GradeHorariaDTO()));

        verify(gradeHorariaService, times(1)).alterar(anyLong(), any(GradeHorariaDTO.class));
    }

    @Test
    void adicionarDisciplinaTest() {
        when(disciplinaGradeHorariaService.salvar(anyLong(), anyLong())).thenReturn(new DisciplinaGradeHorariaEdicaoDTO());

        DisciplinaDTO disciplinaDTO = new DisciplinaDTO();
        disciplinaDTO.setId(1L);

        Assertions.assertNotNull(gradeHorariaController.adicionarDisciplina(1L, disciplinaDTO));

        verify(disciplinaGradeHorariaService, times(1)).salvar(anyLong(), anyLong());
    }

    @Test
    void buscarDisciplinasTest() {
        when(disciplinaGradeHorariaService.buscarPorGradeHorariaId(any(GenericFilter.class), anyLong())).thenReturn(new PageImpl<>(new ArrayList<>()));

        Assertions.assertNotNull(gradeHorariaController.buscarDisciplinas(1L, new HashMap<>()));

        verify(disciplinaGradeHorariaService, times(1)).buscarPorGradeHorariaId(any(GenericFilter.class), anyLong());
    }
}
