package br.ufms.cpcx.grasp.matrizconlfitos;

import br.ufms.cpcx.grasp.utils.txt.ExportarTxt;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class GeradorDeMatrizDeConflitos {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeradorDeMatrizDeConflitos.class);

    private String nomeArquivo;
    private final List<EntidadeConflito> entidadeConflitos;
    private Integer[][] matrizDeConflitos;

    public GeradorDeMatrizDeConflitos() {
        this.entidadeConflitos = new ArrayList<>();
    }

    public List<EntidadeConflito> getEntidadeConflitos() {
        return this.entidadeConflitos;
    }

    public void imprimirEntidadeConflitos() {
        this.entidadeConflitos.forEach(entidadeConflito -> LOGGER.trace(entidadeConflito.toString()));
        System.out.println();
    }

    public void lerEntidadeConflitos() {
        Scanner scanner = new Scanner(System.in);
        String linha = scanner.nextLine();

        int numeroDaEntidadeConflito = 0;
        while (!linha.equals("/exit")) {
            numeroDaEntidadeConflito++;
            adicionarEntidadeConflito(linha, numeroDaEntidadeConflito);
            linha = scanner.nextLine();
        }

    }

    public void lerEntidadeConflitosNoTxt(String nomeArquivo) {
        Scanner scanner;

        try {
            setNomeArquivo(nomeArquivo);
            scanner = new Scanner(new FileReader(nomeArquivo));
        } catch (FileNotFoundException err) {
            System.err.println("Erro: Leitura arquivo " + nomeArquivo);
            return;
        }

        int numeroDaEntidadeConflito = 0;
        while (scanner.hasNext()) {
            String linha = scanner.nextLine();
            numeroDaEntidadeConflito++;
            adicionarEntidadeConflito(linha, numeroDaEntidadeConflito);
        }
    }

    private void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo.replace(".txt", "")
                .replace("-lista-conflitos", "")
                + "-matriz-conflitos.txt";
    }

    public void gerarMatrizDeConflitos() {
        this.matrizDeConflitos = new Integer[this.entidadeConflitos.size()][this.entidadeConflitos.size()];

        for (int i = 0; i < this.entidadeConflitos.size(); i++) {
            EntidadeConflito entidadeConflito = this.entidadeConflitos.get(i);
            List<Integer> entidadesComConflitos = entidadeConflito.getListaDeEntidadesComConflitos();

            for (int j = 0; j < this.entidadeConflitos.size(); j++) {
                if (entidadesComConflitos.contains(j + 1)) {
                    this.matrizDeConflitos[i][j] = 1;
                } else {
                    this.matrizDeConflitos[i][j] = 0;
                }
            }
        }
    }

    public void imprimirMatrizDeConflitos() {
        int cont = 0;
        System.out.println("\t" + gerarListaDeTitutlos(this.entidadeConflitos));
        for (Integer[] linhaMatriz : this.matrizDeConflitos) {
            EntidadeConflito entidadeConflito = this.entidadeConflitos.get(cont++);
            Integer numero = entidadeConflito.getNumero();
            System.out.print(numero + "\t");
            System.out.println(formatarLinhaMatrizDeConflitos(Arrays.toString(linhaMatriz)));
        }
        System.out.println();
    }

    public void imprimirMatrizDeAdjacencia() {
        List<String> matrizDeConflitos = Arrays.stream(this.matrizDeConflitos)
                .map(linhaMatriz -> formatacaoBasicaNaLista(Arrays.toString(linhaMatriz)) + ",")
                .collect(Collectors.toList());

        matrizDeConflitos.forEach(System.out::println);

        exportarTxt(matrizDeConflitos);
    }

    private void exportarTxt(List<String> matrizDeConflitos) {
        try {
            if (isNull(this.nomeArquivo)) return;

            ExportarTxt exportarTxt = new ExportarTxt(this.nomeArquivo);

            for (String matrizDeConflito : matrizDeConflitos) {
                exportarTxt.escreverLinha(matrizDeConflito);
            }

            exportarTxt.fecharArquivo();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Erro ao exportar txt: ", e);
        }
    }

    private void adicionarEntidadeConflito(String linha, int numeroDaEntidadeConflito) {
        String[] conflitos = linha.split(",");

        if (temConflitos(conflitos)) {
            List<Integer> entidadesComConflitos = converterParaListadeInteiros(conflitos);
            this.entidadeConflitos.add(new EntidadeConflito(numeroDaEntidadeConflito, entidadesComConflitos));
        } else {
            this.entidadeConflitos.add(new EntidadeConflito(numeroDaEntidadeConflito));
        }
    }

    private static String gerarListaDeTitutlos(List<EntidadeConflito> entidadeConflitos) {
        return formatarListaParaImprimir(entidadeConflitos.stream().map(EntidadeConflito::getNumero).
                collect(Collectors.toList()).toString());
    }

    private static String formatacaoBasicaNaLista(String linhaMatriz) {
        return linhaMatriz.replace("[", "")
                .replace("]", "");
    }

    private static String formatarLinhaMatrizDeConflitos(String linhaMatriz) {
        return formatacaoBasicaNaLista(linhaMatriz.replace(",", "\t")
                .replace("1", "x").replace("0", " "));
    }

    private static String formatarListaParaImprimir(String lista) {
        return formatacaoBasicaNaLista(lista.replace(",", "")
                .replace(" ", "\t"));
    }

    private static List<Integer> converterParaListadeInteiros(String[] conflitos) {
        return Arrays.stream(conflitos).map(conflito -> Integer.parseInt(formatarParaConverterParaInteiro(conflito))).collect(Collectors.toList());
    }

    private static String formatarParaConverterParaInteiro(String conflito) {
        return conflito.replace(" ", "");
    }

    private static boolean temConflitos(String[] entidadesComConflitos) {
        return !(Arrays.toString(entidadesComConflitos).replace(" ", "")).equals("[]");
    }
}
