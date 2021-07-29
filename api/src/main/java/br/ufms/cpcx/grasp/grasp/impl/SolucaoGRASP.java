package br.ufms.cpcx.grasp.grasp.impl;

import br.ufms.cpcx.grasp.conflitos.Entidade;
import br.ufms.cpcx.grasp.gradehoraria.GradeHoraria;
import br.ufms.cpcx.grasp.gradehoraria.HorarioAula;
import br.ufms.cpcx.grasp.grasp.Candidato;
import br.ufms.cpcx.grasp.grasp.Vertice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class SolucaoGRASP<T, E> {
    private T solucaoAtual;
    private List<Integer> cores;
    private List<E> candidatosColoridos;
    private GradeHoraria gradeHorariaAtual;
    private List<Candidato<E, Entidade>> candidatos;
    private List<Candidato<E, Entidade>> candidatosRestrito;

    public SolucaoGRASP() {
        this.candidatos = new ArrayList<>();
        inicializar();
    }

    public void inicializar() {
        this.cores = new ArrayList<>();
        this.candidatosRestrito = new ArrayList<>();
        this.candidatosColoridos = new ArrayList<>();
    }

    public List<Integer> getCores() {
        return this.cores;
    }

    public void setCores(List<Integer> cores) {
        this.cores = cores;
    }

    public List<E> getCandidatosColoridos() {
        return this.candidatosColoridos;
    }

    public List<Candidato<E, Entidade>> getCandidatos() {
        return this.candidatos;
    }

    public void setCandidatos(List<Candidato<E, Entidade>> candidatos) {
        this.candidatos = candidatos;
    }

    public List<Candidato<E, Entidade>> getCandidatosRestrito() {
        return candidatosRestrito;
    }

    public void setCandidatosRestrito(List<Candidato<E, Entidade>> candidatosRestrito) {
        this.candidatosRestrito = candidatosRestrito;
    }

    public T getSolucaoAtual() {
        return this.solucaoAtual;
    }

    public void setSolucaoAtual(T solucaoAtual) {
        this.solucaoAtual = solucaoAtual;
    }

    public GradeHoraria getGradeHorariaAtual() {
        return this.gradeHorariaAtual;
    }

    public void setGradeHorariaAtual(GradeHoraria gradeHorariaAtual) {
        this.gradeHorariaAtual = gradeHorariaAtual;
    }

    public List<Entidade> getEntidades() {
        return this.candidatos.stream().map(Candidato::getEntidade).collect(Collectors.toList());
    }

    public Integer getProximaMenorCor(List<Integer> coresVizinhos) {
        return this.cores.stream().filter(cor -> !coresVizinhos.contains(cor))
                .mapToInt(cor -> cor).min().orElse(this.cores.size() + 1);
    }

    public int getAvaliacaoDias(String colunaLocal, int qtdLaboratorios) {
        return this.getAvaliacoesDias(colunaLocal, qtdLaboratorios).stream().mapToInt(v -> v).sum();
    }

    public List<Integer> getAvaliacoesDias(String colunaLocal, int qtdLaboratorios) {
        List<Integer> avaliacoesDias = new ArrayList<>();

        this.cores.forEach(cor -> {
            AtomicInteger soma = new AtomicInteger(qtdLaboratorios);
            this.gradeHorariaAtual.getDia(cor - 1).stream()
                    .map(HorarioAula::getEntidade)
                    .filter(Objects::nonNull).distinct()
                    .forEach(entidade -> {
                        String usaLaboratorio = this.candidatos.get(entidade - 1).getEntidade().get(colunaLocal);
                        if (usaLaboratorio.equalsIgnoreCase("sim")) {
                            soma.getAndDecrement();
                        }
                    });
            avaliacoesDias.add(soma.get());
        });

        return avaliacoesDias;
    }

    public int getAvaliacaoTurmas() {
        return (int) AvaliadorSolucaoBO.getQtdAvaliacoesNegativas(this.gradeHorariaAtual.getAvaliacoesTurmas()) * -1;
    }

    public List<Integer> getAvaliacoesTurmas() {
        return this.gradeHorariaAtual.getAvaliacoesTurmas();
    }

    public List<Integer> getCoresCandidatos(List<Candidato<Vertice<Integer>, Entidade>> candidatos) {
        return candidatos.stream().map(candidato -> candidato.getValor().getCor()).distinct().collect(Collectors.toList());
    }

    public int getQtdCoresNecessaria(List<Candidato<Vertice<Integer>, Entidade>> candidatos) {
        Map<String, Integer> qtdRepeticoesDeCadaTurmaNoSemestre = new TreeMap<>();
        for (Candidato<Vertice<Integer>, Entidade> candidato : candidatos) {
            String nomeEntidade = candidato.getEntidade().getNome();
            Integer qtdRepeticoesDisciplinaNoSemestre = qtdRepeticoesDeCadaTurmaNoSemestre.get(nomeEntidade);
            if (isNull(qtdRepeticoesDisciplinaNoSemestre)) {
                qtdRepeticoesDisciplinaNoSemestre = 0;
            }
            qtdRepeticoesDeCadaTurmaNoSemestre.put(nomeEntidade, ++qtdRepeticoesDisciplinaNoSemestre);
        }

        Optional<Integer> minimoDeCores = qtdRepeticoesDeCadaTurmaNoSemestre.values().stream().max(Comparator.comparing(Integer::intValue));
        return minimoDeCores.map(integer -> Integer.parseInt(String.valueOf(integer))).orElse(-1);
    }

    public List<Candidato<E, Entidade>> getCandidatosDia(int dia) {
        return this.gradeHorariaAtual.getCandidatosDia(dia).stream().map(candidato -> this.candidatos.get(candidato - 1)).collect(Collectors.toList());
    }
}
