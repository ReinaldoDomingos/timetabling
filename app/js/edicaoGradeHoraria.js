Vue.config.errorHandler = err => {
    console.log('Exception: ', err)
}

window.onerror = function (message, source, lineno, colno, error) {
    console.log('Exception: ', error)
}
let URL_API = "http://localhost:8080/api";

new Vue({
    el: '#app',
    data: {
        gradeHoraria: {},
        disciplinas: [],
        visualizando: false,
        filters: getFilters(),
        professores: [],
        disciplinaSelecionada: null,
        disciplinasGradeHoraria: [],
        semestresDS: constantes.ENUMS.SEMESTRE,
        colunasDisciplinasGradeHoraria: constantes.ESQUEMAS.colunasDisciplinasGradeHoraria,
        colunasSelecionaveis: []
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
        voltar() {
            location.href = 'index.html';
        },
        excluirDisciplinaGradeHoraria(disciplinaGradeHoraria) {
            deletarRegistro(URL_API + '/disciplinaGradeHoraria', disciplinaGradeHoraria.idDisciplinaGradeHoraria).finally(this.buscarDisciplinasGradeHoraria);
        },
        salvarDisciplinaGradeHoraria(disciplinaGradeHoraria) {
            return function () {
                salvarRegistro(URL_API + '/disciplinaGradeHoraria', disciplinaGradeHoraria, 'idDisciplinaGradeHoraria')
                    .then(function (reponse) {
                        console.log(reponse)
                    })
                    .catch(mostrarErro);
            }
        },
        buscarDisciplinas(page, size, sort) {
            this.disciplinas = [];
            buscarListagem(URL_API + '/disciplina', page ? page : 0, size ? size : 10, sort ? sort : 'nome')
                .then(response => this.disciplinas = response.data.content.map(disciplina => gerarDataListOptions(disciplina, 'nome')))
        },
        adicionarColunaSelecionavel: function () {
            this.colunasSelecionaveis.push(
                {
                    coluna: 'nome',
                    titulo: 'Professor',
                    chaveObjeto: 'professor',
                    lista: this.professores
                });
        }, buscarProfessores() {
            let self = this;
            buscarListagem(URL_API + '/professor/todos')
                .then(response => self.professores = response.data)
                .then(() => this.adicionarColunaSelecionavel(self));
        },
        salvarGradeHoraria() {
            let camposObrigatorios = [
                {atributo: 'ano', titulo: 'ano'},
                {atributo: 'semestreAno', titulo: 'semestre'}
            ];
            if (this.isValidoFormulario(camposObrigatorios)) {
                axios.post(URL_API + '/gradeHoraria', this.gradeHoraria).then(function (response) {
                    location.search = '?id=' + response.data.id;
                }, function (err) {
                    alert('Erro: ao salvar grade horária: ' + err);
                });
            }
        },
        adicionarDisciplina() {
            if (this.disciplinaSelecionada && this.disciplinaSelecionada.id) {
                let self = this;
                let disciplinaSelecionada = {id: this.disciplinaSelecionada.id};
                axios.post(`${URL_API}/gradeHoraria/${this.filters.id}/adicionarDisciplina`, disciplinaSelecionada).then(function () {
                    self.buscarDisciplinasGradeHoraria();
                });
            }
        },
        buscarDisciplinasGradeHoraria(page, size, sort) {
            buscarListagem(URL_API + '/gradeHoraria/' + this.filters.id + '/disciplinas', page ? page : 0, size ? size : 10)
                .then(response => this.disciplinasGradeHoraria = response.data);
        },
        isValidoFormulario(campos) {
            for (let indice in campos) {
                let atributo = campos[indice].atributo;
                if (!this.gradeHoraria[atributo]) {
                    alert('Preencha o campo ' + campos[indice].titulo + '.')
                    return false;
                }
            }

            return true;
        }
    }
});
