package br.ufms.cpcx.grasp.grasp.exception;

public class NenhumaRestricaoFracaAtivaException extends Exception {
    public NenhumaRestricaoFracaAtivaException() {
        super("Adicione ao menos uma restrição fraca pra avaliação das soluções.");
    }
}
