package br.ufms.cpcx.grasp.grasp;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

public class Grafo<T extends Comparable<T>, E extends Comparable<E>> {

    private List<Vertice<T>> listaDeVertices;
    private List<Aresta<T, E>> listaDeArestas;

    public Grafo() {
        listaDeVertices = new LinkedList<>();
        listaDeArestas = new LinkedList<>();
    }

    public Vertice<T> buscaVertice(T rotulo) {
        for (Vertice<T> vertice : listaDeVertices) {
            if (rotulo.compareTo(vertice.getRotulo()) == 0) {
                return vertice;
            }
        }
        return null;
    }

    public void adicionaVertice(Vertice<T> vertice) {
        listaDeVertices.add(vertice);
    }

    public void adicionarAresta(Vertice<T> origem, Vertice<T> destino, E info) {
        listaDeArestas.add(new Aresta<>(origem, destino, info));
    }

    public boolean isAresta(T origem, T destino) {
        return isAresta(this.buscaVertice(origem), this.buscaVertice(destino));
    }

    private boolean isAresta(Vertice<T> origem, Vertice<T> destino) {
        for (Aresta<T, E> aresta : listaDeArestas) {
            if (aresta.getOrigem().getRotulo().compareTo(origem.getRotulo()) == 0
                    && aresta.getDestino().getRotulo().compareTo(destino.getRotulo()) == 0) {
                return true;
            }
        }
        return false;
    }

    public Aresta<T, E> getAresta(T origem, T destino) {
        return getAresta(this.buscaVertice(origem), this.buscaVertice(destino));
    }

    private Aresta<T, E> getAresta(Vertice<T> origem, Vertice<T> destino) {
        for (Aresta<T, E> aresta : listaDeArestas) {
            if (aresta.getOrigem().getRotulo().compareTo(origem.getRotulo()) == 0
                    && aresta.getDestino().getRotulo().compareTo(destino.getRotulo()) == 0) {
                return aresta;
            }
        }
        return null;
    }

    public List<Vertice<T>> listaAdjacentes(T rotulo) {
        return listaAdjacentes(this.buscaVertice(rotulo));
    }

    private List<Vertice<T>> listaAdjacentes(Vertice<T> vertice) {
        List<Vertice<T>> vizinhosDoVertice = new LinkedList<>();

        for (Aresta<T, E> aresta : listaDeArestas) {
            if (aresta.getDestino().getRotulo().compareTo(vertice.getRotulo()) == 0 ||
                    aresta.getOrigem().getRotulo().compareTo(vertice.getRotulo()) == 0) {
                int i = 0;
                for (; i < vizinhosDoVertice.size(); i++) {
                    T rotuloDestino = aresta.getDestino().getRotulo();
                    T rotuloViziho = vizinhosDoVertice.get(i).getRotulo();
                    if (rotuloDestino.compareTo(rotuloViziho) != -1)
                        break;
                }
                if (aresta.getOrigem().equals(vertice))
                    vizinhosDoVertice.add(i, aresta.getDestino());
                else
                    vizinhosDoVertice.add(i, aresta.getOrigem());
            }
        }
        return vizinhosDoVertice;
    }

    public List<Aresta<T, E>> listaArestasDoVerticeResumida(Vertice<T> vertice) {
        List<Aresta<T, E>> arestasDoVertice = new LinkedList<>();

        for (Aresta<T, E> aresta : listaDeArestas) {
            if (aresta.getOrigem().getRotulo().compareTo(vertice.getRotulo()) == 0) {
                arestasDoVertice.add(aresta);
            }
        }
        return arestasDoVertice;
    }

    public List<Aresta<T, E>> listaArestasDoVertice(Vertice<T> vertice) {
        List<Aresta<T, E>> arestasDoVertice = new LinkedList<>();

        for (Aresta<T, E> aresta : listaDeArestas) {
            if (aresta.getOrigem().getRotulo().compareTo(vertice.getRotulo()) == 0
                    || aresta.getDestino().getRotulo().compareTo(vertice.getRotulo()) == 0) {
                arestasDoVertice.add(aresta);
            }
        }
        return arestasDoVertice;
    }

