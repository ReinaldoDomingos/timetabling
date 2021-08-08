package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.grasp.conflitos.Entidade;
import br.ufms.cpcx.grasp.conflitos.GeradorListaDeConflitos;
import br.ufms.cpcx.grasp.gradehoraria.EPeriodo;
import br.ufms.cpcx.grasp.gradehoraria.GradeHorariaPlanilha;
import br.ufms.cpcx.grasp.grasp.Grafo;
import br.ufms.cpcx.grasp.grasp.impl.GRASPImpl;
import br.ufms.cpcx.grasp.grasp.impl.MelhorSolucaoDTO;
import br.ufms.cpcx.grasp.restricoes.ERestricao;
import br.ufms.cpcx.grasp.utils.StringUtils;
import br.ufms.cpcx.grasp.utils.xls.ExportarXLS;
import br.ufms.cpcx.gradehoraria.dto.DisciplinaDTO;
import br.ufms.cpcx.gradehoraria.dto.GradeHorariaDTO;
import br.ufms.cpcx.gradehoraria.entity.Disciplina;
import br.ufms.cpcx.gradehoraria.entity.GradeHoraria;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.service.DisciplinaGradeHorariaService;
import br.ufms.cpcx.gradehoraria.service.GradeHorariaService;
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
    public ResponseEntity<?> buscar(@RequestParam Map<String, String> filters) {
        return new ResponseEntity<>(gradeHorariaService.buscarTodos(GenericFilter.of(filters)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {

        return new ResponseEntity<>(gradeHorariaService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/gradeHorariaCompleta/{id}")
    @ResponseBody
//    public ResponseEntity<?> buscarGradeHorariaDTOPorId(@PathVariable("id") Long id) {
    public List<Entidade> buscarGradeHorariaDTOPorId(@PathVariable("id") Long id) throws Exception {

        String colunasDisciplinasGradeHoraria = StringUtils.getTextoDaLista(asList("Numero", "Disciplina", "Professor", "Semestre", "CHS", "Laboratório")).replaceAll(",", "\t");
        GradeHorariaDTO gradeHorariaDTO = buscarGradeHorariaDTO(id);

        AtomicInteger contador = new AtomicInteger();
        List<String> disciplinasTabuladas = gradeHorariaDTO.getDisciplinas().stream()
                .map(disciplinaDTO -> getDisciplinaString(contador, disciplinaDTO))
                .collect(Collectors.toList());

        disciplinasTabuladas.add(0, colunasDisciplinasGradeHoraria);

        GeradorListaDeConflitos geradorListaDeConflitos = new GeradorListaDeConflitos("Disciplina");
        geradorListaDeConflitos.setColunaIdentificador("Numero");
        geradorListaDeConflitos.setColunaSemestre("Semestre");
        geradorListaDeConflitos.setColunaCargaHoraria("CHS");

        geradorListaDeConflitos.lerRegistrosTabuladosNoDTO("teste", asList("Professor"), disciplinasTabuladas);

        try {
            geradorListaDeConflitos.adicionarRestricaoColuna("Semestre");
            geradorListaDeConflitos.adicionarRestricaoColuna("Professor");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        geradorListaDeConflitos.getEntidades().forEach(System.out::println);

        geradorListaDeConflitos.imprimirListaDeConflitos();


        /* GRASP */
        int maximoIteracoes = 15;

        EPeriodo periodo = EPeriodo.UNICO;
        String colunaCargaHoraria = "CHS";
        String colunaNomeEntidade = "Disciplina";
        List<String> restricoes = asList("Professor", "Semestre");

        GRASPImpl grasp = new GRASPImpl(periodo, 5, restricoes.get(1), colunaCargaHoraria);
        grasp.setColunasRestricoesFortes(restricoes);
        grasp.setCandidatos(geradorListaDeConflitos.getEntidades());
        grasp.setColunaLocal("Laboratório");

        grasp.ativarRestricao(ERestricao.DIAS_NAO_CONSECUTIVOS);

        MelhorSolucaoDTO<Grafo<Integer, Integer>> melhorSolucaoDTO = grasp.execute(maximoIteracoes);
        Grafo<Integer, Integer> grafo = melhorSolucaoDTO.getSolucao();


        GradeHorariaPlanilha grade = new GradeHorariaPlanilha(grafo, grasp.getSolucaoGRASP().getEntidades(), restricoes.get(1), periodo);
        ExportarXLS exportarXLS = new ExportarXLS("Grade Horária disciplinas");

        Map<String, List<String>> gradeHoraria = grade.gerarGradeHoraria(asList(colunaNomeEntidade, restricoes.get(0)));
        exportarXLS.setDados(grade.getCabecalho(), grade.getHorarios(), gradeHoraria, periodo);

        int pagina = exportarXLS.adicionarPagina("Grade Horária professores");
        exportarXLS.setDados(pagina, grade.getCabecalho(), grade.getHorarios(), grade.gerarGradeHoraria(restricoes), periodo);

        exportarXLS.salvarXLS("timetabling-api/src/main/resources/teste-0.xlsx");

        return geradorListaDeConflitos.getEntidades();
//        return disciplinasTabuladas;

//        return colunasDisciplinasGradeHoraria;
//        return new ResponseEntity<>(gradeHorariaDTO, HttpStatus.OK);
    }

    private String getDisciplinaString(AtomicInteger numero, DisciplinaDTO disciplinaDTO) {
        if (isNull(disciplinaDTO.getProfessor())) {
            throw new GenericException("Possui disciplina(s) em que não selecionou um professor.");
        }
        if (isNull(disciplinaDTO.getTurma())) {
            throw new GenericException("Possui disciplina(s) em que não selecionou uma turma.");
        }
        Boolean usaLaboratorio = nonNull(disciplinaDTO.getUsaLaboratorio()) && disciplinaDTO.getUsaLaboratorio();
        return numero.incrementAndGet() + "\t" + disciplinaDTO.getNome() + "\t" + disciplinaDTO.getProfessor().getNome() + "\t" + disciplinaDTO.getTurma().getSemestre() + "\t" + disciplinaDTO.getCargaHorariaSemanal() + "\t" + (usaLaboratorio ? "Sim" : "Não");
    }

    private GradeHorariaDTO buscarGradeHorariaDTO(Long idGradeHoraria) {
        GradeHoraria gradeHoraria = gradeHorariaService.buscarPorId(idGradeHoraria);

        return new GradeHorariaDTO(gradeHoraria, disciplinaGradeHorariaService.buscarTodasPorGradeHorariaId(gradeHoraria.getId()));
    }

    @GetMapping("/{id}/disciplinas")
    @ResponseBody
    public ResponseEntity<?> buscarDisciplinas(@PathVariable("id") Long id, @RequestParam Map<String, String> filters) {
        return new ResponseEntity<>(disciplinaGradeHorariaService.buscarPorGradeHorariaId(GenericFilter.of(filters), id), HttpStatus.OK);
    }

    @GetMapping("/{id}/disciplinas/todas")
    @ResponseBody
    public ResponseEntity<?> buscarTodasDisciplinas(@PathVariable("id") Long id) {
        return new ResponseEntity<>(disciplinaGradeHorariaService.buscarTodasPorGradeHorariaId(id), HttpStatus.OK);
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
