package br.ufms.cpcx.grasp.utils.xls;

import br.ufms.cpcx.grasp.gradehoraria.EPeriodo;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExportarXLS<T> {
    private HSSFWorkbook planilha;
    private CellStyle estiloCelula;
    private CellStyle estiloTextoNormal;
    private CellStyle estiloTextoCabecalho;
    private CellStyle estiloNumero;
    private List<HSSFSheet> paginas;
    private HSSFSheet paginaAtual;
    private Map<String, Short> coresCedulas;
    private short contadorCor;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportarXLS.class);

    public ExportarXLS() {
        inicializarPlanilha();

        adicionarPagina();
    }

    public ExportarXLS(String nomePagina) {
        inicializarPlanilha();

        this.coresCedulas = new HashMap<>();

        adicionarPagina(nomePagina);
    }

    private void inicializarPlanilha() {
        this.planilha = new HSSFWorkbook();
        this.paginas = new ArrayList<>();

        HSSFFont negrito = planilha.createFont();
        negrito.setBold(true);

        this.estiloCelula = planilha.createCellStyle();
        this.estiloCelula.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        this.estiloCelula.setAlignment(HorizontalAlignment.CENTER);
        this.estiloCelula.setVerticalAlignment(VerticalAlignment.CENTER);

        this.estiloTextoCabecalho = planilha.createCellStyle();
        setEstilosPadrao(this.estiloTextoCabecalho);
        this.estiloTextoCabecalho.setFont(negrito);

        this.estiloTextoNormal = planilha.createCellStyle();
        setEstilosPadrao(this.estiloTextoNormal);
        this.estiloTextoNormal.setWrapText(true);
        this.estiloTextoNormal.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        HSSFDataFormat formatoNumerico = planilha.createDataFormat();
        this.estiloNumero = planilha.createCellStyle();
        setEstilosPadrao(this.estiloNumero);
        this.estiloNumero.setDataFormat(formatoNumerico.getFormat("#,## 0.00"));
    }

    private void setEstilosPadrao(CellStyle estilo) {
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        estilo.setBorderBottom(BorderStyle.MEDIUM);
        estilo.setBorderLeft(BorderStyle.MEDIUM);
        estilo.setBorderTop(BorderStyle.MEDIUM);
        estilo.setBorderRight(BorderStyle.MEDIUM);
        estilo.setBottomBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
        estilo.setTopBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
        estilo.setRightBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
        estilo.setLeftBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
    }

    public void adicionarPagina() {
        this.adicionarPagina("Pagina " + (this.paginas.isEmpty() ? 1 : this.paginas.size()));
    }

    public int adicionarPagina(String nomePagina) {
        HSSFSheet pagina = planilha.createSheet(nomePagina);
        this.paginas.add(pagina);

        pagina.setDefaultColumnWidth(20);
        pagina.setDefaultRowHeight((short) 400);

        this.paginas.add(pagina);

        return this.paginas.size() - 1;
    }

    public void setDados(List<ColunaXls> colunasCabecalho, List<T> colunasHorarios, Map<String, List<T>> valoresColunas, EPeriodo periodo) {
        setDados(0, colunasCabecalho, colunasHorarios, valoresColunas, periodo);
    }

    public void setDados(int numeroDaPagina, List<ColunaXls> colunasCabecalho, List<T> colunasHorarios, Map<String, List<T>> valoresColunas, EPeriodo periodo) {
        if (numeroDaPagina >= this.paginas.size()) {
            return;
        }

        this.paginaAtual = this.paginas.get(numeroDaPagina);

        AtomicInteger numeroLinha = new AtomicInteger(0);
        if (EPeriodo.UNICO.equals(periodo)) {
            adicionarCabecalho(numeroLinha, 0, 1, colunasCabecalho);
            adicionarLinhas(numeroLinha, 0, 1, colunasCabecalho, colunasHorarios, valoresColunas);
        } else if (EPeriodo.INTEGRAL.equals(periodo)) {
            adicionarCabecalho(numeroLinha, 0, 2, colunasCabecalho);
            adicionarLinhas(numeroLinha, 0, 2, colunasCabecalho, colunasHorarios, valoresColunas);
            adicionarCabecalho(numeroLinha, 1, 2, colunasCabecalho);
            adicionarLinhas(numeroLinha, 1, 2, colunasCabecalho, colunasHorarios, valoresColunas);
        }

//        System.out.println("------------cores------------");
//        this.coresCedulas.forEach((chave, valor) -> System.out.println("------------------" + chave + " -> " + valor));
//        System.out.println("------------cores------------");
    }

    private void adicionarCabecalho(AtomicInteger numeroLinha, int pos, int salto, List<ColunaXls> colunasCabecalho) {
        Row linha;
        int numeroColuna = 0;
        linha = this.paginaAtual.createRow(numeroLinha.getAndIncrement());

        Cell colunaSemestre = linha.createCell(numeroColuna++);
        colunaSemestre.setCellValue("Semestre");
        colunaSemestre.setCellStyle(this.estiloTextoCabecalho);

        Cell coluna;
        for (int i = pos; i < colunasCabecalho.size(); i += salto) {
            ColunaXls colunaCabecalho = colunasCabecalho.get(i);
            coluna = addColunaCabecalho(numeroColuna++, linha, colunaCabecalho);
            coluna.setCellValue(colunaCabecalho.getTitulo());
        }
    }

    private void adicionarLinhas(AtomicInteger numeroLinha, int pos, int salto, List<ColunaXls> colunasCabecalho, List<T> colunasHorarios, Map<String, List<T>> valoresColunas) {
        Cell coluna;
        Row linha;

        int numeroColuna;
        for (int i = 0; i < colunasHorarios.size(); i++) {
            numeroColuna = 0;
            linha = this.paginaAtual.createRow(numeroLinha.getAndIncrement());

            coluna = linha.createCell(numeroColuna++);
            coluna.setCellValue(colunasHorarios.get(i).toString());
            coluna.setCellStyle(this.estiloTextoNormal);

            for (int j = pos; j < colunasCabecalho.size(); j += salto) {
                ColunaXls colunaCabecalho = colunasCabecalho.get(j);
                coluna = addColuna(numeroColuna++, linha, colunaCabecalho);

                List<T> entidades = valoresColunas.get(colunaCabecalho.getTitulo());

                boolean possuiValor = i < entidades.size();
                coluna.setCellValue(possuiValor ? String.valueOf(entidades.get(i)) : "");

                if (possuiValor) {
                    String valor = String.valueOf(entidades.get(i));
                    coluna.setCellStyle(getEstiloTextoNormal(valor));
                } else {
                    coluna.setCellStyle(this.estiloTextoNormal);
                }
            }
        }
    }

    public void salvarXLS(String caminhoArquivo) {
        try {
            FileOutputStream out = new FileOutputStream(caminhoArquivo);
            planilha.write(out);
            out.close();
            planilha.close();
            LOGGER.debug("Success !!");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Erro" + e.getMessage());
        }

    }

    private Cell addColuna(int numeroColuna, Row linha, ColunaXls colunaCabecalho) {
        Cell coluna = linha.createCell(numeroColuna);

        setEstiloCelula(coluna, colunaCabecalho, false);

        return coluna;
    }

    private Cell addColunaCabecalho(int numeroColuna, Row linha, ColunaXls colunaCabecalho) {
        Cell coluna = linha.createCell(numeroColuna);

        setEstiloCelula(coluna, colunaCabecalho, true);

        return coluna;
    }

    private HSSFCellStyle getEstiloTextoNormal(String valor) {
        HSSFCellStyle estilo = planilha.createCellStyle();
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setEstilosPadrao(estilo);
        estilo.setWrapText(true);
//        if (isNull(this.coresCedulas.get(valor))) {
//            this.contadorCor += 1;
//            if (this.contadorCor == 8) this.contadorCor++;
//            this.coresCedulas.put(valor, !valor.isEmpty() ? (short) (this.contadorCor % 100) : IndexedColors.WHITE.index);
//        }
//        estilo.setFillForegroundColor(this.coresCedulas.get(valor));
        return estilo;
    }

    private void setEstiloCelula(Cell coluna, ColunaXls colunaCabecalho, boolean isCabecalho) {
        switch (colunaCabecalho.getTipoColuna()) {
            case NUMERICO:
                coluna.setCellStyle(estiloNumero);
                break;
            case TEXTO:
                if (isCabecalho) {
                    coluna.setCellStyle(estiloTextoCabecalho);
                } else {
                    coluna.setCellStyle(estiloTextoNormal);
                }
                break;
            default:
                coluna.setCellStyle(estiloCelula);
                break;
        }
    }
}