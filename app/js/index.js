let URL_API = "http://localhost:8080/api";

var app = new Vue({
    el: '#app',
    data: {
        disciplinas: [],
        professores: [],
        gradesHorarias: [],
        resultados: {
            carga: 0,
            ch_ufms: 3000,
            carga_horaria_optativa: 238
        }
    }, created() {
        let self = this;
        axios.get(URL_API + '/disciplina').then(response => this.processarDisciplinas(response.data));
        axios.get(URL_API + '/professor').then(response => this.professores = response.data);
        axios.get(URL_API + '/gradeHoraria').then(response => this.gradesHorarias = response.data);
    },
    methods: {
        processarDisciplinas(disciplinas) {
            this.disciplinas = disciplinas;

            this.resultados.quantidade = this.disciplinas.length;

            let self = this;
            this.disciplinas.forEach(disciplina => {
                self.resultados.carga += disciplina.cargaHoraria;
            })

            self.resultados.porcentagem = parseFloat(self.resultados.carga) / parseFloat(self.resultados.ch_ufms) * 100;
        }
    },
    filters: {
        filtroSemestre(enumSemestre) {
            let semestres = [
                {chave: 'PRIMEIRO_SEMESTRE', valor: 1},
                {chave: 'SEGUNDO_SEMESTRE', valor: 2}
            ];

            return enumSemestre ? semestres.filter(semestre=> semestre.chave === enumSemestre)[0].valor : '';
        }
    }
})