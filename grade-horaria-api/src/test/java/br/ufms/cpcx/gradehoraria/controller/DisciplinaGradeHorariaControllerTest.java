package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.GradeHorariaApplicationTest;
import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaDTO;
import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaEdicaoDTO;
import br.ufms.cpcx.gradehoraria.service.DisciplinaGradeHorariaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GradeHorariaApplicationTest.class)
class DisciplinaGradeHorariaControllerTest {

    @InjectMocks
    private DisciplinaGradeHorariaController disciplinaGradeHorariaController;

    @Mock
    private DisciplinaGradeHorariaService disciplinaGradeHorariaService;

    @Test
    void buscarTest() {
        when(disciplinaGradeHorariaService.buscarTodas()).thenReturn(new ArrayList<>());

        Assertions.assertNotNull(disciplinaGradeHorariaController.buscarTodas());

        verify(disciplinaGradeHorariaService, times(1)).buscarTodas();
    }

    @Test
    void buscarTodasTest() {
        Assertions.assertNotNull(disciplinaGradeHorariaController.buscarTodas());

        verify(disciplinaGradeHorariaService, times(1)).buscarTodas();
    }

    @Test
    void buscarPorIdTest() {
        when(disciplinaGradeHorariaService.buscarPorId(anyLong())).thenReturn(new DisciplinaGradeHorariaDTO());

        Assertions.assertNotNull(disciplinaGradeHorariaController.buscarPorId(1L));

        verify(disciplinaGradeHorariaService, times(1)).buscarPorId(anyLong());
    }

    @Test
    void deletarTest() {
        disciplinaGradeHorariaController.deletar(1L);
        verify(disciplinaGradeHorariaService, times(1)).deletar(anyLong());
    }

    @Test
    void alterarTest() {
        when(disciplinaGradeHorariaService.alterar(anyLong(), any(DisciplinaGradeHorariaEdicaoDTO.class))).thenReturn(new DisciplinaGradeHorariaEdicaoDTO());

        Assertions.assertNotNull(disciplinaGradeHorariaController.alterar(1L, new DisciplinaGradeHorariaEdicaoDTO()));

        verify(disciplinaGradeHorariaService, times(1)).alterar(anyLong(), any(DisciplinaGradeHorariaEdicaoDTO.class));
    }
}
