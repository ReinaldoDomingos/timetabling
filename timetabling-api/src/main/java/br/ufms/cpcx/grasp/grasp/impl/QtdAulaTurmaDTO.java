package br.ufms.cpcx.grasp.grasp.impl;

public class QtdAulaTurmaDTO {
    private final Integer id;
    private final String horario;
    private final Integer quantidade;
    private final Integer quantidadeNaoPrencida;
    private final Integer quantidadePrencidaPodeMover;

    public QtdAulaTurmaDTO(Integer id, String horario, Integer quantidade, Integer quantidadeNaoPrencida, Integer quantidadePrencidaPodeMover) {
        this.id = id;
        this.horario = horario;
        this.quantidade = quantidade;
        this.quantidadeNaoPrencida = quantidadeNaoPrencida;
        this.quantidadePrencidaPodeMover = quantidadePrencidaPodeMover;
    }

    public Integer getId() {
        return id;
    }

    public String getHorario() {
        return horario;
    }

    public Integer getQuantidadeNaoPrencida() {
        return quantidadeNaoPrencida;
    }

    public Integer getQuantidadePrencidaPodeMover() {
        return quantidadePrencidaPodeMover;
    }

    @Override
    public String toString() {
        return horario + " = " + quantidade + " -> " + (quantidade - quantidadeNaoPrencida) + " ocupados, "
                + quantidadeNaoPrencida + " sobrando e pode mover " + quantidadePrencidaPodeMover;
    }
}
