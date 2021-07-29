package br.ufms.cpcx.grasp.grasp.impl;

import br.ufms.cpcx.grasp.conflitos.Entidade;
import br.ufms.cpcx.grasp.gradehoraria.EPeriodo;
import br.ufms.cpcx.grasp.gradehoraria.GradeHoraria;
import br.ufms.cpcx.grasp.gradehoraria.HorarioAula;
import br.ufms.cpcx.grasp.grasp.Candidato;
import br.ufms.cpcx.grasp.grasp.GRASP;
import br.ufms.cpcx.grasp.grasp.Grafo;
import br.ufms.cpcx.grasp.grasp.Vertice;
import br.ufms.cpcx.grasp.grasp.exception.NenhumaRestricaoFracaAtivaException;
import br.ufms.cpcx.grasp.restricoes.ERestricao;
import br.ufms.cpcx.grasp.restricoes.ESituacaoRestricao;
import br.ufms.cpcx.grasp.restricoes.RestricaoBO;
import br.ufms.cpcx.grasp.utils.NumericUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static br.ufms.cpcx.grasp.gradehoraria.PeriodoUltils.ehPeriodoUnico;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class GRASPImpl extends GRASP<Grafo<Integer, Integer>, Vertice<Integer>> {
    private String colunaLocal;
    private List<Entidade> entidades;
    private final String colunaHorario;
    private final String colunaCargaHoraria;
    private final RestricaoBO<Vertice<Integer>> restricaoBO;

    public GRASPImpl(EPeriodo periodo, int tamanhoListaRestritaDeCandidatos, String colunaHorario, String colunaCargaHoraria) {
        super(periodo, tamanhoListaRestritaDeCandidatos);
        this.colunaHorario = colunaHorario;
        this.colunaCargaHoraria = colunaCargaHoraria;
        this.restricaoBO = new RestricaoBO<>();
    }

    public void ativarRestricao(ERestricao tipoRestricao) {
        ativarRestricao(tipoRestricao, null);
    }

    public void ativarRestricao(ERestricao tipoRestricao, List<String> descricaoEntidades) {
        restricaoBO.ativarRestricao(tipoRestricao, descricaoEntidades);
    }

    @Override
    public Grafo<Integer, Integer> buscaLocal(Grafo<Integer, Integer> solucao) {
        return solucao;
    }

    @Override
    public void avaliarCandidatos() {
        this.solucaoGRASP.getCandidatos().forEach(verticeCandidato -> setAvaliacaoCandidato(getCandidatosAgrupadosPorGrau(), verticeCandidato));
    }


    @Override
    public Grafo<Integer, Integer> adicionarCandidatoNaSolucao(Candidato<Vertice<Integer>, Entidade> candidato) {
        if (isNull(this.solucaoGRASP.getSolucaoAtual())) {
            this.solucaoGRASP.setSolucaoAtual(new Grafo<>());
        }

        Vertice<Integer> vertice = candidato.getValor();

        if (isNull(vertice.getCor())) {
            adicionaVertice(this.solucaoGRASP.getSolucaoAtual(), vertice);
        }

        List<Integer> coresVizinhos = new ArrayList<>();

        Grafo<Integer, Integer> finalSolucao = this.solucaoGRASP.getSolucaoAtual();
        candidato.getConflitos().forEach(conflito -> {
            Vertice<Integer> vizinho = this.solucaoGRASP.getCandidatos().get(conflito - 1).getValor();

            if (nonNull(vizinho.getCor())) {
                coresVizinhos.add(vizinho.getCor());
            }

            adicionaVertice(finalSolucao, vizinho);

            finalSolucao.adicionarAresta(vertice, vizinho, null);
        });

        colorirVertice(vertice, coresVizinhos);

        this.atualizarListaCandidatos(candidato);

        return this.solucaoGRASP.getSolucaoAtual();
    }

    @Override
    protected void executarRestricoesPreColoracao() {
        if (restricaoBO.estaRestringindoPor(ERestricao.MESMO_DIA)) {
            executarRestricaoMesmoDia();
            restricaoBO.setSituacaoRestricao(ERestricao.MESMO_DIA, ESituacaoRestricao.APLICADO);
        }
    }

    public void executarRestricaoMesmoDia() {
        getCandidatosRestricaoMesmoDia().forEach(this::adicionarCandidatoNaSolucao);
    }

    private List<Candidato<Vertice<Integer>, Entidade>> getCandidatosRestricaoMesmoDia() {
        return restricaoBO.getCandidatosFullRestricao(ERestricao.MESMO_DIA, this.solucaoGRASP.getCandidatos());
    }

    @Override
    public Candidato<Vertice<Integer>, Entidade> selecionarCandidato() {
        double soma = this.solucaoGRASP.getCandidatosRestrito().stream().mapToDouble(Candidato::getAvaliacao).sum();

        double valorSorteado = Math.random() * soma;

        double somaAtual = 0.0;
        for (int i = 0; i < this.solucaoGRASP.getCandidatosRestrito().size(); i++) {
            if (i + 1 < this.solucaoGRASP.getCandidatosRestrito().size() && valorSorteado < somaAtual + this.solucaoGRASP.getCandidatosRestrito().get(i + 1).getAvaliacao()) {
                return this.solucaoGRASP.getCandidatosRestrito().get(i);
            }

            somaAtual += this.solucaoGRASP.getCandidatosRestrito().get(i).getAvaliacao();

            if (soma == somaAtual) {
                return this.solucaoGRASP.getCandidatosRestrito().get(i);
            }
        }

        return this.solucaoGRASP.getCandidatosRestrito().get(this.solucaoGRASP.getCandidatosRestrito().size() - 1);
    }

    @Override
    public void construirLRC() {
        this.solucaoGRASP.setCandidatosRestrito(getCandidatosOrdenadosPorAvaliacao(this.tamanhoListaRestritaDeCandidatos));
    }

    @Override
    public void atualizarMelhorSolucao(Grafo<Integer, Integer> solucao) {
        List<Integer> avaliacoesDias = this.solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2);
        List<Integer> avaliacoesTurmas = this.solucaoGRASP.getAvaliacoesTurmas();

        if (isNull(this.melhorSolucao) || ehMelhorSolucao(solucao, avaliacoesDias, avaliacoesTurmas)) {
            this.melhorSolucao = new MelhorSolucaoDTO<>(solucao, avaliacoesDias, avaliacoesTurmas);
        }

        this.solucaoGRASP = new SolucaoGRASP<>();

        this.lerDados();

        this.solucaoGRASP.inicializar();
    }

    private boolean ehMelhorSolucao(Grafo<Integer, Integer> solucao, List<Integer> avaliacoesDias, List<Integer> avaliacoesTurmas) {
        return AvaliadorSolucaoBO.possuiMenosCores(solucao, this.melhorSolucao)
                || (AvaliadorSolucaoBO.possuiIgualOuMenosOuCores(solucao, this.melhorSolucao)
                && AvaliadorSolucaoBO.possuiMelhorAvalicaoQueAtual(this.melhorSolucao, avaliacoesDias, avaliacoesTurmas));
    }

    @Override
    public void lerDados() {
        this.solucaoGRASP.setCandidatos(this.entidades.stream().map(CandidatoImpl::new).collect(Collectors.toList()));
    }

    @Override
    public void repararSolucao(Grafo<Integer, Integer> solucao) {
        System.out.println(this.solucaoGRASP.getGradeHorariaAtual().getAulaEmSequenciaTurmas());
        List<Integer> usaLaboratorio = ehPeriodoUnico(this.periodo) && restricaoBO.estaRestringindoPor(ERestricao.LOCAL_DISPONIVEL) ? this.solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2) : new ArrayList<>();
        this.solucaoGRASP.getGradeHorariaAtual().imprimirGradeHoraria(this.solucaoGRASP.getGradeHorariaAtual().getAvaliacoesTurmas(), usaLaboratorio);
        System.out.println();

        if (ehPeriodoUnico(this.periodo)) {
//            setGradeHorariaAtual();
            repararSolucaoPeriodoUnico();
//            setGradeHorariaAtual();
        } else {
            repararSolucaoPeriodoIntegral();
        }

