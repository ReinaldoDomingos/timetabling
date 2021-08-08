package br.ufms.cpcx.gradehoraria.filter;

import com.mysema.query.types.expr.BooleanExpression;

import java.util.Map;

import static java.util.Objects.isNull;

public class GenericFilter extends PaginacaoOrdenadaFilter {

    private BooleanExpression booleanExpression;

    public GenericFilter(Map<String, String> parametros) {
        super(parametros);
    }

    public BooleanExpression getBooleanExpression() {
        return booleanExpression;
    }

    public void setBooleanExpression(BooleanExpression booleanExpression) {
        if (isNull(this.booleanExpression)) {
            this.booleanExpression = booleanExpression;
        } else {
            this.booleanExpression = this.booleanExpression.and(booleanExpression);
        }
    }

    public static GenericFilter of(Map<String, String> parametros) {
        return new GenericFilter(parametros);
    }

}
