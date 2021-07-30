package br.ufms.cpcx.grasp.grasp.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MelhorSolucaoDTO<E> {
    private E solucao;

    private List<Integer> avaliacoesDias;

    private List<Integer> avaliacoesTurmas;
}
