package br.ufms.cpcx.grasp.gradehoraria;

import br.ufms.cpcx.grasp.conflitos.Entidade;
import br.ufms.cpcx.grasp.grasp.Grafo;
import br.ufms.cpcx.grasp.grasp.Vertice;
import br.ufms.cpcx.grasp.utils.DataUtils;
import br.ufms.cpcx.grasp.utils.xls.ColunaXls;
import br.ufms.cpcx.grasp.utils.xls.ETipoColunaXls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradeHorariaPlanilha {
    private List<String> horarios;
    private List<String> diasDaSemana;
    private final EPeriodo tipoPeriodo;
    private final List<Entidade> entidades;
    private final List<ColunaXls> cabecalho;
    private final Grafo<Integer, Integer> solucao;

    public GradeHorariaPlanilha(Grafo<Integer, Integer> grafo, List<Entidade> entidades, String colunaHorario, EPeriodo periodo) {
        this.solucao = grafo;
        this.tipoPeriodo = periodo;
        this.entidades = entidades;
        this.cabecalho = new ArrayList<>();

        setDiasDaSemana();

        setHorarios(colunaHorario);

        getVerticesAgrupadoPorCor().forEach((cor, numeroEntidade) -> adicionarColunaCabecalho(cor));
    }

    private void setDiasDaSemana() {
        if (this.tipoPeriodo.equals(EPeriodo.UNICO)) {
            this.diasDaSemana = DataUtils.getDiasDaSemana();
        } else {
            this.diasDaSemana = DataUtils.getDiasDaSemanaIntegral();
        }
    }

    private void setHorarios(String colunaHorario) {
        this.horarios = this.entidades.stream().map(entidade -> entidade.get(colunaHorario)).distinct().sorted().collect(Collectors.toList());
    }

    public List<ColunaXls> getCabecalho() {
        return this.cabecalho;
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public Map<String, List<String>> gerarGradeHoraria(List<String> atributos) {
        HashMap<String, List<String>> valoresColunas = new HashMap<>();

        getVerticesAgrupadoPorCor().forEach((cor, vizinhos) -> adicionarValorNaColuna(valoresColunas, cor, vizinhos, atributos));

        return valoresColunas;
    }

    private void adicionarColunaCabecalho(Integer cor) {
        String titulo = cor < diasDaSemana.size() ? diasDaSemana.get(cor - 1) : String.valueOf(cor);
        this.cabecalho.add(new ColunaXls(titulo, ETipoColunaXls.TEXTO));
    }

    private Map<Integer, List<Vertice<Integer>>> getVerticesAgrupadoPorCor() {
        return this.solucao.getListaDeVertices().stream().collect(Collectors.groupingBy(Vertice::getCor));
    }

    private void adicionarValorNaColuna(HashMap<String, List<String>> valoresColunas, Integer cor, List<Vertice<Integer>> vizinhos, List<String> atributos) {
        String dia = cor < diasDaSemana.size() ? this.diasDaSemana.get(cor - 1) : String.valueOf(cor);

        ArrayList<String> valores = new ArrayList<>();
        for (int i = 0; i < this.horarios.size(); i++) {
            valores.add("");
        }


        vizinhos.forEach(vertice -> valores.set(getIndiceHorario(vertice), getValor(vertice, atributos)));

        valoresColunas.put(dia, valores);
    }

    public int getIndiceHorario(Vertice<Integer> vertice) {
        String valor = this.entidades.get(vertice.getRotulo() - 1).get("Semestre");


        for (int i = 0; i < this.horarios.size(); i++) {
            if (this.horarios.get(i).equals(valor)) {
                return i;
            }
        }

        return -1;
    }

    private String getValor(Vertice<Integer> vertice, List<String> atributos) {
        StringBuilder retorno = new StringBuilder();

        atributos.forEach(atributo -> retorno.append(getValorAtributoDaEntidade(vertice.getRotulo(), atributo)).append("\n\n"));

        return retorno.toString();
    }

    public String getValorAtributoDaEntidade(int numeroEntidade, String atributo) {
        if (atributo.equals("Disciplina")) {
            return getNomeDisciplina(numeroEntidade - 1);
        }
        return this.entidades.get(numeroEntidade - 1).get(atributo);
    }

    private String getNomeDisciplina(int pos) {
        return this.entidades.get(pos).getNome();
    }
}
