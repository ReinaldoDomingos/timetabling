package br.ufms.cpcx.grasp.gradehoraria;

public class AulaEmSequenciaDTO {
    private String aula;
    private Integer repeticoes;

    public AulaEmSequenciaDTO(String aula) {
        this.aula = aula;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public Integer getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(Integer repeticoes) {
        this.repeticoes = repeticoes;
    }

    public void adicionarRepeticao() {
        this.repeticoes++;
    }

    @Override
    public String toString() {
        return this.aula;
    }
}
