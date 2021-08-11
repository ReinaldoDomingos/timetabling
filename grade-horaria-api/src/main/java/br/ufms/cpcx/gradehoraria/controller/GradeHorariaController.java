package br.ufms.cpcx.gradehoraria.controller;

import br.ufms.cpcx.gradehoraria.dto.DisciplinaDTO;
import br.ufms.cpcx.gradehoraria.dto.DisciplinaGradeHorariaEdicaoDTO;
import br.ufms.cpcx.gradehoraria.dto.GradeHorariaDTO;
import br.ufms.cpcx.gradehoraria.dto.GradeHorariaEdicaoDTO;
import br.ufms.cpcx.gradehoraria.exception.GenericException;
import br.ufms.cpcx.gradehoraria.filter.GenericFilter;
import br.ufms.cpcx.gradehoraria.service.DisciplinaGradeHorariaService;
import br.ufms.cpcx.gradehoraria.service.GradeHorariaService;
import br.ufms.cpcx.grasp.conflitos.GeradorListaDeConflitos;
import br.ufms.cpcx.grasp.gradehoraria.EPeriodo;
import br.ufms.cpcx.grasp.gradehoraria.GradeHorariaPlanilha;
import br.ufms.cpcx.grasp.grasp.Grafo;
import br.ufms.cpcx.grasp.grasp.impl.GRASPImpl;
import br.ufms.cpcx.grasp.grasp.impl.MelhorSolucaoDTO;
import br.ufms.cpcx.grasp.restricoes.ERestricao;
import br.ufms.cpcx.grasp.utils.StringUtils;
import br.ufms.cpcx.grasp.utils.xls.ExportarXLS;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Controller
@RestController
@RequestMapping("/gradehoraria-api/gradeHoraria")
public class GradeHorariaController {

    @Autowired
    private GradeHorariaService gradeHorariaService;

    @Autowired
    private DisciplinaGradeHorariaService disciplinaGradeHorariaService;

    @GetMapping
    public Page<GradeHorariaDTO> buscarTodas(@RequestParam Map<String, String> filters) {
        return gradeHorariaService.buscarTodos(GenericFilter.of(filters));
    }

    @GetMapping("/{id}")
    public GradeHorariaDTO buscarPorId(@PathVariable("id") Long id) {
        return gradeHorariaService.buscarPorId(id);
    }

