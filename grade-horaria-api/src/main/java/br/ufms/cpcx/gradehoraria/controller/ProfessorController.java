package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.dto.ProfessorDTO;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/gradehoraria-api/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    @ResponseBody
    public Page<ProfessorDTO> buscar(@RequestParam Map<String, String> filters) {
        return professorService.buscarTodos(GenericFilter.of(filters));
    }

    @GetMapping("/todos")
    @ResponseBody
    public List<ProfessorDTO> buscarTodos() {
        return professorService.buscarTodos();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ProfessorDTO buscarPorId(@PathVariable("id") Long id) {
        return professorService.buscarPorId(id);
    }

    @PostMapping
    @ResponseBody
    public ProfessorDTO salvar(@RequestBody ProfessorDTO professorDTO) {
        return professorService.salvar(professorDTO);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public void deletar(@PathVariable("id") Long id) {
        professorService.deletar(id);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ProfessorDTO alterar(@PathVariable("id") Long id, @RequestBody ProfessorDTO professorDTO) {
        return professorService.alterar(id, professorDTO);
    }
}
