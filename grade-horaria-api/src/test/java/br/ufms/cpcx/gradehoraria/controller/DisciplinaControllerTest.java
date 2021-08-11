package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.GradeHorariaApplicationTest;
import br.ufms.cpcx.gradehoraria.dto.DisciplinaDTO;
import br.ufms.cpcx.gradehoraria.entity.Disciplina;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.repository.DisciplinaRepository;
import br.ufms.cpcx.gradehoraria.service.DisciplinaService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GradeHorariaApplicationTest.class)
class DisciplinaControllerTest {

    @InjectMocks
    private DisciplinaController disciplinaController;

    @Mock
    private DisciplinaService disciplinaService;

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @Test
    void buscarTest() {
        when(disciplinaService.buscarTodas(any(GenericFilter.class))).thenReturn(new PageImpl<>(new ArrayList<>()));

        Assertions.assertNotNull(disciplinaController.buscarTodas(new HashMap<>()));

        verify(disciplinaService, times(1)).buscarTodas(any(GenericFilter.class));
    }

    @Test
    void buscarTodasTest() {
        Assertions.assertNotNull(disciplinaController.buscarTodas());

        verify(disciplinaService, times(1)).buscarTodas();
    }

    @Test
    void buscarPorIdTest() {
        when(disciplinaService.buscarPorId(anyLong())).thenReturn(new DisciplinaDTO());

        Assertions.assertNotNull(disciplinaController.buscarPorId(1L));

        verify(disciplinaService, times(1)).buscarPorId(anyLong());
    }

    @Test
    void salvarTest() {
        when(disciplinaService.salvar(any(DisciplinaDTO.class))).thenReturn(new DisciplinaDTO());

        Assertions.assertNotNull(disciplinaController.salvar(new DisciplinaDTO()));

        verify(disciplinaService, times(1)).salvar(any(DisciplinaDTO.class));
    }

    @Test
    void salvarDisciplinaExistenteExceptionTest() {
        when(disciplinaRepository.existsDisciplinaByCodigo(anyString())).thenThrow(GenericException.class);
        when(disciplinaService.salvar(any(DisciplinaDTO.class))).thenThrow(GenericException.class);

        try {
            Assertions.assertNotNull(disciplinaController.salvar(new DisciplinaDTO()));
        } catch (Exception exception) {
            Assertions.assertEquals(exception.getClass(), GenericException.class);
        }
    }

    @Test
    void deletarTest() {
        disciplinaController.deletar(1L);
        verify(disciplinaService, times(1)).deletar(anyLong());
    }

    @Test
    void alterarTest() {
        when(disciplinaService.alterar(anyLong(), any(Disciplina.class))).thenReturn(new DisciplinaDTO());

        Assertions.assertNotNull(disciplinaController.alterar(1L, new Disciplina()));

        verify(disciplinaService, times(1)).alterar(anyLong(), any(Disciplina.class));
    }
}
