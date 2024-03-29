package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.dto.DisciplinaDTO;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.service.DisciplinaService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/gradehoraria-api/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @GetMapping
    public Page<DisciplinaDTO> buscarTodas(@RequestParam Map<String, String> filters) {
        return disciplinaService.buscarTodas(GenericFilter.of(filters));
    }

    @GetMapping("/todas")
    public List<DisciplinaDTO> buscarTodas() {
        return disciplinaService.buscarTodas();
    }

    @GetMapping("/{id}")
    public DisciplinaDTO buscarPorId(@PathVariable("id") Long id) {
        return disciplinaService.buscarPorId(id);
    }

    @PostMapping
    public DisciplinaDTO salvar(@RequestBody DisciplinaDTO disciplinaDTO) {
        return disciplinaService.salvar(disciplinaDTO);
    }

    @DeleteMapping("{id}")
    public void deletar(@PathVariable("id") Long id) {
        disciplinaService.deletar(id);
    }

    @PutMapping("{id}")
    public DisciplinaDTO alterar(@PathVariable("id") Long id, @RequestBody DisciplinaDTO disciplinaDTO) {
        return disciplinaService.alterar(id, disciplinaDTO);
    }
}
