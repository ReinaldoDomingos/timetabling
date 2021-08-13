Vue.component('caixa-de-selecao', {
    props: ['isMultiSelect', 'label', 'lista', 'campoCombo', 'onSelecionar', 'valor',
        'campo', 'chaveCombo', 'visualizando'],
    data() {
        return {
            idElemento: null,
            isSelectAberto: false,
            opcoesSelecionadas: [],
            opcaoSelecionada: null,
            chaveOpcaoSelecionada: null,
            textoOpcaoSelecionada: null
        };
    },
    template: `
<div>
    <div class="select" v-bind:id="'multi-select-' + _uid"
    v-bind:class="estiloClasseSelect()">
        <div class="form-label label">           
            <label v-show="label">{{label}}</label>
        </div>
        <div @click="abrirSelect">
            <div class="btn botao-select">
                <span class="texto" v-show="!isMultiSelect && descricao">
                    {{ descricao }}
                </span>
                <span class="texto" v-show="isMultiSelect">
                    {{opcoesSelecionadas.length ?
                    opcoesSelecionadas.length + ' itens selecionados' 
                    : 'Selecione uma ou mais opções'}}
                </span>
                <span class="texto" v-show="lista.length===0">
                    {{'Nenhum item existente'}}
                </span>
                <span class="icone-item-lista-select">
                    <i class="material-icons">expand_more</i>
                </span>                
            </div>            
        </div>
    </div>
    
    <div>
        <ul v-show="isSelectAberto" class="select lista-select">
            <li v-show="lista.length===0" @click="fecharSelect"
                class="item-lista-select">
                    Nenhum item existente
            </li>
            <li v-bind:class="estiloClasseOpcao(item)" @click="selecionarOpcao(item)" 
                v-for="item in lista" class="item-lista-select">
                    <span>{{item[campoCombo]}}</span>
            </li>
        </ul>            
    </div>

    <div v-show="isSelectAberto" @click="fecharSelect" class="select-backdrop"></div>
</div>`,
    computed: {
        descricao() {
            if (this.lista.length > 0 && this.chaveCombo !== 'this') {
                return this.textoOpcaoSelecionada ? this.textoOpcaoSelecionada : 'Selecione uma opção';
            } else if (this.lista.length > 0) {
                return this.opcaoSelecionada ? this.opcaoSelecionada[this.campoCombo] : 'Selecione uma opção';
            }

            return null;
        },
        estiloClasseSelect() {
            return function () {
                return {'disabled': this.visualizando};
            }
        },
        estiloClasseOpcao() {
            return function (item) {
                return {
                    'item-lista-select-selecionado': (!this.isMultiSelect && this.opcaoSelecionada === item)
                        || (this.isMultiSelect && this.opcoesSelecionadas.indexOf(item) !== -1)
                };
            }
        }
    },
    mounted() {
        this.inicializarOpcaoSelecionada();
    },
    methods: {
        inicializarOpcaoSelecionada() {
            this.opcaoSelecionada = {};

            if (this.valor && this.chaveCombo === 'this') {
                this.opcaoSelecionada = this.valor[this.campo];
            } else if (this.valor && this.valor[this.campo]) {
                let valorInicial = this.valor[this.campo];
                let opcoesFiltrada = this.getOpcoesFiltradaPorChave(valorInicial);
                this.opcaoSelecionada = opcoesFiltrada.length > 0 ? opcoesFiltrada[0] : this.opcaoSelecionada;
            }
        },
        getOpcoesFiltradaPorChave(chave) {
            return this.lista.filter((item) => item[this.chaveCombo] === chave);
        },
        selecionarOpcao(item) {
            if (this.isMultiSelect) {
                this.selecionarOpcaoMultiSelect(item);
            } else {
                this.opcaoSelecionada = item;
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
        abrirSelect() {
            if (this.visualizando) {
                return;
            }
            this.isSelectAberto = true;
        },
        fecharSelect() {
            this.isSelectAberto = false;
        }
    },
    watch: {
        valor() {
            this.inicializarOpcaoSelecionada();
        },
        opcaoSelecionada(opcaoSelecionada) {
            if (opcaoSelecionada) {
                this.chaveOpcaoSelecionada = opcaoSelecionada[this.chaveCombo];
                this.textoOpcaoSelecionada = opcaoSelecionada[this.campoCombo];

                let foiAlterada = false;
                if (this.chaveCombo === 'this' && this.valor[this.campo] !== opcaoSelecionada) {
                    foiAlterada = true;
                    this.valor[this.campo] = opcaoSelecionada;
                } else if (this.chaveCombo !== 'this' && this.valor[this.campo] !== this.chaveOpcaoSelecionada) {
                    foiAlterada = true;
                    this.valor[this.campo] = this.chaveOpcaoSelecionada;
                }

                if (this.onSelecionar && foiAlterada) {
                    this.onSelecionar();
                }


                this.fecharSelect();
            }
        }
    }
});