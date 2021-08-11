package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.dto.TurmaDTO;
import br.ufms.cpcx.gradehoraria.entity.Turma;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.service.TurmaService;
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
@RequestMapping("/gradehoraria-api/turma")
public class TurmaController {

    @Autowired
    TurmaService turmaService;

    @GetMapping
    @ResponseBody
    public Page<TurmaDTO> buscar(@RequestParam Map<String, String> filters) {
        return turmaService.buscarTodos(GenericFilter.of(filters));
    }

    @GetMapping("/todas")
    @ResponseBody
    public List<Turma> buscarTodas() {
        return turmaService.buscarTodos();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public TurmaDTO buscarPorId(@PathVariable("id") Long id) {
        return turmaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseBody
    public TurmaDTO salvar(@RequestBody TurmaDTO turmaDTO) {
        return turmaService.salvar(turmaDTO);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public void deletar(@PathVariable("id") Long id) {
        turmaService.deletar(id);
    }

    @PutMapping("{id}")
    @ResponseBody
    public TurmaDTO alterar(@PathVariable("id") Long id, @RequestBody TurmaDTO turmaDTO) {
        return turmaService.alterar(id, turmaDTO);
    }
}
