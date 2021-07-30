package br.ufms.cpcx.grasp.grasp;

public class Aresta<T, E> {

    private final E chave;
    private final Vertice<T> origem;
    private final Vertice<T> destino;

    public Aresta(Vertice<T> origem, Vertice<T> destino) {
        this(origem, destino, null);
    }

    public Aresta(Vertice<T> origem, Vertice<T> destino, E chave) {
        this.origem = origem;
        this.destino = destino;
        this.chave = chave;
    }

    public E getChave() {
        return chave;
    }

    public Vertice<T> getOrigem() {
        return origem;
    }

    public Vertice<T> getDestino() {
        return destino;
    }


    @Override
    public String toString() {
        if (chave == null)
            return "{" + origem.getRotulo() + "," + destino.getRotulo() + '}';
        else
            return "{" + origem.getRotulo() + "," + destino.getRotulo() + "," + chave + '}';
    }
}
