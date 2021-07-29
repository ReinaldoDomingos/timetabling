package br.ufms.cpcx.grasp.restricoes;

import br.ufms.cpcx.grasp.generico.EStatus;

import java.util.List;

public class Restricao {
    private EStatus status;
    private final ERestricao tipo;
    private List<String> entidades;
    private ESituacaoRestricao situacao;

    public Restricao(ERestricao tipo) {
        this.tipo = tipo;
        this.status = EStatus.INATIVO;
        this.situacao = ESituacaoRestricao.NAO_APLICADO;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public ESituacaoRestricao getSituacao() {
        return this.situacao;
    }

    public void setSituacao(ESituacaoRestricao situacao) {
        this.situacao = situacao;
    }

    public ERestricao getTipo() {
        return tipo;
    }

    public List<String> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<String> entidades) {
        this.entidades = entidades;
    }
}
