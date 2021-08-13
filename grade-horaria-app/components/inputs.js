Vue.component('caixa-de-texto', {
    props: ['label', 'valor', 'campo', 'visualizando'],
    template: `
    <div class="col-6">
        <label class="form-label">{{label}}</label>
        <div class="has-validation">
            <input v-model="valor[campo]"  v-bind:disabled="visualizando"
                type="text" class="form-control"
                v-bind:placeholder="label ? 'Preencha o campo ' + label : ''" required>
            <div class="invalid-feedback">Campo {{label}} é obrigatório</div>
       </div>
    </div>`,
});

Vue.component('caixa-de-numero', {
    props: ['label', 'valor', 'campo', 'visualizando', 'minimo'],
    template: `
    <div class="col-6 caixa-de-numero">
        <label v-show="label" class="form-label">{{label}}</label>
        <div class="has-validation">
            <input v-model="valor[campo]"  v-bind:disabled="visualizando"
                type="number" class="form-control"
                v-bind:placeholder="label ? 'Preencha o campo ' + label : ''"
                 v-bind:min="minimo" required>
            <div class="invalid-feedback">Campo {{label}} é obrigatório</div>
       </div>
    </div>`
});