Vue.component('caixa-de-selecao', {
    props: ['isMultiSelect', 'label', 'lista', 'campoCombo', 'onSelecionar', 'valor', 'campo', 'chaveCombo'],
    data() {
        return {
            isSelectAberto: false,
            opcaoSelecionada: null,
            opcoesSelecionadas: []
        };
    },
    template: `
    <div>
        <div class="select">
            <label v-show="label">{{label}}</label>
            <button @click="toggleSelect" class="btn botao-select item-lista-select">
                <span v-show="!isMultiSelect">{{opcaoSelecionada ? opcaoSelecionada[campoCombo] : 'Selecione uma opção'}}</span>
                <span v-show="isMultiSelect">{{opcoesSelecionadas.length ? opcoesSelecionadas.length + ' itens selecionados' : 'Selecione uma ou mais opções'}}</span>
                <span class="icone-item-lista-select">
                        <i class="material-icons">expand_more</i>
                </span>
            </button>
            <ul v-show="isSelectAberto" class="select lista-select">
                <li v-bind:class="estiloClasseItem(item)" @click="selecionarOpcao(item)" 
                          v-for="item in lista" class="item-lista-select">
                    <span>{{item[campoCombo]}}</span>
                </li>
            </ul>
        </div>
        <div v-show="isSelectAberto" @click="fecharSelect" class="select-backdrop"></div>
    </div>`,
    computed: {
        estiloClasseItem() {
            return function (item) {
                return {
                    'item-lista-select-selecionado': this.isMultiSelect && this.opcoesSelecionadas.indexOf(item) !== -1
                };
            }
        },
        tamanhoSelect() {
            return function () {
                return this.isSelectAberto ? this.lista.length : 1;
            }
        }
    },
    mounted() {
        this.opcaoSelecionada = this.valor[this.campo];
    },
    methods: {
        selecionarOpcao(item) {
            if (this.isMultiSelect) {
                this.selecionarOpcaoMultiSelect(item);
            } else {
                this.opcaoSelecionada = item;
                this.valor[this.campo] = item;
                this.fecharSelect();
                if (this.onSelecionar) {
                    this.onSelecionar(this.valor);
                }
            }
        },
        selecionarOpcaoMultiSelect(item) {
            if (this.opcoesSelecionadas.indexOf(item) === -1) {
                this.opcoesSelecionadas.push(item);
            } else {
                this.opcoesSelecionadas = this.opcoesSelecionadas
                    .filter(opcao => opcao !== item);
            }
        },
        toggleSelect() {
            this.isSelectAberto = true;
        },
        fecharSelect() {
            this.isSelectAberto = false;
        }
    }
});