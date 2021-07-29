package br.ufms.cpcx.grasp.utils.xls;

import br.ufms.cpcx.grasp.gradehoraria.EPeriodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TesteXls {
    public static void main(String[] args) {
        ExportarXLS<String> exportarXLS = new ExportarXLS<>();

        List<ColunaXls> colunasCabecalho = new ArrayList<>();
        colunasCabecalho.add(new ColunaXls("Id", ETipoColunaXls.NUMERICO));

        HashMap<String, List<String>> colunas = new HashMap<>();

        ArrayList<String> valoresColuna = new ArrayList<>();
        valoresColuna.add(String.valueOf(1));

        colunas.put("Id", valoresColuna);

        exportarXLS.setDados(colunasCabecalho, Collections.singletonList(""), colunas, EPeriodo.UNICO);

        exportarXLS.salvarXLS("teste.xls");
    }
}