//        setGradeHorariaAtual();
//        System.out.println();
//        usaLaboratorio = EPeriodo.UNICO.equals(this.periodo) && restricaoBO.estaRestringindoPor(ERestricao.LOCAL_DISPONIVEL) ? this.solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2) : new ArrayList<>();
//        this.solucaoGRASP.getGradeHorariaAtual().imprimirGradeHoraria(this.solucaoGRASP.getGradeHorariaAtual().getAvaliacoesTurmas(), usaLaboratorio);
    }

    private void repararSolucaoPeriodoUnico() {
        List<Integer> usaLaboratorio;

        int tentativasRestantes = 5;
        if (restricaoBO.estaRestringindoPor(ERestricao.LOCAL_DISPONIVEL)) {
            System.out.println();
//            setGradeHorariaAtual();
            usaLaboratorio = ehPeriodoUnico(this.periodo) && restricaoBO.estaRestringindoPor(ERestricao.LOCAL_DISPONIVEL) ? this.solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2) : new ArrayList<>();
            this.solucaoGRASP.getGradeHorariaAtual().imprimirGradeHoraria(this.solucaoGRASP.getGradeHorariaAtual().getAvaliacoesTurmas(), usaLaboratorio);

            while (tentativasRestantes-- > 0 && AvaliadorSolucaoBO.getQtdAvaliacoesNegativas(this.solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2)) > 0) {
                List<Integer> avaliacoesDias = solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2);

                List<Integer> candidatosRestricaoMesmoDia = getCandidatosRestricaoMesmoDia().stream().map(Candidato::getNumero).collect(Collectors.toList());

