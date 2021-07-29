package br.ufms.cpcx.grasp.conflitos;

import static java.util.Arrays.asList;

public class TesteListaConflitos {
    public static void main(String[] args) throws Exception {
        GeradorListaDeConflitos geradorListaDeConflitos = new GeradorListaDeConflitos("Disciplina");

        geradorListaDeConflitos.setColunaIdentificador("Número");
        geradorListaDeConflitos.setColunaSemestre("Semestre");
        geradorListaDeConflitos.setColunaCargaHoraria("CHS");
//        geradorListaDeConflitos.setColunaCargaHorariaPratica("CHPS");
//        geradorListaDeConflitos.setColunaCargaHorariaTeorica("CHTS");
//        geradorListaDeConflitos.setColunaNumeroTurmaPratica("NTP");

//        geradorListaDeConflitos.lerRegistrosTabulados();
        geradorListaDeConflitos.lerRegistrosTabuladosNoXlS("alarr-api/resources/disciplinas-2021-2.xlsx", asList("Professor"));
//        geradorListaDeConflitos.lerRegistrosTabuladosNoXlS("alarr-api/resources/disciplinas-enfermagem-2021-1.xlsx");

        geradorListaDeConflitos.getEntidades().forEach(System.out::println);

        geradorListaDeConflitos.juntarColunas("Professor", "Professor Prática", "Professor Teórica");
        geradorListaDeConflitos.juntarColunas("Local", "Local Teórica", "Local Prática");

        try {
            geradorListaDeConflitos.adicionarRestricaoColuna("Semestre");
            geradorListaDeConflitos.adicionarRestricaoColuna("Professor");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        geradorListaDeConflitos.getEntidades().forEach(System.out::println);

        System.out.println("------------------------------------------------------------");

        geradorListaDeConflitos.imprimirListaDeConflitos();
        System.out.println(geradorListaDeConflitos.getEntidades().size() + " entidades");
    }
}
