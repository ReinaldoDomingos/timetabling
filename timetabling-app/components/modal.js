Vue.component('modal-selecionar-restricoes', {
    props: ['options', 'funcao', 'acoesCheckbox'],
    template: `
    <div v-show="options.isModalAberto">
        <div class="modal fade show modal-aberto" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">{{options.titulo}}</h5>
                        <button @click="fecharModal" type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                    <div v-for="acaoCheckbox in acoesCheckbox">
                        <label><input type="checkbox"/> {{acaoCheckbox.titulo}}</label>
                    </div>                     
                    </div>
                    <div class="modal-footer">
                        <botao-cancelar :funcao="fecharModal"></botao-cancelar>
                        <botao-salvar v-show="funcao" titulo="Gerar" :funcao="funcao"></botao-salvar>
                    </div>
                </div>
            </div>
        </div>
        <div @click="fecharModal" class="modal-backdrop fade show"></div>
    </div>`,
    computed: {
        estiloClasses() {
            return function () {

                console.log(this.options);
                return {
                    'modal-aberto': this.options.isModalAberto,
                    'show': this.options.isModalAberto
                }
            }
        }
    },
    mounted() {
        this.options.abrirModal = this.abrirModal;
        this.options.fecharModal = this.fecharModal;
    },
    methods: {
        abrirModal() {
            this.options.isModalAberto = true;
        },
        fecharModal() {
            this.options.isModalAberto = false;
        }
    }
});