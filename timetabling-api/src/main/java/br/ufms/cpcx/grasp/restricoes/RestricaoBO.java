package br.ufms.cpcx.grasp.restricoes;

import br.ufms.cpcx.grasp.conflitos.Entidade;
import br.ufms.cpcx.grasp.generico.EStatus;
import br.ufms.cpcx.grasp.grasp.Candidato;
import br.ufms.cpcx.grasp.grasp.exception.NenhumaRestricaoFracaAtivaException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RestricaoBO<E> {
    private final List<Restricao> restricoesFracas;
    private List<String> colunasRestricoesFortes;

    public RestricaoBO() {
        this.restricoesFracas = RestricaoUtils.getListaRestricoes();
    }

    public void ativarRestricao(ERestricao tipoRestricao, List<String> entidades) {
        this.restricoesFracas.stream()
                .filter(restricao -> restricao.getTipo().equals(tipoRestricao))
                .forEach(restricao -> ativarRestricao(entidades, restricao));
    }

    private void ativarRestricao(List<String> entidades, Restricao restricao) {
        restricao.setStatus(EStatus.ATIVO);
        restricao.setEntidades(entidades);
    }

    public List<Integer> getIdsCandidatosRestricao(ERestricao tipoRestricao, List<Candidato<E, Entidade>> candidatos) {
        return this.getCandidatosFullRestricao(tipoRestricao, candidatos).stream().map(candidato -> candidato.getEntidade().getId()).collect(Collectors.toList());
    }

    public List<Candidato<E, Entidade>> getCandidatosFullRestricao(ERestricao tipoRestricao, List<Candidato<E, Entidade>> candidatos) {
        Restricao restricaoMesmoDia = getRestricaoFraca(tipoRestricao);

        if (restricaoMesmoDia.getStatus().equals(EStatus.ATIVO)) {
            return candidatos.stream()
                    .filter(candidato -> restricaoMesmoDia.getEntidades().contains(candidato.getEntidade().getNome()))
                    .sorted(Comparator.comparing(Candidato::getNumero)).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public Restricao getRestricaoFraca(ERestricao tipoRestricao) {
        return this.restricoesFracas.stream()
                .filter(restricao -> tipoRestricao.equals(restricao.getTipo()))
                .collect(Collectors.toList()).get(0);
    }

    public List<String> getDiasQuePodeMover(List<String> entidades) {
        List<String> entidadesMesmoDia = getRestricaoFraca(ERestricao.MESMO_DIA).getEntidades();
        return entidades.stream().filter(entidade -> !entidadesMesmoDia.contains(entidade)).collect(Collectors.toList());
    }

    public void validarExistenciaRestricaoFraca() throws NenhumaRestricaoFracaAtivaException {
        if (getRestricoesAtivas().isEmpty()) {
            throw new NenhumaRestricaoFracaAtivaException();
        }
    }

    public boolean estaRestringindoPor(ERestricao restricao) {
        return getRestricoesAtivas().contains(restricao);
    }

    public boolean estaAtivaEAplicadaRestricao(ERestricao tipoRestricao) {
        Restricao restricaoFraca = getRestricaoFraca(tipoRestricao);
        return estaAtiva(restricaoFraca) && estaAplicada(restricaoFraca);
    }

    private boolean estaAtiva(Restricao restricaoFraca) {
        return EStatus.ATIVO.equals(restricaoFraca.getStatus());
    }

    private boolean estaAplicada(Restricao restricaoFraca) {
        return ESituacaoRestricao.APLICADO.equals(restricaoFraca.getSituacao());
    }

    public void setColunasRestricoesFortes(List<String> colunasRestricoesFortes) {
        this.colunasRestricoesFortes = colunasRestricoesFortes;
    }

    public List<ERestricao> getRestricoesAtivas() {
        return this.restricoesFracas.stream()
                .filter(restricao -> restricao.getStatus().equals(EStatus.ATIVO))
                .map(Restricao::getTipo).collect(Collectors.toList());
    }

    public void setSituacaoRestricao(ERestricao tipoRestricao, ESituacaoRestricao situacaoRestricao) {
        this.getRestricaoFraca(tipoRestricao).setSituacao(situacaoRestricao);
    }
}
