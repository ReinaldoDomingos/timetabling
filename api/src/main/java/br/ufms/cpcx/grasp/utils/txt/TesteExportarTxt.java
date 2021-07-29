package br.ufms.cpcx.grasp.utils.txt;

import java.io.IOException;

public class TesteExportarTxt {
    public static void main(String[] args) throws IOException {
        ExportarTxt exportarTxt = new ExportarTxt("./teste.txt");

        exportarTxt.escreverLinha("teste");

        exportarTxt.fecharArquivo();
    }
}
