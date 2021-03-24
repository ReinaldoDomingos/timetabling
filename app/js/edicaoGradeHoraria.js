let URL_API = "http://localhost:8080/api";
let idGradeHoraria = location.search.replace('?', '').replace('idGradeHoraria=', '');

new Vue({
    el: '#app',
    data: {
        idGradeHoraria: idGradeHoraria,
        gradeHoraria: {},
        disciplinas: [],
        disciplinaSelecionada: null,
        disciplinasGradeHoraria: [],
        result: undefined,
        groupList: [
            {
                label: "something",
                id: 1
            },
            {
                label: "this too",
                id: 2
            },
            {
                label: "something different",
                id: 3
            }
        ]
    },
    mounted() {
        if (this.idGradeHoraria) {
            let self = this;
            axios.get(URL_API + '/gradeHoraria/' + this.idGradeHoraria).then(function (response) {
                self.gradeHoraria = response.data;
            });
            this.buscarDisciplinasGradeHoraria();
            axios.get(URL_API + '/disciplina').then(response => this.disciplinas = response.data.map(disciplina => gerarDataListOptions(disciplina, 'nome')));
        }
    },
    methods: {
        salvar() {
            let camposObrigatorios = [
                {atributo: 'ano', titulo: 'ano'},
                {atributo: 'semestreAno', titulo: 'semestre'}
            ];
            if (this.isValidoFormulario(camposObrigatorios)) {
                axios.post(URL_API + '/gradeHoraria', this.gradeHoraria).then(function (response) {
                    location.search = '?idGradeHoraria=' + response.data.id;
                }, function (err) {
                    alert('Erro: ao salvar grade horária: ' + err);
                });
            }
        },
        adicionarDisciplina() {
            if (this.disciplinaSelecionada && this.disciplinaSelecionada.id) {
                let self = this;
                let disciplinaSelecionada = {id: this.disciplinaSelecionada.id};
                axios.post(`${URL_API}/gradeHoraria/${this.idGradeHoraria}/adicionarDisciplina`, disciplinaSelecionada).then(function () {
                    self.buscarDisciplinasGradeHoraria();
                });
            }
        },
        buscarDisciplinasGradeHoraria: function () {
            axios.get(`${URL_API}/gradeHoraria/${this.idGradeHoraria}/disciplinas`).then(response => this.disciplinasGradeHoraria = response.data);
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
