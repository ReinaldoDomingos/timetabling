package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.GradeHorariaApplicationTest;
import br.ufms.cpcx.gradehoraria.dto.ProfessorDTO;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.repository.ProfessorRepository;
import br.ufms.cpcx.gradehoraria.service.ProfessorService;
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
class ProfessorControllerTest {

    @InjectMocks
    private ProfessorController professorController;

    @Mock
    private ProfessorService professorService;

    @Mock
    private ProfessorRepository professorRepository;

    @Test
    void buscarTest() {
        when(professorService.buscarTodos(any(GenericFilter.class))).thenReturn(new PageImpl<>(new ArrayList<>()));

        Assertions.assertNotNull(professorController.buscar(new HashMap<>()));

        verify(professorService, times(1)).buscarTodos(any(GenericFilter.class));
    }

    @Test
    void buscarTodasTest() {
        when(professorService.buscarTodos()).thenReturn(new ArrayList<>());

        Assertions.assertNotNull(professorController.buscarTodos());

        verify(professorService, times(1)).buscarTodos();
    }

    @Test
    void buscarPorIdTest() {
        when(professorService.buscarPorId(anyLong())).thenReturn(new ProfessorDTO());

        Assertions.assertNotNull(professorController.buscarPorId(1L));

        verify(professorService, times(1)).buscarPorId(anyLong());
    }

    @Test
    void salvarTest() {
        when(professorService.salvar(any(ProfessorDTO.class))).thenReturn(new ProfessorDTO());

        Assertions.assertNotNull(professorController.salvar(new ProfessorDTO()));

        verify(professorService, times(1)).salvar(any(ProfessorDTO.class));
    }

    @Test
    void salvarDisciplinaExistenteExceptionTest() {
        when(professorRepository.existsProfessorByNome(anyString())).thenThrow(GenericException.class);
        when(professorService.salvar(any(ProfessorDTO.class))).thenThrow(GenericException.class);

        try {
            Assertions.assertNotNull(professorController.salvar(new ProfessorDTO()));
        } catch (Exception exception) {
            Assertions.assertEquals(exception.getClass(), GenericException.class);
        }
    }

    @Test
    void deletarTest() {
        professorController.deletar(1L);

        verify(professorService, times(1)).deletar(anyLong());
    }

    @Test
    void alterarTest() {
        when(professorService.alterar(anyLong(), any(ProfessorDTO.class))).thenReturn(new ProfessorDTO());

        Assertions.assertNotNull(professorController.alterar(1L, new ProfessorDTO()));

        verify(professorService, times(1)).alterar(anyLong(), any(ProfessorDTO.class));
    }
}
