package br.ufms.cpcx.grasp.grasp;

public class Vertice<T> {
    private Integer cor;
    private final T rotulo;
    private final int index;
    private static int id = 0;

    public Vertice(T rotulo) {
        this.rotulo = rotulo;
        this.index = Vertice.id++;
    }

    public T getRotulo() {
        return rotulo;
    }

    public int getIndex() {
        return index;
    }

    public Integer getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    @Override
    public String toString() {
        return String.valueOf(rotulo);
    }

    public static void zerarId() {
        Vertice.id = 0;
    }
}
