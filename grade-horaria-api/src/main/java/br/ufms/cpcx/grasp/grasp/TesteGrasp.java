package br.ufms.cpcx.grasp.grasp;

import br.ufms.cpcx.grasp.conflitos.GeradorListaDeConflitos;
import br.ufms.cpcx.grasp.gradehoraria.EPeriodo;
import br.ufms.cpcx.grasp.gradehoraria.GradeHorariaPlanilha;
import br.ufms.cpcx.grasp.grasp.impl.AvaliadorSolucaoBO;
import br.ufms.cpcx.grasp.grasp.impl.GRASPImpl;
import br.ufms.cpcx.grasp.grasp.impl.MelhorSolucaoDTO;
import br.ufms.cpcx.grasp.restricoes.ERestricao;
import br.ufms.cpcx.grasp.utils.xls.ExportarXLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

public class TesteGrasp {
    public static void main(String[] args) throws Exception {
        String colunaCargaHoraria = "CHS";
        String colunaNomeEntidade = "Disciplina";

        int maximoIteracoes = 15;

        // Geração lista de conflitos
        GeradorListaDeConflitos geradorListaDeConflitos = new GeradorListaDeConflitos(colunaNomeEntidade);

        geradorListaDeConflitos.setColunaIdentificador("Número");
        geradorListaDeConflitos.setColunaSemestre("Semestre");
        geradorListaDeConflitos.setColunaCargaHoraria(colunaCargaHoraria);
        geradorListaDeConflitos.setColunaCargaHorariaPratica("CHPS");// Enfermagem
        geradorListaDeConflitos.setColunaCargaHorariaTeorica("CHTS");// Enfermagem
        geradorListaDeConflitos.setColunaQtdTurmaPratica("NTP");// Enfermagem

//        EPeriodo periodo = EPeriodo.UNICO;// Sistemas
//        geradorListaDeConflitos.lerRegistrosTabuladosNoXlS("alarr-api/resources/disciplinas-2021-2-v2.xlsx"); // Sistemas
//        geradorListaDeConflitos.lerRegistrosTabuladosNoXlS("alarr-api/resources/disciplinas-2021-2.xlsx"); // Sistemas
//        geradorListaDeConflitos.lerRegistrosTabuladosNoXlS("alarr-api/resources/disciplinas-2021-2.xlsx"); // Sistemas
//        List<String> disciplinasNoMesmoDia = asList("Algoritmos e Programação I T01", "Algoritmos e Programação I T02");
//        List<String> disciplinasNoMesmoDia = asList("Algoritmos e Programação I", "Algoritmos e Programação II", "Empreendedorismo");

        EPeriodo periodo = EPeriodo.INTEGRAL;
        List<String> restricoes = asList("Professor", "Semestre", "Local");

        List<String> colunasProfessor = asList("Professor Prática", "Professor Teórica");
        geradorListaDeConflitos.lerRegistrosTabuladosNoXlS("alarr-api/resources/disciplinas-enfermagem-2021-1.xlsx", colunasProfessor);
//        geradorListaDeConflitos.juntarColunas(restricoes.get(0), "Professor Prática", "Professor Teórica");
        geradorListaDeConflitos.juntarColunas(restricoes.get(2), "Local Teórica", "Local Prática");


        try {
            for (String restricao : restricoes) {
                geradorListaDeConflitos.adicionarRestricaoColuna(restricao);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // Execução GRASP
//        List<Integer> validas = new ArrayList<>();
//        List<Integer> validasAjustadas = new ArrayList<>();
//        List<Integer> invalidas = new ArrayList<>();
        List<Integer> invalidasDias = new ArrayList<>();
        List<Integer> invalidasTurmas = new ArrayList<>();

        Map<Integer, Integer> quantidadeCores = new TreeMap<>();
        int gerados = 0;
//        int i = 0;
        for (int i = 0; i < 100; i++) {
            GRASPImpl grasp = new GRASPImpl(periodo, 5, restricoes.get(1), colunaCargaHoraria);

            grasp.setColunasRestricoesFortes(restricoes);
            grasp.setColunaLocal("Laboratório");

//            grasp.ativarRestricao(ERestricao.DIAS_NAO_CONSECUTIVOS);
            grasp.ativarRestricao(ERestricao.LOCAL_DISPONIVEL);
//            grasp.ativarRestricao(ERestricao.MESMO_DIA, disciplinasNoMesmoDia);

            grasp.setCandidatos(geradorListaDeConflitos.getEntidades());

            // Exportar xls
            MelhorSolucaoDTO<Grafo<Integer, Integer>> melhorSolucaoDTO = grasp.execute(maximoIteracoes);
            Grafo<Integer, Integer> grafo = melhorSolucaoDTO.getSolucao();

            int cores = grasp.melhorSolucao.getSolucao().getCores().size();
            Integer quantidadeDeCor = quantidadeCores.get(cores);
            if (isNull(quantidadeDeCor)) {
                quantidadeDeCor = 0;
            }
            quantidadeCores.put(cores, ++quantidadeDeCor);

            if (AvaliadorSolucaoBO.getQtdAvaliacoesNegativas(grasp.melhorSolucao.getAvaliacoesDias()) > 0) {
                invalidasDias.add(i + 1);
            }

            if (AvaliadorSolucaoBO.getQtdAvaliacoesNegativas(grasp.melhorSolucao.getAvaliacoesTurmas()) > 0) {
                invalidasTurmas.add(i + 1);
            }


//            if (grasp.ehValida(grafo)) {
//                validas.add(i + 1);
//                System.out.println("Solucão " + (i + 1) + " é valida");
//            } else if (grasp.ehValida(grasp.repararSolucao(grafo))) {
//                validasAjustadas.add(i + 1);
//                System.out.println("Solucão " + (i + 1) + " ficou valida");
//            } else {
//                invalidas.add(i + 1);
//                System.out.println("Solucão " + (i + 1) + " não é valida");
//            }
            System.out.println();

            System.out.println("---------- " + (i + 1) + " finalizado -------------");
            if (gerados >= 10) continue; // Não gerar muito xls
            gerados++;
            GradeHorariaPlanilha grade = new GradeHorariaPlanilha(grafo, grasp.solucaoGRASP.getEntidades(), restricoes.get(1), periodo);

            ExportarXLS exportarXLS = new ExportarXLS("Grade Horária disciplinas");

            Map<String, List<String>> gradeHoraria = grade.gerarGradeHoraria(asList(colunaNomeEntidade, restricoes.get(0)));
            exportarXLS.setDados(grade.getCabecalho(), grade.getHorarios(), gradeHoraria, periodo);

            int pagina = exportarXLS.adicionarPagina("Grade Horária professores");
            exportarXLS.setDados(pagina, grade.getCabecalho(), grade.getHorarios(), grade.gerarGradeHoraria(restricoes), periodo);

            exportarXLS.salvarXLS("alarr-api/resources/teste-" + (i + 1) + ".xlsx");
        }
//        System.out.println(validas.size() + " soluções validas");
//        System.out.println(validas);
//        System.out.println();
//        System.out.println(validasAjustadas.size() + " soluções validas ajustadas");
//        System.out.println(validasAjustadas);
//        System.out.println();
//        System.out.println(invalidas.size() + " soluções invalidas");
//        System.out.println(invalidas);

        System.out.println("Dias invalidos: " + invalidasDias.size());
        System.out.println(invalidasDias);
        System.out.println("Semestres invalidos: " + invalidasTurmas.size());
        System.out.println(invalidasTurmas);

        invalidasTurmas.addAll(invalidasDias);
        System.out.println("Soluções invalidas: " + invalidasTurmas.stream().distinct().count());
        System.out.println();
        System.out.println(quantidadeCores);
    }
}
