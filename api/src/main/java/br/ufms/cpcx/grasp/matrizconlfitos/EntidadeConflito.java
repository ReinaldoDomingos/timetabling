package br.ufms.cpcx.grasp.matrizconlfitos;

import java.util.ArrayList;
import java.util.List;

public class EntidadeConflito {
    private final Integer numero;
    private final List<Integer> listaDeEntidadesComConflitos;

    public EntidadeConflito(Integer numero) {
        this.numero = numero;
        this.listaDeEntidadesComConflitos = new ArrayList<>();
    }

    public EntidadeConflito(Integer numero, List<Integer> listaDeEntidadesComConflitos) {
        this.numero = numero;
        this.listaDeEntidadesComConflitos = listaDeEntidadesComConflitos;
    }

    public Integer getNumero() {
        return numero;
    }

    public List<Integer> getListaDeEntidadesComConflitos() {
        return listaDeEntidadesComConflitos;
    }

    @Override
    public String toString() {
        return "Entidade " + numero + ": conflitos=" + listaDeEntidadesComConflitos;
    }
}
