package br.ufms.cpcx.grasp.utils.txt;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ExportarTxt {
    private final OutputStreamWriter out;
    private final BufferedWriter writer;

    public ExportarTxt(String nomeArquivo) throws IOException {
        OutputStream arquivo = new FileOutputStream(nomeArquivo);
        this.out = new OutputStreamWriter(arquivo);
        this.writer = new BufferedWriter(out);
    }

    public void escreverLinha(String linha) throws IOException {
        this.writer.write(linha + "\n");
    }

    public void fecharArquivo() throws IOException {
        this.writer.close();
        this.out.close();
    }
}
