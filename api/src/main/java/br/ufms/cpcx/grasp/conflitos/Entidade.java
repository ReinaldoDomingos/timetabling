package br.ufms.cpcx.grasp.conflitos;


import br.ufms.cpcx.grasp.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Entidade extends HashMap<String, String> {
    private Integer id;
    private String nome;
    private Integer numero;
    private String semestre;
    private Integer cargaHoraria;
    private List<String> atributos;
    private List<String> idsEntidadesIdenticas;
    private List<String> listaDeEntidadesComConflitos;

    public Entidade(Integer numero) {
        this.numero = numero;
        inicializarListas();
    }

    public Entidade() {
        inicializarListas();
    }

    private void inicializarListas() {
        this.atributos = new ArrayList<>();
        this.idsEntidadesIdenticas = new ArrayList<>();
        this.listaDeEntidadesComConflitos = new ArrayList<>();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getSemestre() {
        return this.semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Integer getCargaHoraria() {
        return this.cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public void setAtributos(List<String> atributos) {
        this.atributos = atributos;
    }

    public List<String> getListaDeEntidadesComConflitos() {
        return this.listaDeEntidadesComConflitos;
    }

    public List<String> getIdsEntidadesIdenticas() {
        return idsEntidadesIdenticas;
    }

    public void setIdsEntidadesIdenticas(List<String> idsEntidadesIdenticas) {
        this.idsEntidadesIdenticas = idsEntidadesIdenticas;
    }

    public void adicionarEntidadesComConflitos(List<String> lista) {
        List<String> listaFiltrada = lista.stream()
//                .filter(item -> !this.idsEntidadesIdenticas.contains(item))
                .filter(item -> !jaExisteConflito(item))
                .collect(Collectors.toList());

        this.listaDeEntidadesComConflitos.addAll(listaFiltrada);
    }

    private boolean jaExisteConflito(String item) {
        return item.equals(this.getId().toString()) || this.listaDeEntidadesComConflitos.contains(item);
    }

    @Override
    public String put(String chave, String valor) {
        validarAtributo(chave);
        return super.put(chave, valor);
    }

    private String gerarTextoLinhaAtributo(Object atributo, Object valor) {
        return atributo + "=" + valor + "\n";
    }

    private void validarAtributo(Object chave) {
        if (!this.atributos.contains(chave)) {
            this.atributos.add(chave.toString());
        }
    }

    @Override
    public String toString() {

        Object[] chaves = keySet().toArray();
        Object[] valores = values().toArray();
        StringBuilder retorno = new StringBuilder();
        retorno.append(gerarTextoLinhaAtributo("Id", this.id));
        for (int i = 0; i < keySet().size(); i++) {
            String atributoComValor = gerarTextoLinhaAtributo(chaves[i], valores[i]);
            retorno.append(atributoComValor);
        }
        retorno.append(gerarTextoLinhaAtributo("conflitos", this.listaDeEntidadesComConflitos.toString()));

        return StringUtils.removerColchetes(retorno.toString());
    }
}
