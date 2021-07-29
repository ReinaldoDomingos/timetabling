package br.ufms.cpcx.grasp.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    private StringUtils() {
    }

    public static String removerColchetes(String texto) {
        return texto.replace("[", "").replace("]", "");
    }

    public static String limparLista(List<String> texto) {
        return limparLista(texto.toString());
    }

    public static String limparLista(String texto) {
        return removerColchetes(texto.replace(", ", ","));
    }

    public static String getTextoDaLista(List<String> valores) {
        return limparLista(valores.stream().distinct().filter(valor -> !valor.isEmpty()).collect(Collectors.toList()).toString());
    }

    public static List<String> getListaDoTexto(String texto) {
        return Arrays.stream(texto.split(",")).collect(Collectors.toList());
    }
}
