package br.ufms.cpcx.grasp.gradehoraria;

public class HorarioAula {
    private Integer entidade;
    private String horario;
    private Integer dia;

    public Integer getEntidade() {
        return entidade;
    }

    public void setEntidade(Integer entidade) {
        this.entidade = entidade;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "HorarioAula{" +
                "entidade=" + entidade +
                ", horario='" + horario + '\'' +
                ", dia=" + dia +
                '}';
    }
}
