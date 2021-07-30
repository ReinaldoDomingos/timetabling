package br.ufms.cpcx.timetabling.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericException extends RuntimeException {
    private final String[] parametros;

    public GenericException(String message, Throwable cause, String... parameters) {
        super(message, cause);
        this.parametros = parameters;
    }

    public GenericException(String... parameters) {
        this.parametros = parameters;
    }

    public String[] getParametros() {
        return this.parametros;
    }

    public String getMessage() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            if (this.parametros != null && this.parametros.length > 0) {
                return mapper.writeValueAsString(this.parametros);
            }
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
        }

        return super.getMessage();
    }
}
