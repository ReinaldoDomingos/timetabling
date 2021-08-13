package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaDTO;
import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaEdicaoDTO;
import br.ufms.cpcx.gradehoraria.service.DisciplinaGradeHorariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gradehoraria-api/disciplinaGradeHoraria")
public class DisciplinaGradeHorariaController {

    @Autowired
    private DisciplinaGradeHorariaService disciplinaGradeHorariaService;

    @GetMapping
    public List<DisciplinaGradeHorariaDTO> buscarTodas() {
        return disciplinaGradeHorariaService.buscarTodas();
    }

    @GetMapping("/{id}")
    public DisciplinaGradeHorariaDTO buscarPorId(@PathVariable("id") Long id) {
        return disciplinaGradeHorariaService.buscarPorId(id);
    }

    @DeleteMapping("{id}")
    public void deletar(@PathVariable("id") Long id) {
        disciplinaGradeHorariaService.deletar(id);
    }

    @PutMapping("{id}")
    public DisciplinaGradeHorariaEdicaoDTO alterar(@PathVariable("id") Long id, @RequestBody DisciplinaGradeHorariaEdicaoDTO disciplinaGradeHoraria) {
        return disciplinaGradeHorariaService.alterar(id, disciplinaGradeHoraria);
    }
}
