package br.ufms.cpcx.grasp.grasp;

import br.ufms.cpcx.grasp.conflitos.Entidade;
import br.ufms.cpcx.grasp.gradehoraria.EPeriodo;
import br.ufms.cpcx.grasp.grasp.exception.NenhumaRestricaoFracaAtivaException;
import br.ufms.cpcx.grasp.grasp.impl.MelhorSolucaoDTO;
import br.ufms.cpcx.grasp.grasp.impl.SolucaoGRASP;

import java.util.List;

public abstract class GRASP<T, E> {
    protected MelhorSolucaoDTO<T> melhorSolucao;
    protected EPeriodo periodo;
    protected SolucaoGRASP<T, E> solucaoGRASP;
    protected int tamanhoListaRestritaDeCandidatos;

    protected GRASP(EPeriodo periodo, int tamanhoListaRestritaDeCandidatos) {
        this.periodo = periodo;
        this.tamanhoListaRestritaDeCandidatos = tamanhoListaRestritaDeCandidatos;
    }

    public SolucaoGRASP<T, E> getSolucaoGRASP() {
        return solucaoGRASP;
    }

    public MelhorSolucaoDTO<T> execute(int maximoIteracoes) throws NenhumaRestricaoFracaAtivaException {
        this.solucaoGRASP = new SolucaoGRASP<>();

        this.lerDados();

        for (int i = 0; i < maximoIteracoes * 2; i++) {
            T solucao = this.construirSolucao();

            if (!ehValida(solucao)) {
                this.repararSolucao(solucao);
            }

            this.buscaLocal(solucao);

            this.atualizarMelhorSolucao(solucao);
        }

        return this.melhorSolucao;
    }

    public T construirSolucao() {
        T solucao = null;

        this.avaliarCandidatos();

        executarRestricoesPreColoracao();

        while (this.solucaoGRASP.getCandidatosColoridos().size() != this.solucaoGRASP.getCandidatos().size()) {

            this.construirLRC();

            Candidato<E, Entidade> candidato = this.selecionarCandidato();

            solucao = this.adicionarCandidatoNaSolucao(candidato);

            this.atualizarListaCandidatos(candidato);

            this.avaliarCandidatos();
        }

        System.out.println(this.solucaoGRASP.getCores().size() + " cores");
        return solucao;
    }

    protected abstract void executarRestricoesPreColoracao();

    public abstract T buscaLocal(T solucao);

    public abstract void avaliarCandidatos();

    public void atualizarListaCandidatos(Candidato<E, Entidade> candidato) {
        if (!this.solucaoGRASP.getCandidatosColoridos().contains(candidato.getValor())) {
            this.solucaoGRASP.getCandidatosColoridos().add(candidato.getValor());
        }
    }

    public abstract T adicionarCandidatoNaSolucao(Candidato<E, Entidade> candidato);

    public abstract Candidato<E, Entidade> selecionarCandidato();

    public abstract void construirLRC();

    public abstract void atualizarMelhorSolucao(T solucao);

    public abstract void lerDados();

    public abstract void repararSolucao(T solucao);

    public List<Candidato<E, Entidade>> getCandidatosRestrito() {
        return this.solucaoGRASP.getCandidatosRestrito();
    }

    public List<Integer> getCores() {
        return this.solucaoGRASP.getCores();
    }

    public void setCores(List<Integer> cores) {
        this.solucaoGRASP.setCores(cores);
    }

    public abstract boolean ehValida(T solucao) throws NenhumaRestricaoFracaAtivaException;

    public abstract void setColunasRestricoesFortes(List<String> colunasRestricoesFortes);

}
