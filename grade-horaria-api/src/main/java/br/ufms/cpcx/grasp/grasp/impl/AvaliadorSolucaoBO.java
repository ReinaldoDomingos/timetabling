package br.ufms.cpcx.grasp.grasp.impl;

import br.ufms.cpcx.grasp.grasp.Grafo;

import java.util.List;

public class AvaliadorSolucaoBO {
    private AvaliadorSolucaoBO() {
    }

    public static boolean possuiMenosCores(Grafo<Integer, Integer> novaSolucao, MelhorSolucaoDTO<Grafo<Integer, Integer>> melhorSolucaoAtual) {
        return novaSolucao.getCores().size() < melhorSolucaoAtual.getSolucao().getCores().size();
    }

    public static boolean possuiIgualOuMenosOuCores(Grafo<Integer, Integer> novaSolucao, MelhorSolucaoDTO<Grafo<Integer, Integer>> melhorSolucaoAtual) {
        return novaSolucao.getCores().size() <= melhorSolucaoAtual.getSolucao().getCores().size();
    }

    public static boolean possuiMelhorAvalicaoQueAtual(MelhorSolucaoDTO<Grafo<Integer, Integer>> melhorSolucaoAtual, List<Integer> avaliacoesDiasSolucaoAtual, List<Integer> avaliacoesTurmasSolucaoAtual) {
        long qtdAvaliacoesNegativasTurmasSolucaoAtual = getQtdAvaliacoesNegativas(avaliacoesTurmasSolucaoAtual);
        long qtdAvaliacoesNegativasDiasSolucaoAtual = getQtdAvaliacoesNegativas(avaliacoesDiasSolucaoAtual);

        long qtdAvaliacoesNegativasTurmasMelhorSolucao = getQtdAvaliacoesNegativas(melhorSolucaoAtual.getAvaliacoesTurmas());
        long qtdAvaliacoesNegativasDiasMelhorSolucao = getQtdAvaliacoesNegativas(melhorSolucaoAtual.getAvaliacoesDias());

        return qtdAvaliacoesNegativasDiasSolucaoAtual == 0
                || (qtdAvaliacoesNegativasDiasSolucaoAtual < qtdAvaliacoesNegativasDiasMelhorSolucao
//                || (qtdAvaliacoesNegativasDiasSolucaoAtual <= qtdAvaliacoesNegativasDiasMelhorSolucao
                && qtdAvaliacoesNegativasTurmasSolucaoAtual < qtdAvaliacoesNegativasTurmasMelhorSolucao);
    }

    public static long getQtdAvaliacoesNegativas(List<Integer> avaliacoes) {
        return avaliacoes.stream().filter(avaliacao -> avaliacao < 0).count();
    }
}
