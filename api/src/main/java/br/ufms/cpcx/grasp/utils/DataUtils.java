package br.ufms.cpcx.grasp.utils;

import java.util.List;

import static java.util.Arrays.asList;

public class DataUtils {
    private DataUtils() {
    }

    public static List<String> getDiasDaSemanaIntegral() {
        return asList("Segunda de manhã", "Segunda á tarde", "Terça-feira de manhã", "Terça-feira á tarde",
                "Quarta-feira de manhã", "Quarta-feira á tarde", "Quinta-feira de manhã", "Quinta-feira á tarde",
                "Sexta-feira de manhã", "Sexta-feira á tarde", "Sábado  de manhã", "Sábado á tarde",
                "Domingo  de manhã", "Domingo á tarde"
        );
    }

    public static List<String> getDiasDaSemana() {
        return asList("Segunda", "Terça-feira", "Quarta-feira", "Quinta-feira",
                "Sexta-feira", "Sábado", "Domingo"
        );
    }
}
