package br.ufms.cpcx.timetabling.controller;

import br.ufms.cpcx.timetabling.entity.Professor;
import br.ufms.cpcx.timetabling.filter.GenericFilter;
import br.ufms.cpcx.timetabling.service.ProfessorService;
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
@RequestMapping("/api/professor")
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> buscar(@RequestParam Map<String, String> filters) {
        return new ResponseEntity<>(professorService.buscarTodos(GenericFilter.of(filters)), HttpStatus.OK);
    }

    @GetMapping("/todos")
    @ResponseBody
    public ResponseEntity<?> buscarTodos() {
        return new ResponseEntity<>(professorService.buscarTodos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(professorService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> salvar(@RequestBody Professor professor) {
        return new ResponseEntity<>(professorService.salvar(professor), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        professorService.deletar(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<?> alterar(@PathVariable("id") Long id, @RequestBody Professor professor) {
        return new ResponseEntity<>(professorService.alterar(id, professor), HttpStatus.ACCEPTED);
    }
}
