package br.ufms.cpcx.gradehoraria.filter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class PaginacaoOrdenadaFilter {
    private int page;
    private int size;
    private Sort sort;

    public PaginacaoOrdenadaFilter(Map<String, String> parametros) {
        setPage(parametros);
        setSize(parametros);
        setSort(parametros);
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    private void setSort(Map<String, String> parametros) {
        String sortStr = getParametro(parametros, "sort");
        String direcaoStr = getParametro(parametros, "direcaoStr");
        if (nonNull(sortStr)) {
            List<Order> ordens = new ArrayList<>();
            for (String atributo : sortStr.split(",")) {
                ordens.add(criarSortOrdem(atributo, direcaoStr));
            }
            this.sort = Sort.by(ordens);
        }
    }

    private void setPage(Map<String, String> parametros) {
        String page = getParametro(parametros, "page");
        if (nonNull(page)) {
            this.page = Integer.parseInt(page);
        }
    }

    private void setSize(Map<String, String> parametros) {
        String size = getParametro(parametros, "size");
        if (nonNull(size)) {
            this.size = Integer.parseInt(size);
        }
    }

    private String getParametro(Map<String, String> parametros, String chave) {
        return parametros.get(chave);
    }

    public PageRequest getPageRequest() {
        if (isNull(this.sort)) {
            return PageRequest.of(this.getPage(), this.getSize());
        }

        return PageRequest.of(this.getPage(), this.getSize(), this.sort);
    }

    private static Order criarSortOrdem(String atributoSort, String direcaoSort) {
        Direction direction = getSortDirecaoDeString(direcaoSort);
        return new Order(direction, atributoSort);
    }

    private static Direction getSortDirecaoDeString(String directionStr) {
        return Objects.isNull(directionStr) ? Direction.ASC : Direction.fromString(directionStr);
    }
}