    public List<T> listaRotuloAdjacentesPorDestino(Vertice<T> vertice) {
        return this.listaAdjacentesPorDestino(vertice).stream().map(Vertice::getRotulo).collect(Collectors.toList());
    }

    public List<Vertice<T>> listaAdjacentesPorDestino(Vertice<T> vertice) {
        List<Vertice<T>> vizinhosDoVertice = new LinkedList<>();

        for (Aresta<T, E> aresta : listaDeArestas) {
            if (aresta.getDestino().getRotulo().compareTo(vertice.getRotulo()) == 0) {
                int i = 0;
                for (; i < vizinhosDoVertice.size(); i++) {
                    T rotuloDestino = aresta.getDestino().getRotulo();
                    T rotuloViziho = vizinhosDoVertice.get(i).getRotulo();
                    if (rotuloDestino.compareTo(rotuloViziho) != -1)
                        break;
                }
                vizinhosDoVertice.add(i, aresta.getDestino());
            }
        }
        return vizinhosDoVertice;
    }

    public List<Vertice<T>> getListaDeVertices() {
        return listaDeVertices;
    }

    public void setListaDeVertices(List<Vertice<T>> listaDeVertices) {
        this.listaDeVertices = listaDeVertices;
    }

    public List<Aresta<T, E>> getListaDeArestas() {
        return listaDeArestas;
    }

    public void setListaDeArestas(List<Aresta<T, E>> listaDeArestas) {
        this.listaDeArestas = listaDeArestas;
    }

    public int qtdComponentes() {
        return -1;
    }

    public void buscaEmProfundidade(int origem) {
        Stack<Vertice<T>> pilha;
        pilha = new Stack<>();
        boolean[] isDescobertos = new boolean[this.listaDeVertices.size()];

        Vertice<T> origemDaBusca = listaDeVertices.get(origem);
        pilha.push(origemDaBusca);
        int movimentos = 0;
        while (!pilha.isEmpty()) {
            Vertice<T> escolhido = pilha.pop();
            if (!isDescobertos[escolhido.getIndex()]) {
                movimentos++;
                isDescobertos[escolhido.getIndex()] = true;
                List<Vertice<T>> vizinhos = listaAdjacentes(escolhido);
                for (Vertice<T> vizinho : vizinhos) {
                    pilha.push(vizinho);
                }
            }
        }
        System.out.println((movimentos - 1) * 2);
    }

    public boolean buscaEmLargura(int origem, boolean[] isDescobertos) {
        Queue<Vertice<T>> fila;
        fila = new LinkedList<>();

        Vertice<T> origemDaBusca = listaDeVertices.get(origem);
        fila.add(origemDaBusca);
        isDescobertos[origemDaBusca.getIndex()] = true;

        while (!fila.isEmpty()) {
            Vertice<T> escolhido = fila.remove();
            List<Vertice<T>> vizinhos = listaAdjacentes(escolhido);

            for (Vertice<T> vizinho : vizinhos) {
                if (!isDescobertos[vizinho.getIndex()]) {
                    isDescobertos[vizinho.getIndex()] = true;
                    fila.add(vizinho);
                }
            }
        }

        boolean todosEncontrados = true;
        int i = origem;
        for (; i < isDescobertos.length; i++) {
            if (!isDescobertos[i]) {
                todosEncontrados = false;
                break;
            }
        }
        return todosEncontrados;
    }

    public boolean isConexo() {
        boolean[] isDescobertos = new boolean[this.listaDeVertices.size()];
        return buscaEmLargura(0, isDescobertos);
    }

