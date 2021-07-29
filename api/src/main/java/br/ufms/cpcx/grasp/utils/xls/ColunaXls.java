package br.ufms.cpcx.grasp.utils.xls;

public class ColunaXls {
    private String titulo;
    private ETipoColunaXls tipoColuna;

    public ColunaXls(String titulo, ETipoColunaXls tipoColuna) {
        this.titulo = titulo;
        this.tipoColuna = tipoColuna;
    }

    public String getTitulo() {
        return titulo;
    }

    public ETipoColunaXls getTipoColuna() {
        return tipoColuna;
    }

}
