Vue.component('grid', {
    props: ['lista', 'atributos', 'atributosSelecionaveis', 'funcao', 'funcaoEditar',
        'funcaoExcluir', 'funcaoSalvar', 'isModoVisualizar', 'escoderBototes'],
    template: `
    <div>
    <div class="caixa grid">
        <button v-show="funcaoEditar" @click="funcaoEditar()" class="btn btn-primary float-left" style="margin-bottom: 20px;">Adicionar</button>
        <div class="table-responsive-xl">
            <table class="table table-hover table-striped">
                <tr>
                    <th v-for="atributo in atributos">{{atributo.titulo}}</th>
                    <th v-for="atributo in atributosSelecionaveis">{{atributo.titulo}}</th>
                    <th  v-show="!escoderBototes && atributos.length && (funcaoEditar || funcaoExcluir || isModoVisualizar)">Ações</th>
                </tr>
                <tr v-for="item in lista.content">
                    <td v-for="atributo in atributos">
                        <span v-show="!atributo.editavel">{{item[atributo.coluna]}}</span>
                        <caixa-de-numero v-show="atributo.editavel" :valor="item" :campo="atributo.coluna"></caixa-de-numero>
                    </td>
                    <td v-for="atributo in atributosSelecionaveis">
                        <caixa-de-selecao :on-selecionar="funcaoSalvar(item)" :valor="item" 
                              v-bind:campo="atributo.chaveObjeto" :lista="atributo.lista" 
                              chave-combo="this" campo-combo="nome">
                        </caixa-de-selecao>
                    </td>
                    <td v-show="!escoderBototes && atributos.length && (funcaoEditar || funcaoExcluir || isModoVisualizar)">
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