package br.ufms.cpcx.grasp.matrizconlfitos;

public class TesteMatrizConflitos {
    public static void main(String[] args) {
        GeradorDeMatrizDeConflitos geradorDeMatrizDeConflitos = new GeradorDeMatrizDeConflitos();

        String grade_sistemas_2021 = "alarr-api/resources/disciplinas-2021-1-lista-conflitos.txt";
//        String grade_enfermagem_2021_1 = "alarr-api/resources/disciplinas-enfermagem-2021-1-lista-conflitos.txt";

        geradorDeMatrizDeConflitos.lerEntidadeConflitosNoTxt(grade_sistemas_2021);

        System.out.println(geradorDeMatrizDeConflitos.getEntidadeConflitos().size() + " entidades");

        geradorDeMatrizDeConflitos.imprimirEntidadeConflitos();

        geradorDeMatrizDeConflitos.gerarMatrizDeConflitos();

        geradorDeMatrizDeConflitos.imprimirMatrizDeConflitos();

        geradorDeMatrizDeConflitos.imprimirMatrizDeAdjacencia();
    }
}
