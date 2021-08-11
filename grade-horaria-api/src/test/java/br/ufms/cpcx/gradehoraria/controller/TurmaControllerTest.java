package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.GradeHorariaApplicationTest;
import br.ufms.cpcx.gradehoraria.dto.TurmaDTO;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.repository.TurmaRepository;
import br.ufms.cpcx.gradehoraria.service.TurmaService;
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
class TurmaControllerTest {

    @InjectMocks
    private TurmaController turmaController;

    @Mock
    private TurmaService turmaService;

    @Mock
    private TurmaRepository turmaRepository;

    @Test
    void buscarTest() {
        when(turmaService.buscarTodos(any(GenericFilter.class))).thenReturn(new PageImpl<>(new ArrayList<>()));

        Assertions.assertNotNull(turmaController.buscar(new HashMap<>()));

        verify(turmaService, times(1)).buscarTodos(any(GenericFilter.class));
    }

    @Test
    void buscarTodasTest() {
        when(turmaService.buscarTodos()).thenReturn(new ArrayList<>());

        Assertions.assertNotNull(turmaController.buscarTodas());

        verify(turmaService, times(1)).buscarTodos();
    }

    @Test
    void buscarPorIdTest() {
        when(turmaService.buscarPorId(anyLong())).thenReturn(new TurmaDTO());

        Assertions.assertNotNull(turmaController.buscarPorId(1L));

        verify(turmaService, times(1)).buscarPorId(anyLong());
    }

    @Test
    void salvarTest() {
        when(turmaService.salvar(any(TurmaDTO.class))).thenReturn(new TurmaDTO());

        Assertions.assertNotNull(turmaController.salvar(new TurmaDTO()));

        verify(turmaService, times(1)).salvar(any(TurmaDTO.class));
    }

    @Test
    void salvarDisciplinaExistenteExceptionTest() {
        when(turmaRepository.existsTurmaByCodigo(anyString())).thenThrow(GenericException.class);
        when(turmaService.salvar(any(TurmaDTO.class))).thenThrow(GenericException.class);

        try {
            Assertions.assertNotNull(turmaController.salvar(new TurmaDTO()));
        } catch (Exception exception) {
            Assertions.assertEquals(exception.getClass(), GenericException.class);
        }
    }

    @Test
    void deletarTest() {
        turmaController.deletar(1L);

        verify(turmaService, times(1)).deletar(anyLong());
    }

    @Test
    void alterarTest() {
        when(turmaService.alterar(anyLong(), any(TurmaDTO.class))).thenReturn(new TurmaDTO());

        Assertions.assertNotNull(turmaController.alterar(1L, new TurmaDTO()));

        verify(turmaService, times(1)).alterar(anyLong(), any(TurmaDTO.class));
    }
}
