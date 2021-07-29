package br.ufms.cpcx.grasp.conflitos;

import br.ufms.cpcx.grasp.conflitos.exception.ColunaInexistentException;
import br.ufms.cpcx.grasp.utils.StringUtils;
import br.ufms.cpcx.grasp.utils.txt.ExportarTxt;
import br.ufms.cpcx.grasp.utils.xls.LeitorXlS;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

public class GeradorListaDeConflitos {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeradorListaDeConflitos.class);

    private String nomeArquivo;
    private String colunaSemestre;
    private List<String> cabecalho;
    private String colunaCargaHoraria;
    private String colunaIdentificador;
    private String colunaQtdTurmaPratica;
    private final List<Entidade> entidades;
    private final String colunaNomeEntidade;
    private String colunaCargaHorariaPratica;
    private String colunaCargaHorariaTeorica;
    private final List<String> colunasRestritas;

    public GeradorListaDeConflitos(String colunaNomeEntidade) {
        this.entidades = new ArrayList<>();
        this.cabecalho = new ArrayList<>();
        this.colunasRestritas = new ArrayList<>();
        this.colunaNomeEntidade = colunaNomeEntidade;
    }

    public List<Entidade> getEntidades() {
        return this.entidades;
    }

    public void setColunaIdentificador(String colunaIdentificador) {
        this.colunaIdentificador = colunaIdentificador;
    }

    public void setColunaCargaHoraria(String colunaCargaHoraria) {
        this.colunaCargaHoraria = colunaCargaHoraria;
    }

    public void setColunaCargaHorariaPratica(String colunaCargaHorariaPratica) {
        this.colunaCargaHorariaPratica = colunaCargaHorariaPratica;
    }

    public void setColunaCargaHorariaTeorica(String colunaCargaHorariaTeorica) {
        this.colunaCargaHorariaTeorica = colunaCargaHorariaTeorica;
    }

    public void setColunaQtdTurmaPratica(String colunaQtdTurmaPratica) {
        this.colunaQtdTurmaPratica = colunaQtdTurmaPratica;
    }

    public String getColunaSemestre() {
        return colunaSemestre;
    }

    public void setColunaSemestre(String colunaSemestre) {
        this.colunaSemestre = colunaSemestre;
    }

    public void lerRegistrosTabuladosNoXlS(String nomeArquivo, List<String> colunasProfessor) throws Exception {
        System.out.println("-------------------------------------------------------");
        setNomeArquivo(nomeArquivo);

        List<String> linhas = LeitorXlS.getLinhas(nomeArquivo);

        this.cabecalho = gerarListaAtributos(linhas.get(0));

        try {
            validarColuna(this.colunaCargaHoraria);

            int i = 1;
            while (i < linhas.size()) {
                adicionarEntidades(linhas.get(i++));
            }

            if(colunasProfessor.size()>1){
                juntarColunas("Professor", colunasProfessor.get(0), colunasProfessor.get(1));
            }
            Map<String, List<Entidade>> aulasPeriodoParcialPorSemestre = this.entidades.stream()
                    .filter(entidade -> entidade.getCargaHoraria().compareTo(4) < 0).collect(Collectors.groupingBy(Entidade::getSemestre));

            for (Map.Entry<String, List<Entidade>> entry : aulasPeriodoParcialPorSemestre.entrySet()) {
                String semestre = entry.getKey();
                List<Entidade> entidadesNoSemestre = entry.getValue();
                while (entidadesNoSemestre.size() > 2) {
                    Entidade disciplina1 = entidadesNoSemestre.remove(0);
                    Entidade disciplina2 = entidadesNoSemestre.remove(0);

                    disciplina1.setNome(disciplina1.getNome() + " | " + disciplina2.getNome());
                    disciplina1.getIdsEntidadesIdenticas().addAll(disciplina2.getIdsEntidadesIdenticas());

                    List<String> professores = Arrays.stream(disciplina1.get("Professor").split(",")).collect(Collectors.toList());
                    professores.addAll(Arrays.stream(disciplina2.get("Professor").split(",")).collect(Collectors.toList()));

                    disciplina1.put("Professor", StringUtils.getTextoDaLista(professores.stream().distinct().collect(Collectors.toList())));

                    this.entidades.remove(disciplina2);
                    for (int j = 0; j < this.entidades.size(); j++) {
                        this.entidades.get(j).setId(j + 1);
                    }
                }
            }
        } catch (ColunaInexistentException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        System.out.println("-----------------Leitura arquivo finalizada---------------------");
    }

    public void lerRegistrosTabuladosNoDTO(String nomeArquivo, List<String> colunasProfessor, List<String> linhas) throws Exception {
        System.out.println("-------------------------------------------------------");
        setNomeArquivo(nomeArquivo);

        this.cabecalho = gerarListaAtributos(linhas.get(0));

        try {
            validarColuna(this.colunaCargaHoraria);

            int i = 1;
            while (i < linhas.size()) {
                adicionarEntidades(linhas.get(i++));
            }

            if(colunasProfessor.size()>1){
                juntarColunas("Professor", colunasProfessor.get(0), colunasProfessor.get(1));
            }

            Map<String, List<Entidade>> aulasPeriodoParcialPorSemestre = this.entidades.stream()
                    .filter(entidade -> entidade.getCargaHoraria().compareTo(4) < 0).collect(Collectors.groupingBy(Entidade::getSemestre));

            for (Map.Entry<String, List<Entidade>> entry : aulasPeriodoParcialPorSemestre.entrySet()) {
                String semestre = entry.getKey();
                List<Entidade> entidadesNoSemestre = entry.getValue();
                while (entidadesNoSemestre.size() > 2) {
                    Entidade disciplina1 = entidadesNoSemestre.remove(0);
                    Entidade disciplina2 = entidadesNoSemestre.remove(0);

                    disciplina1.setNome(disciplina1.getNome() + " | " + disciplina2.getNome());
                    disciplina1.getIdsEntidadesIdenticas().addAll(disciplina2.getIdsEntidadesIdenticas());

                    List<String> professores = Arrays.stream(disciplina1.get("Professor").split(",")).collect(Collectors.toList());
                    professores.addAll(Arrays.stream(disciplina2.get("Professor").split(",")).collect(Collectors.toList()));

                    disciplina1.put("Professor", StringUtils.getTextoDaLista(professores.stream().distinct().collect(Collectors.toList())));

                    this.entidades.remove(disciplina2);
                    for (int j = 0; j < this.entidades.size(); j++) {
                        this.entidades.get(j).setId(j + 1);
                    }
                }
            }
        } catch (ColunaInexistentException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        System.out.println("-----------------Leitura arquivo finalizada---------------------");
    }

    private void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo.replace(".xlsx", "")
                .replace(".xls", "") + "-lista-conflitos.txt";
    }

    private void validarColuna(String coluna) throws ColunaInexistentException {
        if (!this.cabecalho.contains(coluna))
            throw new ColunaInexistentException("Coluna Inexistente: " + coluna);
    }

    private void adicionarEntidades(String linha) {
        String[] valores = linha.split("\t");

        if (valores.length == 0) return;

        int cargaHoraria = Integer.parseInt(valores[this.cabecalho.indexOf(this.colunaCargaHoraria)]);

        if (isNull(this.colunaCargaHorariaPratica)) {
            adicionarEntidades(valores, cargaHoraria);
        } else {
            int numeroTurmaPratica = Integer.parseInt(valores[this.cabecalho.indexOf(this.colunaQtdTurmaPratica)]);
            int cargaHorariaPratica = Integer.parseInt(valores[this.cabecalho.indexOf(this.colunaCargaHorariaPratica)]);
            int cargaHorariaTeorica = Integer.parseInt(valores[this.cabecalho.indexOf(this.colunaCargaHorariaTeorica)]);
            String professoresPratica = valores[this.cabecalho.indexOf("Professor Prática")];
            int qtdProfessores = professoresPratica.split(",").length;

            String[] valoresEntidade = Arrays.copyOf(valores, valores.length);
            if (qtdProfessores > 0) {
                valoresEntidade[this.cabecalho.indexOf(this.colunaNomeEntidade)] = valoresEntidade[this.cabecalho.indexOf(this.colunaNomeEntidade)] + " Teórica";
            }
            adicionarEntidades(valoresEntidade, cargaHorariaTeorica);

//            StringBuilder numTurmas = new StringBuilder("\n(");
//            for (int i = 0; i < qtdProfessores; i++) {
//                numTurmas.append("P").append(i + 1).append(",");
//            }
//            String adicional = numTurmas.substring(0, numTurmas.length() - 1) + ")";

//            for (int i = 0; i < numeroTurmaPratica / qtdProfessores; i++) {
            for (int i = 0; i < numeroTurmaPratica; i++) {
                valoresEntidade = Arrays.copyOf(valores, valores.length);
//                valoresEntidade[this.cabecalho.indexOf(this.colunaNomeEntidade)] = valoresEntidade[this.cabecalho.indexOf(this.colunaNomeEntidade)] + " " + adicional;
                valoresEntidade[this.cabecalho.indexOf(this.colunaNomeEntidade)] = valoresEntidade[this.cabecalho.indexOf(this.colunaNomeEntidade)] + " Prática";
                adicionarEntidades(valoresEntidade, cargaHorariaPratica);
            }

/*
            numTurmas = new StringBuilder("\n(");
            int cont = qtdProfessores;
            for (int i = 0; i < qtdProfessores; i++) {
                numTurmas.append("P").append(++cont).append(",");
            }
            adicional = numTurmas.substring(0, numTurmas.length() - 1) + ")";
            for (int j = 0; j < numeroTurmaPratica % qtdProfessores; j++) {
                valoresEntidade = Arrays.copyOf(valores, valores.length);
                valoresEntidade[this.cabecalho.indexOf(this.colunaNomeEntidade)] = valoresEntidade[this.cabecalho.indexOf(this.colunaNomeEntidade)] + " " + adicional;
                adicionarEntidades(valoresEntidade, cargaHorariaPratica, ETipoEntidade.PRATICA);
            }
*/
        }
    }

    private void adicionarEntidades(String[] valores, int cargaHoraria) {
        List<String> idsEntidadesIdenticas = new ArrayList<>();

        while (cargaHoraria > 0) {
            cargaHoraria -= 4;
            adicionarEntidade(valores, idsEntidadesIdenticas, cargaHoraria >= 0 ? 4 : cargaHoraria + 4);
        }

        for (String idEntidadesIdentica : idsEntidadesIdenticas) {
            int id = Integer.parseInt(idEntidadesIdentica) - 1;
            List<String> entidadesIdenticas = idsEntidadesIdenticas.stream().filter(idEntidade -> !idEntidade.equals(idEntidadesIdentica)).collect(Collectors.toList());
            Entidade entidade = this.entidades.get(id);
            entidade.setIdsEntidadesIdenticas(entidadesIdenticas);
            entidade.adicionarEntidadesComConflitos(entidadesIdenticas);
        }
    }

    private void adicionarEntidade(String[] valores, List<String> idsEntidadesIdenticas, int cargaHoraria) {
        if (valores.length == this.cabecalho.size()) {
            int id = this.entidades.size() + 1;

            Entidade entidade = new Entidade();
            entidade.setId(id);
            entidade.setAtributos(this.cabecalho);
            entidade.setCargaHoraria(cargaHoraria);

            for (int i = 0; i < this.cabecalho.size(); i++) {
                entidade.put(this.cabecalho.get(i), valores[i]);
            }

            entidade.setNumero(Integer.parseInt(entidade.get(this.colunaIdentificador)));
            entidade.setNome(entidade.get(this.colunaNomeEntidade));
            entidade.setSemestre(entidade.get(this.colunaSemestre));

            this.entidades.add(entidade);

            idsEntidadesIdenticas.add(String.valueOf(id));
        }
    }

    public void juntarColunas(String coluna, String coluna1, String coluna2) {
        try {
            validarColuna(coluna1);
            validarColuna(coluna2);

            for (Entidade entidade : this.entidades) {
                List<String> valores = StringUtils.getListaDoTexto(entidade.get(coluna1));
                valores.addAll(StringUtils.getListaDoTexto(entidade.get(coluna2)));
                entidade.put(coluna, StringUtils.getTextoDaLista(valores));
            }
        } catch (ColunaInexistentException e) {
            LOGGER.error("Erro ao juntar colunas: " + StringUtils.limparLista(asList(coluna1, coluna2)));
        }
    }

    public void adicionarRestricaoColuna(String coluna) throws ColunaInexistentException {
        if (!possuiColunaIdentificador()) return;

        validarColuna(coluna);

        this.colunasRestritas.add(coluna);

        List<String> valoresExistentes = getValoresExistentesColuna(coluna);

        List<Conflito> conflitos = gerarEntidadesConflitoParaCadaValor(valoresExistentes);

        this.entidades.forEach(entidade -> {
            for (Conflito conflito : conflitos) {
                if (conflito.getValor().equals(String.valueOf(entidade.get(coluna)))) {
                    conflito.adicionarEntidadeComConflito(String.valueOf(entidade.getId()));
                }
            }
        });

        conflitos.forEach(conflito -> {
            List<String> listaDeEntidadesComConflitos = conflito.getListaDeEntidadesComConflitos();
            this.entidades.forEach(entidade -> {
                if (listaDeEntidadesComConflitos.contains(String.valueOf(entidade.getId()))) {
                    entidade.adicionarEntidadesComConflitos(listaDeEntidadesComConflitos);
                }
            });
        });
    }

    public void imprimirListaDeConflitos() {
        if (this.colunasRestritas.isEmpty()) {
            System.out.println("Adicione pelo menos uma restrição!");
            return;
        }

        if (!possuiColunaIdentificador()) return;

        System.out.println("------------------Lista de conflitos------------------");
        List<String> listaDeConflitos = getListaDeConflitos();


        for (String linhaListaDeConflitos : listaDeConflitos) {
            System.out.println(linhaListaDeConflitos);
        }

        System.out.println("------------------------------------------------------");

        exportarTxt(listaDeConflitos);
    }

    public List<String> getListaDeConflitos() {
        return this.entidades.stream().sorted(Comparator.comparing(Entidade::getId))
                .map(Entidade::getListaDeEntidadesComConflitos).map(this::getLinhaListaDeConflitos).collect(Collectors.toList());
    }

    private List<Conflito> gerarEntidadesConflitoParaCadaValor(List<String> valoresExistentes) {
        return valoresExistentes.stream().map(this::criarEntidadeConflito).collect(Collectors.toList());
    }

    private List<String> getValoresExistentesColuna(String coluna) {
        return this.entidades.stream().map(entidade -> entidade.get(coluna)).distinct().collect(Collectors.toList());
    }

    private String getLinhaListaDeConflitos(List<String> listaConflito) {
//        return StringUtils.limparLista(listaConflito.stream().map(conflito -> conflito + "{" + this.entidades.get(Integer.parseInt(conflito) - 1).getNumero() + "}")
        return StringUtils.limparLista(listaConflito.stream().sorted().collect(Collectors.toList()).toString());
    }

    private void exportarTxt(List<String> listaDeConflitos) {
        try {
            if (isNull(this.nomeArquivo)) return;

            ExportarTxt exportarTxt = new ExportarTxt(this.nomeArquivo);

            for (String linhaListaDeConflito : listaDeConflitos) {
                exportarTxt.escreverLinha(linhaListaDeConflito);
            }

            exportarTxt.fecharArquivo();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Erro ao exportar txt: ", e);
        }
    }

    private boolean possuiColunaIdentificador() {
        if (isNull(this.colunaIdentificador)) {
            LOGGER.error("Defina a coluna identificador!");
            return false;
        }

        return true;
    }

    private Conflito criarEntidadeConflito(String valor) {
        return new Conflito(valor);
    }

    private List<String> gerarListaAtributos(String linha) {
        return Arrays.stream(linha.split("\t")).collect(Collectors.toList());
    }
}