//                setGradeHorariaAtual();
                for (int posDia1 = 0; posDia1 < avaliacoesDias.size(); posDia1++) {
                    if (avaliacoesDias.get(posDia1) < 0) {
                        List<Entidade> entidadesDia1 = this.solucaoGRASP.getCandidatosDia(posDia1 + 1)
                                .stream().map(Candidato::getEntidade).distinct()
//                                .filter(entidade -> !candidatosRestricaoMesmoDia.contains(entidade.getId()))
                                .filter(entidade -> entidade.get(this.colunaLocal).equalsIgnoreCase("sim"))
                                .collect(Collectors.toList());

                        if (entidadesDia1.isEmpty()) continue;

                        String semestreDia1 = entidadesDia1.get(0).get(this.colunaHorario);
                        String responsavelDia1 = entidadesDia1.get(0).get("Professor");


                        for (int posDia2 = 0; posDia2 < avaliacoesDias.size(); posDia2++) {
                            if (posDia2 != posDia1 && posDia2 < 6 && avaliacoesDias.get(posDia2) > 0) {
                                List<String> semestresComEspacoNoDia2 = this.solucaoGRASP.getGradeHorariaAtual().getSemetresComEspacoNoDia(posDia2 + 1)
                                        .stream().filter(semestre -> semestre.equalsIgnoreCase(semestreDia1)).collect(Collectors.toList());
                                List<String> responsaveisNoDia2 = this.solucaoGRASP.getGradeHorariaAtual().getResponsavelNoDia(posDia2 + 1);
                                if (!semestresComEspacoNoDia2.contains(semestreDia1) || responsaveisNoDia2.contains(responsavelDia1))
                                    continue;

                                System.out.println();
                                System.out.println("------------------------------------");
//                                System.out.println("Dia 1: " + (posDia1 + 1));
//                                entidadesDia1.forEach(System.out::println);
//                                System.out.println();

//                                System.out.println("Dia 2: " + (posDia2 + 1));
//                                System.out.println(semestresComEspacoNoDia2);

                                this.solucaoGRASP.getGradeHorariaAtual().deslocarAula(entidadesDia1.get(0).getId(), posDia2 + 1);
                                System.out.println(entidadesDia1.get(0).getNome() + " movido para dia " + (posDia2 + 1));
                                System.out.println("------------------------------------");

                                System.out.println();
//                                setGradeHorariaAtual();
                                usaLaboratorio = ehPeriodoUnico(this.periodo) && restricaoBO.estaRestringindoPor(ERestricao.LOCAL_DISPONIVEL) ? this.solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2) : new ArrayList<>();
                                this.solucaoGRASP.getGradeHorariaAtual().imprimirGradeHoraria(this.solucaoGRASP.getGradeHorariaAtual().getAvaliacoesTurmas(), usaLaboratorio);
                                break;
                            }
                            setGradeHorariaAtual();
                            avaliacoesDias = solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2);
                        }
                        break;
                    }
                }
            }
        }

        atualizarCoresExistentes();
        setGradeHorariaAtual();

        if (restricaoBO.estaAtivaEAplicadaRestricao(ERestricao.MESMO_DIA)) {
            corrigirCandidatosRestricaoMesmoDia();
        }

        atualizarCoresExistentes();
        setGradeHorariaAtual();

        tentativasRestantes = 5;
        if (restricaoBO.estaRestringindoPor(ERestricao.DIAS_NAO_CONSECUTIVOS)) {
            this.solucaoGRASP.getGradeHorariaAtual().trocarDias();
            while (tentativasRestantes-- > 0 && this.solucaoGRASP.getAvaliacaoTurmas() < 0) {
                this.solucaoGRASP.getGradeHorariaAtual().trocarDias();

                System.out.println();

                System.out.println(this.solucaoGRASP.getGradeHorariaAtual().getAulaEmSequenciaTurmas());
                this.solucaoGRASP.getGradeHorariaAtual().reinicializarListas();
                List<Integer> avaliacoesDias = restricaoBO.estaRestringindoPor(ERestricao.LOCAL_DISPONIVEL) ? this.solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2) : new ArrayList<>();
                this.solucaoGRASP.getGradeHorariaAtual().imprimirGradeHoraria(this.solucaoGRASP.getAvaliacoesTurmas(), avaliacoesDias);
                System.out.println();
            }
            restricaoBO.getRestricaoFraca(ERestricao.DIAS_NAO_CONSECUTIVOS).setSituacao(ESituacaoRestricao.APLICADO);
        }

        atualizarCoresExistentes();
        setGradeHorariaAtual();

        System.out.println();
        usaLaboratorio = ehPeriodoUnico(this.periodo) && restricaoBO.estaRestringindoPor(ERestricao.LOCAL_DISPONIVEL) ? this.solucaoGRASP.getAvaliacoesDias(this.colunaLocal, 2) : new ArrayList<>();
        this.solucaoGRASP.getGradeHorariaAtual().imprimirGradeHoraria(this.solucaoGRASP.getGradeHorariaAtual().getAvaliacoesTurmas(), usaLaboratorio);
        System.out.println();
    }


    private void atualizarCoresExistentes() {
        this.solucaoGRASP.setCores(this.solucaoGRASP.getSolucaoAtual()
                .getListaDeVertices()
                .stream().map(Vertice::getCor).distinct().sorted().collect(Collectors.toList()));
    }

    private void corrigirCandidatosRestricaoMesmoDia() {
        List<Candidato<Vertice<Integer>, Entidade>> candidatosRestricao = getCandidatosRestricaoMesmoDia();
        List<Integer> coresCandidatos = this.solucaoGRASP.getCoresCandidatos(candidatosRestricao);

        int tentativasRestantes = 5;
        while (tentativasRestantes-- > 0 && coresCandidatos.size() > this.solucaoGRASP.getQtdCoresNecessaria(candidatosRestricao)) {
            Integer cor = coresCandidatos.get(coresCandidatos.size() - 1);
            List<Integer> candidatosDia = this.solucaoGRASP.getGradeHorariaAtual().getCandidatosDia(cor);
            List<Candidato<Vertice<Integer>, Entidade>> candidatosRestricaoNoDia = candidatosRestricao.stream()
                    .filter(candidato -> candidatosDia.contains(candidato.getEntidade().getId())).collect(Collectors.toList());

            String semestre = candidatosRestricaoNoDia.get(0).getEntidade().get(this.colunaHorario);
            List<Integer> diasComEspacoVazioNoSemetre = this.solucaoGRASP.getGradeHorariaAtual().getDiasComEspacoNoSemetre(semestre);

            for (Integer dia : diasComEspacoVazioNoSemetre) {
                if (coresCandidatos.contains(dia)) {
                    this.solucaoGRASP.getGradeHorariaAtual().deslocarAula(candidatosRestricaoNoDia.get(0).getNumero(), dia);
                    break;
                }
            }

            candidatosRestricao = getCandidatosRestricaoMesmoDia();
            coresCandidatos = this.solucaoGRASP.getCoresCandidatos(candidatosRestricao);
        }
    }

    private void repararSolucaoPeriodoIntegral() {
        List<String> aulaEmSequenciaTurmas = new ArrayList<>();
        this.solucaoGRASP.getGradeHorariaAtual().getAulaEmSequenciaTurmas().stream()
                .filter(aulaEmSequencia -> !aulaEmSequencia.isEmpty())
                .forEach(aulaEmSequenciaTurmas::addAll);

        aulaEmSequenciaTurmas = aulaEmSequenciaTurmas.stream().distinct().collect(Collectors.toList());

//            String aula = aulaEmSequenciaTurmas.get(aulaEmSequenciaTurmas.size()-1).get(0);
//            List<Candidato<Vertice<Integer>, Entidade>> candidatosDaAula = this.solucaoGRASP.getCandidatos().stream().filter(candidato -> candidato.getEntidade().get(this.colunaNomeEntidade).equals(aula) && candidato.getEntidade().getTipo().equals(ETipoEntidade.PRATICA)).collect(Collectors.toList());
//            List<Integer> dias = candidatosDaAula.stream().map(candidato -> candidato.getValor().getCor()).collect(Collectors.toList());
//            List<Integer> idsCandidatosDaAula = candidatosDaAula.stream().map(candidato -> candidato.getEntidade().getId()).collect(Collectors.toList());
//
//            candidatosDaAula.forEach(System.out::println);
//            String[] professoresPratica = candidatosDaAula.get(0).getEntidade().get("Professor Prática").split(",");
//            if (professoresPratica.length > 1) {
//                System.out.println(Arrays.toString(professoresPratica));
//                System.out.println(dias);
//
//                for (int i = dias.size() - 1; i >= 0; i--) {
//                    System.out.println();
//                    this.solucaoGRASP.getGradeHorariaAtual().getDia(dias.get(i)).stream()
//                            .filter(horario -> nonNull(horario.getEntidade()) && !idsCandidatosDaAula.contains(horario.getEntidade()))
//                            .forEach(System.out::println);
//                }
//
//            }
        System.out.println();
        int posDia = this.solucaoGRASP.getCores().size() - 1;
        List<Integer> entidadesNoDia = this.solucaoGRASP.getGradeHorariaAtual().getDia(posDia).stream()
                .filter(horario -> nonNull(horario.getEntidade()))
                .map(HorarioAula::getEntidade).distinct().collect(Collectors.toList());

        while (posDia >= 0 && entidadesNoDia.size() != 1 || !aulaEmSequenciaTurmas.contains(this.solucaoGRASP.getCandidatos().get(entidadesNoDia.get(0) - 1).getEntidade().getNome())) {
            entidadesNoDia = this.solucaoGRASP.getGradeHorariaAtual().getDia(--posDia).stream()
                    .filter(horario -> nonNull(horario.getEntidade()))
                    .map(HorarioAula::getEntidade).distinct().collect(Collectors.toList());
            if (posDia == 0) break;
        }

        if (entidadesNoDia.size() == 1) {
            Entidade entidade = this.solucaoGRASP.getCandidatos().get(entidadesNoDia.get(0) - 1).getEntidade();
            System.out.println(entidade);
            System.out.println(entidade.getIdsEntidadesIdenticas());
            System.out.println(posDia);

            this.solucaoGRASP.getGradeHorariaAtual().getDia(posDia).stream()
                    .filter(horario -> nonNull(horario.getEntidade()))
                    .forEach(System.out::println);
        }
    }

    private void diminuirNumeroDeCores() {
        List<QtdAulaTurmaDTO> qtdAulaNaoPrencidaPorTurma = new ArrayList<>();
        List<List<HorarioAula>> horarioAulas = new ArrayList<>(this.solucaoGRASP.getGradeHorariaAtual().getHorarioAulas());
        for (int i = 0; i < horarioAulas.size(); i++) {

            List<HorarioAula> turma = new ArrayList<>(horarioAulas.get(i));

            for (int j = turma.size() - 1; j >= 0; j--) {
                if (isNull(turma.get(j).getEntidade())) {
                    turma.remove(j);
                } else {
                    break;
                }
            }

            int qtd = turma.size();
            List<HorarioAula> aulasPrencidas = turma.stream().filter(aula -> nonNull(aula.getEntidade())).collect(Collectors.toList());

            turma.forEach(aula -> System.out.print(aula.getEntidade() + " "));
            System.out.println();

            if (qtd - aulasPrencidas.size() > 0) {
                List<String> entidadesDaTurma = aulasPrencidas.stream().map(aula -> this.solucaoGRASP.getEntidades().get(aula.getEntidade() - 1).getNome()).collect(Collectors.toList());

                int quantidadePrencidaPodeMover = restricaoBO.getDiasQuePodeMover(entidadesDaTurma).size();

                qtdAulaNaoPrencidaPorTurma.add(new QtdAulaTurmaDTO(i, aulasPrencidas.get(0).getHorario(), qtd, qtd - aulasPrencidas.size(), quantidadePrencidaPodeMover));
            }
        }
        System.out.println();
        System.out.println(qtdAulaNaoPrencidaPorTurma);

        List<QtdAulaTurmaDTO> qtdAulaTurmaDTOS = qtdAulaNaoPrencidaPorTurma.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(QtdAulaTurmaDTO::getQuantidadeNaoPrencida)))
                .filter(qtdAulaTurmaDTO -> qtdAulaTurmaDTO.getQuantidadePrencidaPodeMover() > 0)
                .collect(Collectors.toList());

        if (!qtdAulaTurmaDTOS.isEmpty()) {
            List<HorarioAula> ultimoDia = this.solucaoGRASP.getGradeHorariaAtual().getDia(this.solucaoGRASP.getCores().size() - 1)
                    .stream().filter(aula -> nonNull(aula.getEntidade())).collect(Collectors.toList());

            if (ultimoDia.isEmpty()) {
                List<Candidato<Vertice<Integer>, Entidade>> candidatosNaCor = this.solucaoGRASP.getCandidatos().stream().filter(verticeEntidadeCandidato -> verticeEntidadeCandidato.getValor().getCor().equals(this.solucaoGRASP.getCores().size() - 1)).collect(Collectors.toList());
                System.out.println("cor " + (this.solucaoGRASP.getCores().size() - 1));
                System.out.println(ultimoDia);
            }
            for (QtdAulaTurmaDTO qtdAulaTurmaDTO : qtdAulaTurmaDTOS) {
                if (ultimoDia.get(0).getHorario().equals(qtdAulaTurmaDTO.getHorario())) {
                    System.out.println(horarioAulas.get(qtdAulaTurmaDTO.getId()));
                }
            }
        }
    }

    @Override
    public boolean ehValida(Grafo<Integer, Integer> solucao) throws NenhumaRestricaoFracaAtivaException {
        restricaoBO.validarExistenciaRestricaoFraca();

        int avaliacaoTurmas = 0;
        int avaliacaoDias = 0;

        this.solucaoGRASP.setGradeHorariaAtual(null);
        if (restricaoBO.estaRestringindoPor(ERestricao.DIAS_NAO_CONSECUTIVOS)) {
            setGradeHorariaAtual();
            avaliacaoTurmas = this.solucaoGRASP.getAvaliacaoTurmas();
//            System.out.println();
        }

        int qtdLaboratorios = 2;
        if (restricaoBO.estaRestringindoPor(ERestricao.LOCAL_DISPONIVEL) && ehPeriodoUnico(this.periodo)) {
            if (isNull(this.solucaoGRASP.getGradeHorariaAtual())) setGradeHorariaAtual();
            avaliacaoDias = (int) AvaliadorSolucaoBO.getQtdAvaliacoesNegativas(this.solucaoGRASP.getAvaliacoesDias(this.colunaLocal, qtdLaboratorios)) * -1;
        }

        return avaliacaoTurmas >= 0 && avaliacaoDias >= 0;
    }

    private void setGradeHorariaAtual() {
        List<Candidato<Vertice<Integer>, Entidade>> candidatos = this.solucaoGRASP.getCandidatos();
        List<Integer> coresExistentes = candidatos.stream().map(candidato -> candidato.getValor().getCor()).distinct().sorted().collect(Collectors.toList());
        List<Integer> cores = new ArrayList<>();
        for (int i = 0; i < coresExistentes.size(); i++) {
            cores.add(i + 1);
        }
        candidatos.forEach(candidato -> candidato.getValor().setCor(cores.get(coresExistentes.indexOf(candidato.getValor().getCor()))));
        this.solucaoGRASP.setCores(cores);

        this.solucaoGRASP.setGradeHorariaAtual(new GradeHoraria(this.solucaoGRASP.getCores().size(), this.solucaoGRASP.getCandidatos(), this.colunaHorario, this.colunaCargaHoraria));
    }

    public List<Candidato<Vertice<Integer>, Entidade>> getCandidatosOrdenadosPorAvaliacao(int qtdMaximaCandidatos) {
        HashMap<Integer, List<Integer>> candidatosAgrupadosPorGrau = this.getCandidatosAgrupadosPorGrau();

        TreeMap<Integer, Integer> qtdEmCadaGrau = new TreeMap<>();
        candidatosAgrupadosPorGrau.forEach((chave, valor) -> qtdEmCadaGrau.put(chave, valor.size()));

        List<Integer> grausOrdenadoPorQuantidadeEmCadaVertice = qtdEmCadaGrau.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList());

        List<Candidato<Vertice<Integer>, Entidade>> candidatosOrdenadosPorAvaliacao = new ArrayList<>();
        for (int i = grausOrdenadoPorQuantidadeEmCadaVertice.size() - 1; i >= 0; i--) {
            Integer grau = grausOrdenadoPorQuantidadeEmCadaVertice.get(i);

            List<Integer> numerosCandidatosNoGrau = candidatosAgrupadosPorGrau.get(grau);

            Collections.sort(numerosCandidatosNoGrau);

            for (Integer numeroCandidato : numerosCandidatosNoGrau) {
                Candidato<Vertice<Integer>, Entidade> candidato = this.solucaoGRASP.getCandidatos().get(numeroCandidato - 1);
                if (!this.solucaoGRASP.getCandidatosColoridos().contains(candidato.getValor())) {
                    candidatosOrdenadosPorAvaliacao.add(candidato);
                }

                if (candidatosOrdenadosPorAvaliacao.size() == qtdMaximaCandidatos) {
                    break;
                }
            }

            if (candidatosOrdenadosPorAvaliacao.size() == qtdMaximaCandidatos) {
                break;
            }
        }

        return candidatosOrdenadosPorAvaliacao;
    }

    public void setCandidatos(List<Entidade> entidades) {
        this.entidades = entidades;
    }

    @Override
    public void setColunasRestricoesFortes(List<String> colunasRestricoesFortes) {
        restricaoBO.setColunasRestricoesFortes(colunasRestricoesFortes);
    }

    public void setColunaLocal(String colunaLocal) {
        this.colunaLocal = colunaLocal;
    }

    private void colorirVertice(Vertice<Integer> vertice, List<Integer> coresVizinhos) {
        if (this.solucaoGRASP.getCores().isEmpty()) {
            this.solucaoGRASP.getCores().add(1);
        }

        if (coresVizinhos.isEmpty()) {
            vertice.setCor(this.solucaoGRASP.getCores().get(0));
        } else if (coresVizinhos.size() < this.solucaoGRASP.getCores().size()) {
            vertice.setCor(this.solucaoGRASP.getProximaMenorCor(coresVizinhos));
        } else {
            int maiorCor = coresVizinhos.stream().mapToInt(cor -> cor).max().orElse(0);
            int cor = maiorCor + 1;

            vertice.setCor(cor);

            if (!this.solucaoGRASP.getCores().contains(cor)) {
                this.solucaoGRASP.getCores().add(cor);
            }
        }
    }

    private Grafo<Integer, Integer> gerarGrafo() {
        Grafo<Integer, Integer> grafo = new Grafo<>();

        this.solucaoGRASP.getCandidatos().forEach(verticeCandidato -> {
            Vertice<Integer> vertice = verticeCandidato.getValor();
            adicionaVertice(grafo, vertice);

            verticeCandidato.getConflitos().forEach(conflito -> grafo.adicionarAresta(verticeCandidato.getValor(), this.solucaoGRASP.getCandidatos().get(conflito - 1).getValor(), null));
        });

        return grafo;
    }

    private void adicionaVertice(Grafo<Integer, Integer> solucao, Vertice<Integer> vertice) {
        if (isNull(solucao.buscaVertice(vertice.getRotulo()))) {
            solucao.adicionaVertice(vertice);
        }
    }

    private void setAvaliacaoCandidato
            (HashMap<Integer, List<Integer>> verticesAgrupadosPorGrau, Candidato<Vertice<Integer>, Entidade> verticeCandidato) {
        double avaliacao = NumericUtils.getDouble(verticesAgrupadosPorGrau.get(verticeCandidato.getGrau()).size()) / NumericUtils.getDouble(this.solucaoGRASP.getCandidatos().size());
        BigDecimal av = BigDecimal.valueOf(avaliacao).setScale(2, RoundingMode.HALF_EVEN);
        verticeCandidato.setAvaliacao(av.doubleValue());
    }

    private HashMap<Integer, List<Integer>> getCandidatosAgrupadosPorGrau() {
        Grafo<Integer, Integer> grafo = this.gerarGrafo();

        HashMap<Integer, List<Integer>> graus = new HashMap<>();
        this.solucaoGRASP.getCandidatos().forEach(verticeCandidato -> {
            int grau = grafo.listaRotuloAdjacentesPorDestino(verticeCandidato.getValor()).size();

            verticeCandidato.setGrau(grau);

            graus.put(grau, adicionarNaListaDeVerticesNoGrau(graus.get(grau), verticeCandidato.getNumero()));
        });

        return graus;
    }

    private List<Integer> adicionarNaListaDeVerticesNoGrau(List<Integer> listaVerticesNoGrau, Integer
            numeroCandidato) {
        if (isNull(listaVerticesNoGrau)) {
            listaVerticesNoGrau = new ArrayList<>();
        }

        listaVerticesNoGrau.add(numeroCandidato);

        return listaVerticesNoGrau;
    }
}
