package br.ufms.cpcx.grasp.restricoes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RestricaoUtils {
    public static List<Restricao> getListaRestricoes() {
        return Arrays.stream(ERestricao.values()).map(Restricao::new).collect(Collectors.toList());
    }
}
