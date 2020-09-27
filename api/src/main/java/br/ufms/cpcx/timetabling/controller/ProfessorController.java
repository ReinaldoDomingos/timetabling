package br.ufms.cpcx.timetabling.controller;

import br.ufms.cpcx.timetabling.entity.Professor;
import br.ufms.cpcx.timetabling.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/professor")
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> buscar(@RequestParam(name = "codigo", required = false) Long codigo,
                                    @RequestParam(name = "nome", required = false) String nome) {

        return new ResponseEntity<>(professorService.buscarTodos(codigo, nome), HttpStatus.OK);
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

        return new ResponseEntity<>(professorService.alterar(id,professor), HttpStatus.ACCEPTED);
    }
}
