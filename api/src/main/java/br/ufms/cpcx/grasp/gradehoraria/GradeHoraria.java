package br.ufms.cpcx.grasp.gradehoraria;

import br.ufms.cpcx.grasp.conflitos.Entidade;
import br.ufms.cpcx.grasp.grasp.Candidato;
import br.ufms.cpcx.grasp.grasp.Vertice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class GradeHoraria {
    private int qtdDias;
    private List<String> turmas;
    private final String colunaHorario;
    private final String colunaCargaHoraria;
    private List<List<HorarioAula>> horarioAulas;
    private List<List<String>> aulaEmSequenciaTurmas;
    private final List<Candidato<Vertice<Integer>, Entidade>> candidatos;

    private final Random random = new Random();

    public GradeHoraria(int qtdDias, List<Candidato<Vertice<Integer>, Entidade>> candidatos, String colunaHorario, String colunaCargaHoraria) {
        this.qtdDias = qtdDias;
        this.candidatos = candidatos;
        this.colunaHorario = colunaHorario;
        this.colunaCargaHoraria = colunaCargaHoraria;

        reinicializarListas();
    }

    public void reinicializarListas() {
        this.horarioAulas = new ArrayList<>();
        this.aulaEmSequenciaTurmas = new ArrayList<>();
        this.turmas = gerarListaDeTurmas();

        for (int turma = 0; turma < this.turmas.size() * 4; turma++) {
            this.horarioAulas.add(new ArrayList<>());
            this.aulaEmSequenciaTurmas.add(new ArrayList<>());
            for (int dia = 0; dia < this.qtdDias; dia++) {
                this.horarioAulas.get(turma).add(new HorarioAula());
            }
        }

        this.candidatos.forEach(this::converterCandidatoParaHorarioAula);
    }

    public void imprimirGradeHoraria() {
        this.imprimirGradeHoraria(new ArrayList<>(), new ArrayList<>());
    }

    public void imprimirGradeHoraria(List<Integer> avaliacoesTurmas, List<Integer> avaliacoesDias) {
        for (int i = 0; i < horarioAulas.size(); i++) {
            List<String> turma = gerarListaAulaDaTurma(i);
            for (String horarioAula : turma) {
                StringBuilder nomeDisciplina = new StringBuilder(horarioAula.length() < 4 ? horarioAula : horarioAula.substring(0, 4));

                while (nomeDisciplina.length() < 4) {
                    nomeDisciplina.append(" ");
                }

                System.out.print(nomeDisciplina + " | ");
            }
            if (!avaliacoesTurmas.isEmpty()) {
                System.out.println(avaliacoesTurmas.get(i));
            } else {
                System.out.println();
            }
        }

        if (!avaliacoesDias.isEmpty()) {
            System.out.print("  " + avaliacoesDias.get(0).toString() + " | ");
            for (int i = 1; i < avaliacoesDias.size(); i++) {
                System.out.print("  " + avaliacoesDias.get(i).toString() + "  | ");
            }
        }
        System.out.println();
    }

    public int getAvaliacaoTurmas() {
        return this.getAvaliacoesTurmas().stream().mapToInt(Integer::intValue).sum();
    }

    public void avaliarTurmas() {
        for (int i = 0; i < horarioAulas.size(); i++) {
            avaliarTurma(i);
        }
    }

    public List<Integer> getAvaliacoesTurmas() {
        List<Integer> avaliacoes = new ArrayList<>();

        for (int i = 0; i < this.horarioAulas.size(); i++) {
            avaliacoes.add(avaliarTurma(i));
        }

        return avaliacoes;
    }

    public void trocarDias() {
        int qtdTrocas = 1;

        trocarDiasAleatorio();

        for (int i = 0; i < this.aulaEmSequenciaTurmas.size(); i++) {
            if (!this.aulaEmSequenciaTurmas.get(i).isEmpty()) {
                String aulaNome = aulaEmSequenciaTurmas.get(i).get(0);
                List<HorarioAula> turma = this.horarioAulas.get(i);
                List<Integer> diasDaAula = turma.stream()
                        .filter(aula -> nonNull(aula.getEntidade()) && getNomeDisciplina(aula.getEntidade() - 1).equals(aulaNome))
                        .map(HorarioAula::getDia).collect(Collectors.toList());

                if (diasDaAula.size() > 1) {
                    List<Integer> dias = selecionarDiasParaTrocar(aulaNome, diasDaAula);

                    int avaliacaoAntes = getAvaliacaoTurmas();
                    qtdTrocas++;
                    deslocarDia(dias.get(0), dias.get(1));
                    System.out.printf("trocou %d por % d\n", dias.get(0), dias.get(1));

                    reinicializarListas();
                    if (avaliacaoAntes > getAvaliacaoTurmas()) {
                        qtdTrocas--;
                        deslocarDia(dias.get(0), dias.get(1));
                        System.out.printf("destrocou %d por % d\n", dias.get(0), dias.get(1));
                        reinicializarListas();
                        this.avaliarTurmas();
                    }
                }
            }
        }
        System.out.println(qtdTrocas + " trocas");
    }

    public void deslocarDia(Integer dia1, Integer dia2) {
        List<HorarioAula> aulasDia1 = getAulasDia(dia1);
        List<HorarioAula> aulasDia2 = getAulasDia(dia2);

        aulasDia1.forEach(aula -> this.candidatos.get(aula.getEntidade() - 1).getValor().setCor(dia2));
        aulasDia2.forEach(aula -> this.candidatos.get(aula.getEntidade() - 1).getValor().setCor(dia1));
    }

    public void deslocarAula(int idCandidato, int novoDia) {
        Candidato<Vertice<Integer>, Entidade> candidato = this.candidatos.get(idCandidato - 1);
        candidato.getValor().setCor(novoDia);
        this.qtdDias = Math.toIntExact(this.candidatos.stream().map(c -> c.getValor().getCor()).distinct().count());
//        reinicializarListas();
    }

    public int getQtdDias() {
        return this.qtdDias;
    }

    public void setQtdDias(int qtdDias) {
        this.qtdDias = qtdDias;
    }

    public List<HorarioAula> getTurma(int posTurma) {
        return this.horarioAulas.get(posTurma);
    }

    public List<HorarioAula> getDia(int posDia) {
        List<HorarioAula> dia = new ArrayList<>();

        for (int turma = 0; turma < this.horarioAulas.size(); turma++) {
            dia.add(new HorarioAula());
        }

        for (int turma = 0; turma < this.horarioAulas.size(); turma++) {
            dia.set(turma, this.horarioAulas.get(turma).get(posDia));
        }

        return dia;
    }

    public List<Integer> getCandidatosDia(Integer cor) {
        return this.getDia(cor - 1).stream().filter(aula -> nonNull(aula.getEntidade()))
                .map(HorarioAula::getEntidade).collect(Collectors.toList());
    }

    public List<List<String>> getAulaEmSequenciaTurmas() {
        this.getAvaliacoesTurmas();
        return aulaEmSequenciaTurmas;
    }

    public int avaliarTurma(int posTurma) {
        int avaliacao = 0;

        List<String> turma = gerarListaAulaDaTurma(posTurma);

        List<String> entidades = new ArrayList<>();

        this.aulaEmSequenciaTurmas.set(posTurma, new ArrayList<>());

        for (int i = 0; i < turma.size(); i++) {
            entidades.add("");
        }

        for (int i = 0; i < this.qtdDias; i++) {
            String aula = turma.get(i);
            int posExistente = aula.equals("") ? -1 : getPosicaoAulaExistente(entidades, aula);
            if (posExistente >= 0 && (posExistente == i + 1 || posExistente == i - 1)) {
                this.aulaEmSequenciaTurmas.get(posTurma).add(aula);
                avaliacao--;
            }

            entidades.set(i, aula);
        }

        return avaliacao;
    }

    private int getPosicaoAulaExistente(List<String> entidades, String aula) {
        String[] nomes = aula.split("\\|");
        if (nomes.length == 1)
            return entidades.indexOf(aula);
        else
            for (String nome : nomes) {
                if (entidades.contains(nome))
                    return entidades.indexOf(nome);
            }
        return -1;
    }

    private void trocarDiasAleatorio() {
        HashSet<Integer> diasSorteio = new HashSet<>();

        while (diasSorteio.size() < 2) {
            diasSorteio.add((random.nextInt(this.qtdDias - 1) + 1));
        }

        Iterator<Integer> iterator = diasSorteio.iterator();

        this.deslocarDia(iterator.next(), iterator.next());

        reinicializarListas();
        this.avaliarTurmas();
    }

    private List<Integer> selecionarDiasParaTrocar(String aulaNome, List<Integer> diasDaAula) {
        List<Integer> dias = new ArrayList<>();
        Integer dia1 = diasDaAula.get(0);
        Integer dia2 = diasDaAula.get(1);
        System.out.println(dia1 + " e " + dia2 + " tem " + aulaNome);

        if (dia1 + 1 < this.qtdDias && !dia2.equals(dia1 + 1)) {
            dias.add(dia1);
            dias.add(dia1 + 1);
        } else if (dia1 - 1 > 0 && !dia2.equals(dia1 - 1)) {
            dias.add(dia1);
            dias.add(dia1 - 1);
        } else if (dia2 + 1 < this.qtdDias && !dia1.equals(dia2 + 1)) {
            dias.add(dia2);
            dias.add(dia2 + 1);
        } else if (dia2 - 1 > 0 && !dia1.equals(dia2 - 1)) {
            dias.add(dia2);
            dias.add(dia2 - 1);
        }
        return dias;
    }

    private List<HorarioAula> getAulasDia(Integer posDia) {
        return this.getDia(posDia - 1).stream().filter(aula -> nonNull(aula.getEntidade())).collect(Collectors.toList());
    }

    private List<String> gerarListaDeTurmas() {
        return this.candidatos.stream().map(candidato -> candidato.getEntidade().get(this.colunaHorario)).distinct().collect(Collectors.toList());
    }

    private List<String> gerarListaAulaDaTurma(int i) {
        return this.getTurma(i).stream().map(aula -> {
            if (nonNull(aula.getEntidade())) {
                return getNomeDisciplina(aula.getEntidade() - 1);
            }
            return "";
        }).collect(Collectors.toList());
    }

    private void converterCandidatoParaHorarioAula(Candidato<Vertice<Integer>, Entidade> candidato) {
        int dia = candidato.getValor().getCor();
        Entidade entidade = candidato.getEntidade();
        String horario = entidade.get(this.colunaHorario);

        int posHorario = this.turmas.indexOf(horario) * 4;
        for (int i = 0; i < entidade.getCargaHoraria(); i++) {
            HorarioAula horarioAula = this.horarioAulas.get(posHorario++).get(dia - 1);
            horarioAula.setDia(dia);
            horarioAula.setHorario(horario);
            horarioAula.setEntidade(entidade.getId());
        }
    }

    private String getNomeDisciplina(int pos) {
        return this.candidatos.get(pos).getEntidade().getNome();
    }

    public List<List<HorarioAula>> getHorarioAulas() {
        List<List<HorarioAula>> horarioAulas = new ArrayList<>();
        for (int i = 0; i * 4 < this.horarioAulas.size(); i++) {
            horarioAulas.add(this.horarioAulas.get(i * 4));
        }
        return horarioAulas;
    }

    public List<Integer> getDiasComEspacoNoSemetre(String semestre) {
        return this.getDiasComEspacoNoSemetre(this.turmas.indexOf(semestre));
    }

    public List<Integer> getDiasComEspacoNoSemetre(int posSemestre) {
        List<HorarioAula> semestre = this.getTurma(posSemestre * 4);

        ArrayList<Integer> diasComEspacoNoSemetre = new ArrayList<>();
        for (int i = 0; i < semestre.size(); i++) {
            if (isNull(semestre.get(i).getEntidade())) {
                diasComEspacoNoSemetre.add(i + 1);
            }
        }
        return diasComEspacoNoSemetre;
    }

    public List<String> getSemetresComEspacoNoDia(int dia) {
        List<HorarioAula> horarioAulasDia = this.getDia(dia - 1);

        List<String> semestresComEspacoNoDia = new ArrayList<>();
        for (int turma = 0; turma < horarioAulasDia.size(); turma += 4) {
            if (isNull(horarioAulasDia.get(turma).getEntidade())) {
                semestresComEspacoNoDia.add(this.turmas.get(turma / 4));
            }
        }
        return semestresComEspacoNoDia;
    }

    public List<String> getResponsavelNoDia(int dia) {
        return this.getCandidatosDia(dia).stream()
                .map(candidato -> this.candidatos.get(candidato - 1).getEntidade().get("Professor"))
                .collect(Collectors.toList());
    }
}
