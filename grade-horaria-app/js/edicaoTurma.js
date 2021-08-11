let URL_API = "http://localhost:8080/gradehoraria-api";

new Vue({
    el: '#app',
    data: {
        id: null,
        turma: {},
        visualizando: false,
        filters: getFilters(),
        alertaOptions: criarAlertaOptions()
    },
    mounted() {
        if (this.filters.id) {
            this.buscarTurma(this.filters.id)
            this.visualizando = this.filters.acao === 'visualizar';
        }
    },
    methods: {
        voltar() {
            location.href = 'index.html';
        },
        buscarTurma(id) {
            buscarRegistro(URL_API + '/turma', id)
                .then(response => this.turma = response.data);
        },
        salvarTurma() {
            let self = this;
            salvarRegistro(URL_API + '/turma', self.turma)
                .then(response => self.turma = response.data)
                .then(() => {
                    if (!self.filters.id) {
                        location.search += 'id=' + self.turma.id;
                    }
                })
                .catch(response => self.alertaOptions.mensagemAlerta = getErroFormatado(response));
        }
    }
});