    public int menorCaminhoDijkstra(T origem, T destino) {
        boolean[] isDescobertos = new boolean[listaDeVertices.size()];
        int[] distancias = new int[listaDeVertices.size()];

        Arrays.fill(distancias, 999999999);

        Queue<Vertice<T>> fila;
        fila = new LinkedList<>();
        fila.add(buscaVertice(origem));
        distancias[0] = 0;
        while (!fila.isEmpty()) {
            Vertice<T> selecionado = fila.remove();
            if (selecionado.getRotulo().compareTo(destino) == 0) {
                return distancias[selecionado.getIndex()];
            }
            if (!isDescobertos[selecionado.getIndex()]) {
                isDescobertos[selecionado.getIndex()] = true;
                List<Aresta<T, E>> listaArestasDoVerticeSelecionado = listaArestasDoVertice(selecionado);
                int menor = -1;
                for (Aresta<T, E> aresta : listaArestasDoVerticeSelecionado) {
                    boolean descoberto = isDescobertos[aresta.getDestino().getIndex()];
                    int novaDistancia = distancias[selecionado.getIndex()] + Integer.parseInt(aresta.getChave().toString());
                    if (!descoberto && aresta.getDestino() != selecionado
                            && novaDistancia < distancias[aresta.getDestino().getIndex()]) {
                        distancias[aresta.getDestino().getIndex()] = novaDistancia;
                    }
                }
                for (Aresta<T, E> aresta : listaArestasDoVerticeSelecionado) {
                    Vertice<T> destinoAresta = aresta.getDestino();
                    boolean descoberto = isDescobertos[destinoAresta.getIndex()];
                    if ((!descoberto && destinoAresta != selecionado)
                            && (destinoAresta.getRotulo() != origem && (menor == -1
                            || distancias[destinoAresta.getIndex()] < distancias[menor]))) {
                        menor = destinoAresta.getIndex();
                    }
                }
                if (menor != -1) {
                    fila.add(listaDeVertices.get(menor));
                }
            }

        }
        return -1;
    }

    public int ordenarTopologicamente() {
        Queue<Vertice<T>> listaDeVerticesGrauZero = new LinkedList<>();

        Vertice.zerarId();
        Grafo<Integer, Integer> grafoCopia = new Grafo<>();
        Aresta<Integer, Integer> aresta;
        Vertice<Integer> v;
        for (Vertice<T> listaDeVertex : listaDeVertices) {
            v = new Vertice<>(Integer.parseInt(String.valueOf(listaDeVertex.getRotulo())));
            grafoCopia.getListaDeVertices().add(v);
            if (listaAdjacentes(listaDeVertex).isEmpty())
                listaDeVerticesGrauZero.add(listaDeVertex);
        }

        for (Aresta<T, E> listaDeAresta : listaDeArestas) {
            Integer origem = Integer.parseInt(String.valueOf(listaDeAresta.getOrigem().getRotulo()));
            Integer destino = Integer.parseInt(String.valueOf(listaDeAresta.getDestino().getRotulo()));
            aresta = new Aresta<>(grafoCopia.buscaVertice(origem), grafoCopia.buscaVertice(destino));
            grafoCopia.getListaDeArestas().add(aresta);
        }

        if (listaDeVerticesGrauZero.isEmpty())
            return -1;

        int qtdMinutosAMais = 0;
        while (!listaDeVerticesGrauZero.isEmpty()) {
            Vertice<T> escolhido = listaDeVerticesGrauZero.remove();
            grafoCopia.removerArestas((Vertice<Integer>) escolhido);
            grafoCopia.removerVertice((Integer) escolhido.getRotulo());

            if (listaDeVerticesGrauZero.isEmpty())
                for (int i = 0; i < grafoCopia.listaDeVertices.size(); i++) {
                    if (grafoCopia.listaAdjacentesPorDestino(grafoCopia.listaDeVertices.get(i)).isEmpty()) {
                        listaDeVerticesGrauZero.add((Vertice<T>) grafoCopia.listaDeVertices.get(i));
                        qtdMinutosAMais++;
                    }
                }
        }
        return qtdMinutosAMais;
    }

    private void removerVertice(T rotulo) {
        listaDeVertices.removeIf(vertice -> vertice.getRotulo().compareTo(rotulo) == 0);
    }

    private void removerArestas(Vertice<T> vertice) {
        listaDeArestas.removeIf(aresta -> aresta.getOrigem().getRotulo().compareTo(vertice.getRotulo()) == 0
                || aresta.getDestino().getRotulo().compareTo(vertice.getRotulo()) == 0);
    }

    public List<Integer> getCores() {
        return this.listaDeVertices.stream().map(Vertice::getCor).distinct().collect(Collectors.toList());
    }
}