    @GetMapping("/gradeHorariaCompleta/{id}")
    public Map<String, String> gerarXLS(@PathVariable("id") Long id) throws Exception {

        String colunaDisciplina = "Disciplina";
        String colunaProfessor = "Professor";
        String colunaSemestre = "Semestre";

        List<String> colunasDisciplinasGradeHoraria = asList("Numero", colunaDisciplina, colunaProfessor, colunaSemestre, "CHS", "Laboratório");
        GradeHorariaEdicaoDTO gradeHorariaEdicaoDTO = buscarGradeHorariaDTO(id);

        AtomicInteger contador = new AtomicInteger();
        List<String> disciplinasTabuladas = gradeHorariaEdicaoDTO.getDisciplinas().stream()
                .map(disciplinaDTO -> getDisciplinaString(contador, disciplinaDTO))
                .collect(Collectors.toList());

        disciplinasTabuladas.add(0, StringUtils.getTextoDaLista(colunasDisciplinasGradeHoraria).replaceAll(",", "\t"));

        GeradorListaDeConflitos geradorListaDeConflitos = new GeradorListaDeConflitos(colunaDisciplina);
        geradorListaDeConflitos.setColunaIdentificador("Numero");
        geradorListaDeConflitos.setColunaSemestre(colunaSemestre);
        geradorListaDeConflitos.setColunaCargaHoraria("CHS");

        geradorListaDeConflitos.lerRegistrosTabuladosNoDTO(Collections.singletonList(colunaProfessor), disciplinasTabuladas);

        try {
            geradorListaDeConflitos.adicionarRestricaoColuna(colunaSemestre);
            geradorListaDeConflitos.adicionarRestricaoColuna(colunaProfessor);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        geradorListaDeConflitos.getEntidades().forEach(System.out::println);

        geradorListaDeConflitos.imprimirListaDeConflitos();


        /* GRASP */
        int maximoIteracoes = 15;

        EPeriodo periodo = EPeriodo.UNICO;
        String colunaCargaHoraria = "CHS";
        String colunaNomeEntidade = colunaDisciplina;
        List<String> restricoes = asList(colunaProfessor, colunaSemestre);

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

        return exportarXLS.exportarBase64();
    }

    private GradeHorariaEdicaoDTO buscarGradeHorariaDTO(Long idGradeHoraria) {
        GradeHorariaDTO gradeHoraria = gradeHorariaService.buscarPorId(idGradeHoraria);

        GradeHorariaEdicaoDTO gradeHorariaEdicaoDTO = new GradeHorariaEdicaoDTO(gradeHoraria.getGradeHoraria());
        gradeHorariaEdicaoDTO.setDisciplinas(getDisciplinasGradeHoraria(gradeHoraria));

        return gradeHorariaEdicaoDTO;
    }

    private List<DisciplinaGradeHorariaEdicaoDTO> getDisciplinasGradeHoraria(GradeHorariaDTO gradeHoraria) {
        return disciplinaGradeHorariaService.buscarTodasPorGradeHorariaId(gradeHoraria.getId());
    }

    @GetMapping("/{id}/disciplinas")
    public Page<DisciplinaGradeHorariaEdicaoDTO> buscarDisciplinas(@PathVariable("id") Long id, @RequestParam Map<String, String> filters) {
        return disciplinaGradeHorariaService.buscarPorGradeHorariaId(GenericFilter.of(filters), id);
    }

    @GetMapping("/{id}/disciplinas/todas")
    public List<DisciplinaGradeHorariaEdicaoDTO> buscarTodasDisciplinas(@PathVariable("id") Long id) {
        return disciplinaGradeHorariaService.buscarTodasPorGradeHorariaId(id);
    }

    @PostMapping
    public GradeHorariaDTO salvar(@RequestBody GradeHorariaDTO gradeHorariaDTO) {
        return gradeHorariaService.salvar(gradeHorariaDTO);
    }

    @PutMapping("{id}")
    public GradeHorariaDTO alterar(@PathVariable("id") Long id, @RequestBody GradeHorariaDTO gradeHorariaDTO) {
        return gradeHorariaService.alterar(id, gradeHorariaDTO);
    }

    @PostMapping("/{id}/adicionarDisciplina")
    public DisciplinaGradeHorariaEdicaoDTO adicionarDisciplina(@PathVariable("id") Long id, @RequestBody DisciplinaDTO disciplinaDTO) {
        return disciplinaGradeHorariaService.salvar(id, disciplinaDTO.getId());
    }

    @DeleteMapping("{id}")
    public void deletar(@PathVariable("id") Long id) {
        gradeHorariaService.deletar(id);
    }

    private String getDisciplinaString(AtomicInteger numero, DisciplinaGradeHorariaEdicaoDTO disciplinaGradeHorariaEdicaoDTO) {
        if (isNull(disciplinaGradeHorariaEdicaoDTO.getProfessor())) {
            throw new GenericException("Possui disciplina(s) em que não selecionou um professor.");
        }
        if (isNull(disciplinaGradeHorariaEdicaoDTO.getTurma())) {
            throw new GenericException("Possui disciplina(s) em que não selecionou uma turma.");
        }

        boolean usaLaboratorio = nonNull(disciplinaGradeHorariaEdicaoDTO.getUsaLaboratorio()) && disciplinaGradeHorariaEdicaoDTO.getUsaLaboratorio();

        return numero.incrementAndGet() + "\t" + disciplinaGradeHorariaEdicaoDTO.getNome() + "\t" + disciplinaGradeHorariaEdicaoDTO.getProfessor().getNome() + "\t" + disciplinaGradeHorariaEdicaoDTO.getTurma().getSemestre() + "\t" + disciplinaGradeHorariaEdicaoDTO.getCargaHorariaSemanal() + "\t" + (usaLaboratorio ? "Sim" : "Não");
    }
}
