let URL_API = "http://localhost:8080/api";
new Vue({
    el: '#app',
    data: {
        turmas: [],
        disciplinas: [],
        professores: [],
        gradesHorarias: [],
        abaSelecionada: 'disciplinas',
        abasMenu: constantes.ESQUEMAS.abasMenu,
        colunasTurmas: constantes.ESQUEMAS.colunasTurmas,
        colunasDisciplinas: constantes.ESQUEMAS.colunasDisciplinas,
        colunasProfessores: constantes.ESQUEMAS.colunasProfessores,
        colunasGradeHorarias: constantes.ESQUEMAS.colunasGradeHorarias,
        resultados: {
            carga: 0,
            ch_ufms: 3000,
            carga_horaria_optativa: 238
        }
    },
    computed: {
        estiloClasses() {
            return function (id) {
                return {
                    'show active': this.abaSelecionada === id
                };
            };
        }
    },
    methods: {
        selecionarAba(nomeLista) {
            this.turmas = [];
            this.professores = [];
            this.professores = [];
            this.gradesHorarias = [];
            this.abaSelecionada = nomeLista;

            switch (nomeLista) {
                case 'disciplinas':
                    this.buscarDisciplinas();
                    break;
                case 'professores':
                    this.buscarProfessores();
                    break;
                case 'turmas':
                    this.buscarTurmas();
                    break;
                case 'gradesHorarias':
                    this.buscarGradeHorarias();
                    break;
            }
        },
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
        acessarTurma(id, visualizar) {
            let link = 'edicaoTurma.html';
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
        excluirTurma(turma) {
            deletarRegistro(URL_API + '/turma', turma.id).finally(this.buscarTurmas);
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
            this.gradesHorarias = [];
            buscarListagem(URL_API + '/gradeHoraria', page ? page : 0, size ? size : 10, sort ? sort : 'ano,semestreAno')
                .then(response => this.processarGradeHorarias(response.data));
        },
        buscarTurmas(page, size, sort) {
            this.turmas = [];
            buscarListagem(URL_API + '/turma', page ? page : 0, size ? size : 10, sort ? sort : 'nome')
                .then(response => this.turmas = response.data);
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