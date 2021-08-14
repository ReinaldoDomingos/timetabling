Vue.component('grid', {
    props: ['lista', 'atributos', 'atributosSelecionaveis', 'funcao', 'funcaoEditar',
        'funcaoExcluir', 'funcaoSalvar', 'isModoVisualizar', 'escoderBototes'],
    template: `
    <div>
    <div class="caixa grid">
        <button v-show="funcaoEditar" @click="funcaoEditar()" class="btn btn-primary float-left" style="margin-bottom: 20px;">Adicionar</button>
        <div class="table-responsive-xl">
            <table class="table table-hover table-striped">
                <tr class="row">
                    <th v-if="atributos" v-bind:class="classesColuna(atributo)" v-for="(atributo, index) in atributos">{{atributo.titulo}}</th>
                    <th v-if="atributos" v-bind:class="classesColuna(atributo)" v-for="(atributo, index) in atributosSelecionaveis">{{atributo.titulo}}</th>
                    <th v-if="atributos" v-bind:class="classesColuna()"  
                    v-show="!escoderBototes && atributos.length && (funcaoEditar || funcaoExcluir || isModoVisualizar)"
                    class="acoes-grid text-center">
                        Ações
                    </th>
                </tr>
                <tr class="row" v-for="item in lista.content">
                    <td v-bind:class="classesColuna(atributo)" v-for="atributo in atributos">
                        <span v-show="!atributo.editavel">{{item[atributo.coluna]}}</span>
                        <caixa-de-numero v-show="atributo.editavel" :valor="item" :campo="atributo.coluna"></caixa-de-numero>
                    </td>
                    <td v-bind:class="classesColuna(atributo)" class="text-left" v-for="atributo in atributosSelecionaveis">
                        <caixa-de-selecao :on-selecionar="funcaoSalvar(item)" :valor="item" 
                              v-bind:campo="atributo.chaveObjeto" :lista="atributo.lista" 
                              chave-combo="this" campo-combo="nome">
                        </caixa-de-selecao>
                    </td>
                    <td v-bind:class="classesColuna()" v-show="!escoderBototes && atributos.length && (funcaoEditar || funcaoExcluir || isModoVisualizar)"
                    class="acoes-grid">
                        <a v-show="isModoVisualizar" class="btn" @click="funcaoEditar(item.id, true)">
                            <i class="material-icons">visibility</i>
                        </a>
                        <a v-show="funcaoEditar" class="btn" @click="funcaoEditar(item.id)">
                            <i class="material-icons">edit</i>
                        </a>
                        <a v-show="funcaoSalvar" class="btn" @click="funcaoSalvar(item)()">
                            <i class="material-icons">check</i>
                        </a>
                        <a v-show="funcaoExcluir" class="btn" @click="funcaoExcluir(item)">
                            <i class="material-icons">clear</i>
                        </a>
                    </td>
                </tr>
            </table>
            <nav v-show="lista.size" class="paginacao">
                <ul class="pagination">
                    <li @click="paginaAnterior()" v-bind:class="{disabled:lista.number===0}" class="page-item">
                        <span class="page-link">Anterior</span>
                    </li>
                    <li @click="irPagina(pagina)" class="page-item" v-for="pagina in lista.totalPages"
                        v-bind:class="{active: pagina === lista.number+1}">
                        <span class="page-link">{{pagina}}</span>
                    </li>
                    <li @click="proximaPagina()" v-bind:class="{disabled:lista.totalPages===0 || lista.number+1 === lista.totalPages}"
                        class="page-item">
                        <span class="page-link">Próximo</span>
                    </li>
                </ul>
                <span class="contagem">
                    Exibindo {{lista.number * lista.size + 1}} - {{(lista.number * lista.size) + lista.numberOfElements}} de {{lista.totalElements}}
                </span>
            </nav>
        </div>
    </div>
</div>
`,
    methods: {
        classesColuna(atributo, valorPadrao) {
            let classes = {};
            valorPadrao = atributo ? valorPadrao : 'col-1';
            atributo = getValorOuValorPadrao(atributo, {});
            console.log(atributo.classes)
            valorPadrao = getValorOuValorPadrao(valorPadrao, 'col-2');
            console.log(valorPadrao);
            let valores = getValorOuValorPadrao(atributo.classes, [valorPadrao]);
            console.log(valores);
            valores.forEach((valor) => classes[valor] = true);
            classes = isNotEmpty(valores) ? classes : {'col': true};
            console.log(classes);
            console.log('')
            return classes;
        },
        paginaAnterior() {
            if (this.lista.number > 0) {
                this.funcao(this.lista.number - 1);
            }
        },
        proximaPagina() {
            if (this.lista.number + 1 < this.lista.totalPages) {
                this.funcao(this.lista.number + 1);
            }
        },
        irPagina(pagina) {
            this.funcao(pagina - 1);
        }
    }
});