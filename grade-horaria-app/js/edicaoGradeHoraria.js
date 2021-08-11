let URL_API = "http://localhost:8080/gradehoraria-api";

new Vue({
    el: '#app',
    data: {
        disciplinas: [],
        professores: [],
        gradeHoraria: {},
        visualizando: false,
        alertaOptions: criarAlertaOptions(),
        alertaDisciplinasOptions: {tipo: 'ERRO', mensagemAlerta: null},
        filters: getFilters(),
        disciplinaSelecionada: null,
        disciplinasGradeHoraria: [],
        semestresDS: constantes.ENUMS.SEMESTRE,
        modalOptions: {isModalAberto: false, titulo: 'Gerar Grade Horária'},
        restricoesCheckbox: [{titulo: 'Manter disciplina em dias alternados'}],
        colunasDisciplinasGradeHoraria: constantes.ESQUEMAS.colunasDisciplinasGradeHoraria,
        colunasSelecionaveis: [],
        isModalAberto: false
    },
    mounted() {
        if (this.filters.id) {
            let self = this;
            buscarRegistro(URL_API + '/gradeHoraria', this.filters.id).then(function (response) {
                self.gradeHoraria = response.data;
            });
            this.buscarDisciplinas();
            this.buscarProfessores();
            this.buscarDisciplinasGradeHoraria();
            this.visualizando = this.filters.acao === 'visualizar';
        }
    },
    methods: {
        abrirModalGerarGradeHoraria() {
            this.modalOptions.abrirModal();
        },
        voltar() {
            location.href = 'index.html';
        },
        excluirDisciplinaGradeHoraria(disciplinaGradeHoraria) {
            deletarRegistro(URL_API + '/disciplinaGradeHoraria', disciplinaGradeHoraria.idDisciplinaGradeHoraria)
                .finally(() => this.buscarDisciplinasGradeHoraria(this.disciplinasGradeHoraria.number, this.disciplinasGradeHoraria.size));
        },
        salvarDisciplinaGradeHoraria(disciplinaGradeHoraria) {
            let self = this;
            return function () {
                salvarRegistro(URL_API + '/disciplinaGradeHoraria', disciplinaGradeHoraria, 'idDisciplinaGradeHoraria')
                    .catch(response => {
                        self.alertaDisciplinasOptions.mensagemAlerta = getErroFormatado(response);
                    })
                    .finally(() => self.buscarDisciplinasGradeHoraria(self.disciplinasGradeHoraria.number, self.disciplinasGradeHoraria.size));
            }
        },
        buscarDisciplinas(page, size, sort) {
            this.disciplinas = [];
            buscarListagem(URL_API + '/disciplina/todas')
                .then(response => this.disciplinas = response.data.map(disciplina => gerarDataListOptions(disciplina, 'nome')))
        },
        adicionarColunaSelecionavel(colunaSelecionavel) {
            this.colunasSelecionaveis.push(colunaSelecionavel);
        },
        buscarProfessores() {
            let self = this;
            buscarListagem(URL_API + '/professor/todos')
                .then(response => self.professores = response.data)
                .then(() => self.adicionarColunaSelecionavel({
                    coluna: 'nome',
                    titulo: 'Professor',
                    chaveObjeto: 'professor',
                    lista: self.professores
                }))
                .finally(self.buscarTurmas);
        },
        buscarTurmas() {
            let self = this;
            buscarListagem(URL_API + '/turma/todas')
                .then(response => self.turmas = response.data)
                .then(() => self.adicionarColunaSelecionavel({
                    coluna: 'nome',
                    titulo: 'Turma',
                    chaveObjeto: 'turma',
                    lista: self.turmas
                }));
        },
        salvarGradeHoraria() {
            let camposObrigatorios = [
                {atributo: 'ano', titulo: 'ano'},
                {atributo: 'semestreAno', titulo: 'semestre'}
            ];
            let self = this;
            if (self.isValidoFormulario(camposObrigatorios)) {
                salvarRegistro(URL_API + '/gradeHoraria', this.gradeHoraria)
                    .then(function (response) {
                        if (!self.filters.id) {
                            location.search = '?id=' + response.data.id;
                        }
                    })
                    .catch(() => self.alertaOptions.mensagemAlerta = 'Falha ao salvar Grade Horária.');
            }
        },
        adicionarDisciplina() {
            if (this.disciplinaSelecionada && this.disciplinaSelecionada.id) {
                let self = this;
                let disciplinaSelecionada = {id: this.disciplinaSelecionada.id};
                axios.post(`${URL_API}/gradeHoraria/${this.filters.id}/adicionarDisciplina`, disciplinaSelecionada)
                    .then(() => self.buscarDisciplinasGradeHoraria(self.disciplinasGradeHoraria.number, self.disciplinasGradeHoraria.size))
                    .catch(response => self.alertaDisciplinasOptions.mensagemAlerta = getErroFormatado(response));
            }
        },
        buscarDisciplinasGradeHoraria(page, size, sort, direction) {
            buscarListagem(URL_API + '/gradeHoraria/' + this.filters.id + '/disciplinas', page ? page : 0, size ? size : 5)
                .then(response => this.disciplinasGradeHoraria = response.data);
        },
        buscarTodasDisciplinasGradeHoraria(page, size) {
            let self = this;
            self.modalOptions.abrirModal();
            buscarRegistro(URL_API + '/gradeHoraria/gradeHorariaCompleta', self.filters.id)
                .then(response => exportarXls('Grade Horária', response.data))
                .catch(response => self.alertaDisciplinasOptions.mensagemAlerta = getErroFormatado(response));
        },
        isValidoFormulario(campos) {
            for (let indice in campos) {
                let atributo = campos[indice].atributo;
                if (!this.gradeHoraria[atributo]) {
                    this.alertaOptions.mensagemAlerta = 'Preencha o campo ' + campos[indice].titulo + '.';
                    return false;
                }
            }

            return true;
        }
    }
});
