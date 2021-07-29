package br.ufms.cpcx.grasp.gradehoraria;

public class PeriodoUltils {
    private PeriodoUltils() {
    }

    public static boolean ehPeriodoUnico(EPeriodo periodo) {
        return EPeriodo.UNICO.equals(periodo);
    }
}
