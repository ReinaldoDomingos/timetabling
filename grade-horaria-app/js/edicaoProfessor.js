let URL_API = "http://localhost:8080/api";

new Vue({
    el: '#app',
    data: {
        id: null,
        professor: {},
        visualizando: false,
        filters: getFilters(),
    },
    mounted() {
        if (this.filters.id) {
            this.buscarProfessor(this.filters.id)
            this.visualizando = this.filters.acao === 'visualizar';
        }
    },
    methods: {
        voltar() {
            location.href = 'index.html';
        },
        buscarProfessor(id) {
            buscarRegistro(URL_API + '/professor', id)
                .then(response => this.professor = response.data);
        },
        salvarProfessor() {
            let self = this;
            salvarRegistro(URL_API + '/professor', self.professor)
                .then(response => self.professor = response.data)
                .then(() => {
                    if (!self.filters.id) {
                        location.search += 'id=' + self.professor.id;
                    }
                });
        }
    }
});
