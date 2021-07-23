let URL_API = "http://localhost:8080/api";
new Vue({
    el: '#app',
    data: {
        disciplinas: [],
        professores: [],
        gradesHorarias: [],
        colunasDisciplinas: constantes.ESQUEMAS.colunasDisciplinas,
        colunasProfessores: constantes.ESQUEMAS.colunasProfessores,
        colunasGradeHorarias: constantes.ESQUEMAS.colunasGradeHorarias,
        resultados: {
            carga: 0,
            ch_ufms: 3000,
            carga_horaria_optativa: 238
        }
    },
    created() {
        this.buscarDisciplinas();
        this.buscarProfessores();
        this.buscarGradeHorarias();
    },
    methods: {
        acessarDisciplina(id, visualizar) {
            let link = 'edicaoDisciplina.html';
            if (id) {
                link += '?id=' + id;
            }

            if (visualizar) {
                link += '&acao=visualizar';
            }

            location.href = link
        },
        acessarProfessor(id, visualizar) {
            let link = 'edicaoProfessor.html';
            if (id) {
                link += '?id=' + id;
            }

            if (visualizar) {
                link += '&acao=visualizar';
            }

            location.href = link
        },
        acessarGradeHoraria(id, visualizar) {
            let link = 'edicaoGradeHoraria.html';
            if (id) {
                link += '?id=' + id;
            }

            if (visualizar) {
                link += '&acao=visualizar';
            }

            location.href = link
        },
        excluirDisciplina(disciplina) {
            deletarRegistro(URL_API + '/disciplina', disciplina.id).finally(this.buscarDisciplinas);
        },
        excluirProfessor(professor) {
            deletarRegistro(URL_API + '/professor', professor.id).finally(this.buscarProfessores);
        },
        excluirGradeHoraria(gradeHoraria) {
            deletarRegistro(URL_API + '/gradeHoraria', gradeHoraria.id).finally(this.buscarGradeHorarias);
        },
        buscarDisciplinas(page, size, sort) {
            this.disciplinas = [];
            buscarListagem(URL_API + '/disciplina', page ? page : 0, size ? size : 10, sort ? sort : 'nome')
                .then(response => this.processarDisciplinas(response.data));
        },
        buscarProfessores(page, size, sort) {
            this.professores = [];
            buscarListagem(URL_API + '/professor', page ? page : 0, size ? size : 10, sort ? sort : 'nome')
                .then(response => this.professores = response.data);
        },
        buscarGradeHorarias(page, size, sort) {
            this.professores = [];
            buscarListagem(URL_API + '/gradeHoraria', page ? page : 0, size ? size : 10, sort ? sort : 'ano')
                .then(response => this.processarGradeHorarias(response.data));
        },
        processarGradeHorarias(gradeHorarias) {
            this.gradesHorarias = gradeHorarias;
            this.gradesHorarias.content.forEach(gradeHoraria => this.setSemestreAnoGradeHoraria(gradeHoraria));
        },
        setSemestreAnoGradeHoraria(gradeHoraria) {
            gradeHoraria.semestreAno = filtroEnum(gradeHoraria.semestreAno, constantes.ENUMS.SEMESTRE)
        },
        processarDisciplinas(disciplinas) {
            this.disciplinas = disciplinas;

            this.resultados.quantidade = this.disciplinas.totalElements;

            let self = this;
            this.disciplinas.content.forEach(disciplina => {
                self.resultados.carga += disciplina.cargaHoraria;
            })

            self.resultados.porcentagem = parseFloat(self.resultados.carga) / parseFloat(self.resultados.ch_ufms) * 100;
        }
    },
    filters: {}
});