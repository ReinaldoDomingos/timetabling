package br.ufms.cpcx.grasp.utils.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorXlS {
    private LeitorXlS() {
    }

    public static List<String> getLinhas(String nomeArquivo) throws IOException {
        File arquivo = new File(nomeArquivo);

        FileInputStream fis = new FileInputStream(arquivo);

        XSSFWorkbook planilha = new XSSFWorkbook(fis);
        XSSFSheet pagina = planilha.getSheetAt(0);

        List<String> linhas = new ArrayList<>();

        StringBuilder linha;
        for (Row row : pagina) {
            linha = new StringBuilder();
            for (Cell coluna : row) {
                try {
                    linha.append(coluna.getStringCellValue()).append("\t");
                } catch (Exception e) {
                    linha.append(Double.valueOf(coluna.getNumericCellValue()).intValue()).append("\t");
                }
            }
            linhas.add(linha.toString());
        }

        return linhas;
    }
}