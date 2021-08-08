Vue.component('barra-menu', {
    props: ['abas', 'selecionarAba', 'abaSelecionada'],
    template: `
        <nav class="barra-menu">
            <div class="nav nav-tabs justify-content-center" id="nav-tab" role="tablist">
                <ul>
                    <li  v-for="aba in abas">
                        <a v-bind:id="'nav-' + aba.id + '-tab'" 
                           v-bind:class="estiloClasses(aba.id)"
                           @click="selecionarAba(aba.id)" class="nav-link" 
                           data-toggle="tab" v-bind:href="'#nav-' + aba.id" role="tab"
                           aria-controls="nav-home" aria-selected="true">{{aba.titulo}}</a>
                    </li>
                </ul>
            </div>
        </nav>`,
    computed: {
        estiloClasses() {
            return function (id) {
                return {
                    'active': this.abaSelecionada === id
                };
            };
        }
    },
    mounted() {
        if (this.abas && this.abas.length) {
            this.selecionarAba(this.abas[0].id);
        }
    }
});