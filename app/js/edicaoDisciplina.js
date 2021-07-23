let URL_API = "http://localhost:8080/api";

new Vue({
    el: '#app',
    data: {
        id: null,
        disciplina: {},
        visualizando: false,
        filters: getFilters(),
    },
    mounted() {
        if (this.filters.id) {
            this.buscarDisciplina(this.filters.id)
            this.visualizando = this.filters.acao === 'visualizar';
        }
    },
    methods: {
        voltar() {
            location.href = 'index.html';
        },
        buscarDisciplina(id) {
            buscarRegistro(URL_API + '/disciplina', id)
                .then(response => this.disciplina = response.data);
        },
        salvarDisciplina() {
            let self = this;
            salvarRegistro(URL_API + '/disciplina', self.disciplina)
                .then(response => self.disciplina = response.data)
                .then(() => {
                    if (!self.filters.id) {
                        location.search += 'id=' + self.disciplina.id;
                    }
                });
        }
    }
});
