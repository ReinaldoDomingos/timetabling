package br.ufms.cpcx.grasp.conflitos;

import java.util.ArrayList;
import java.util.List;

public class Conflito {
    private final String valor;
    private final List<String> listaDeEntidadesComConflitos;

    public Conflito(String valor) {
        this.valor = valor;
        this.listaDeEntidadesComConflitos = new ArrayList<>();
    }

    public String getValor() {
        return this.valor;
    }

    public List<String> getListaDeEntidadesComConflitos() {
        return this.listaDeEntidadesComConflitos;
    }

    public void adicionarEntidadeComConflito(String numero) {
        if (!this.listaDeEntidadesComConflitos.contains(numero)) {
            this.listaDeEntidadesComConflitos.add(numero);
        }
    }

    @Override
    public String toString() {
        return "Entidade " + this.valor + ": conflitos=" + this.listaDeEntidadesComConflitos;
    }
}
