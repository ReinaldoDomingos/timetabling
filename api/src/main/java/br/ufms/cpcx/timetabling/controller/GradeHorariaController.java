package br.ufms.cpcx.timetabling.controller;

import br.ufms.cpcx.timetabling.entity.Disciplina;
import br.ufms.cpcx.timetabling.entity.GradeHoraria;
import br.ufms.cpcx.timetabling.service.DisciplinaGradeHorariaService;
import br.ufms.cpcx.timetabling.service.GradeHorariaService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/api/gradeHoraria")
public class GradeHorariaController {

    @Autowired
    GradeHorariaService gradeHorariaService;

    @Autowired
    DisciplinaGradeHorariaService disciplinaGradeHorariaService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> buscar() {
        return new ResponseEntity<>(gradeHorariaService.buscarTodos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {

        return new ResponseEntity<>(gradeHorariaService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/disciplinas")
    @ResponseBody
    public ResponseEntity<?> buscarDisciplinas(@PathVariable("id") Long id) {

        return new ResponseEntity<>(disciplinaGradeHorariaService.buscarPorGradeHorariaId(id), HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> salvar(@RequestBody GradeHoraria gradeHoraria) {
        try {
            return new ResponseEntity<>(gradeHorariaService.salvar(gradeHoraria), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<?> alterar(@PathVariable("id") Long id, @RequestBody GradeHoraria gradeHoraria) {

        return new ResponseEntity<>(gradeHorariaService.alterar(id, gradeHoraria), HttpStatus.ACCEPTED);
    }

    @PostMapping("/{id}/adicionarDisciplina")
    @ResponseBody
    public ResponseEntity<?> adicionarDisciplina(@PathVariable("id") Long id, @RequestBody Disciplina disciplina) {

        return new ResponseEntity<>(disciplinaGradeHorariaService.salvar(id, disciplina.getId()), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        gradeHorariaService.deletar(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
