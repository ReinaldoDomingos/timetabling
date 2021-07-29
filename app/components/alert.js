Vue.component('caixa-de-alerta', {
    props: ['titulo', 'alertaOptions'],
    template: `
    <div v-show="alertaOptions && alertaOptions.mensagemAlerta" 
    v-bind:class="estiloClasses" class="alert alert-dismissible fade show" role="alert">
        <strong>{{getTitulo}}!</strong> {{alertaOptions.mensagemAlerta}}
        <span @click="fecharAlert" class="float-right">
            <i class="material-icons" >close</i>
        </span>
    </div>`,
    computed: {
        estiloClasses() {
            return {
                'alert-warning': this.alertaOptions.tipo === 'AVISO',
                'alert-danger': this.alertaOptions.tipo === 'ERRO',
                'alert-success': this.alertaOptions.tipo === 'SUCESSO'
            };
        },
        getTitulo() {
            if (this.titulo) {
                return this.titulo;
            } else if (this.alertaOptions.tipo) {
                return filtroEnum(this.alertaOptions.tipo, constantes.ENUMS.TIPO_ALERTA);
            }
        }
    },
    methods: {
        fecharAlert() {
            this.alertaOptions.mensagemAlerta = '';
        }
    }
});