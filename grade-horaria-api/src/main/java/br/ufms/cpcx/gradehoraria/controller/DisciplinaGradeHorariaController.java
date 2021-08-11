package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaEdicaoDTO;
import br.ufms.cpcx.gradehoraria.service.DisciplinaGradeHorariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gradehoraria-api/disciplinaGradeHoraria")
public class DisciplinaGradeHorariaController {

    @Autowired
    DisciplinaGradeHorariaService disciplinaGradeHorariaService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> buscar() {
        return new ResponseEntity<>(disciplinaGradeHorariaService.buscarTodos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(disciplinaGradeHorariaService.buscarPorId(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        disciplinaGradeHorariaService.deletar(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    @ResponseBody
    public Object alterar(@PathVariable("id") Long id, @RequestBody DisciplinaGradeHorariaEdicaoDTO disciplinaGradeHoraria) {
        return new ResponseEntity<>(disciplinaGradeHorariaService.alterar(id, disciplinaGradeHoraria), HttpStatus.ACCEPTED);
    }
}
