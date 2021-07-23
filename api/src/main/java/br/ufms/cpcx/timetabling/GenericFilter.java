package br.ufms.cpcx.timetabling;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Map;

import static java.util.Objects.isNull;

@Data
public class GenericFilter {
    private int page;
    private int size;
    private String sort;

    public GenericFilter(Map<String, String> parametros) {
        setPage(parametros);
        setSize(parametros);
        setSort(parametros);
    }

    private void setSort(Map<String, String> parametros) {
        this.sort = getParametro(parametros, "sort");
    }

    private void setPage(Map<String, String> parametros) {
        this.page = Integer.parseInt(getParametro(parametros, "page"));
    }

    private void setSize(Map<String, String> parametros) {
        this.size = Integer.parseInt(getParametro(parametros, "size"));
    }

    private String getParametro(Map<String, String> parametros, String chave) {
        return parametros.get(chave);
    }

    public static GenericFilter of(Map<String, String> parametros) {
        return new GenericFilter(parametros);
    }

    public PageRequest getPageRequest() {
        if (isNull(this.sort)) {
            return PageRequest.of(this.getPage(), this.getSize());
        }

        return PageRequest.of(this.getPage(), this.getSize(), Sort.by(this.sort));
    }
}
