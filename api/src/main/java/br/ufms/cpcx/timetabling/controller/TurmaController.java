package br.ufms.cpcx.timetabling.controller;

import br.ufms.cpcx.timetabling.entity.Turma;
import br.ufms.cpcx.timetabling.filter.GenericFilter;
import br.ufms.cpcx.timetabling.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.util.Map;

@Controller
@RestController
@RequestMapping("/api/turma")
public class TurmaController {

    @Autowired
    TurmaService turmaService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> buscar(@RequestParam Map<String, String> filters) {
        return new ResponseEntity<>(turmaService.buscarTodos(GenericFilter.of(filters)), HttpStatus.OK);
    }

    @GetMapping("/todas")
    @ResponseBody
    public ResponseEntity<?> buscarTodas() {
        return new ResponseEntity<>(turmaService.buscarTodos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(turmaService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> salvar(@RequestBody Turma turma) {
        try {
            return new ResponseEntity<>(turmaService.salvar(turma), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        turmaService.deletar(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<?> alterar(@PathVariable("id") Long id, @RequestBody Turma turma) {
        return new ResponseEntity<>(turmaService.alterar(id, turma), HttpStatus.ACCEPTED);
    }
}
