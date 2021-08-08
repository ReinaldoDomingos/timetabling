package br.ufms.cpcx.grasp.grasp.impl;

import br.ufms.cpcx.grasp.conflitos.Entidade;
import br.ufms.cpcx.grasp.grasp.Candidato;
import br.ufms.cpcx.grasp.grasp.Vertice;

import java.util.stream.Collectors;

public class CandidatoImpl extends Candidato<Vertice<Integer>, Entidade> {

    public CandidatoImpl(Entidade entidade) {
        Integer numero = entidade.getId();

        this.numero = numero;
        this.entidade = entidade;
        this.valor = new Vertice<>(numero);
        this.conflitos = entidade.getListaDeEntidadesComConflitos()
                .stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
