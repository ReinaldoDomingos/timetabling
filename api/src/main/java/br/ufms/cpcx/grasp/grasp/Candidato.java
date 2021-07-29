package br.ufms.cpcx.grasp.grasp;

import java.util.ArrayList;
import java.util.List;

public abstract class Candidato<T, E> {
    protected T valor;
    protected E entidade;
    protected Integer numero;
    protected Integer grau;
    protected Double avaliacao;
    protected List<Integer> conflitos;

    protected Candidato() {
        this.avaliacao = 0.0;
        conflitos = new ArrayList<>();
    }

    public Integer getGrau() {
        return grau;
    }

    public void setGrau(Integer grau) {
        this.grau = grau;
    }

    public List<Integer> getConflitos() {
        return conflitos;
    }

    public void setConflitos(List<Integer> conflitos) {
        this.conflitos = conflitos;
    }

    public E getEntidade() {
        return entidade;
    }

    public void setEntidade(E entidade) {
        this.entidade = entidade;
    }

    public T getValor() {
        return valor;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public String toString() {
        return "Candidato " + String.format("%02d", this.numero) + " {avaliação= " + this.avaliacao + ", conflitos = " + this.conflitos + "}";
    }
}
