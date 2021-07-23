Vue.component('caixa-de-texto', {
    props: ['label', 'valor', 'campo', 'visualizando'],
    template: `
    <div class="col-6">
        <label class="form-label">{{label}}</label>
        <div class="has-validation">
            <input v-model="valor[campo]"  v-bind:disabled="visualizando"
                type="text" class="form-control"
                v-bind:placeholder="label ? 'Digite o campo ' + label : ''" required>
            <div class="invalid-feedback">Campo {{label}} é obrigatório</div>
       </div>
    </div>`,
});

Vue.component('caixa-de-numero', {
    props: ['label', 'valor', 'campo', 'visualizando', 'maximo'],
    template: `
    <div class="col-6">
        <label class="form-label">{{label}}</label>
        <div class="has-validation">
            <input v-model="valor[campo]"  v-bind:disabled="visualizando"
                type="number" class="form-control"
                v-bind:placeholder="label ? 'Digite o campo ' + label : ''" required>
            <div class="invalid-feedback">Campo {{label}} é obrigatório</div>
       </div>
    </div>`,
});

Vue.component('caixa-de-selecao', {
    props: ['label', 'valor', 'campo', 'valores', 'chaveCombo', 'campoCombo', 'visualizando', 'onSelecionar'],
    template: `
    <div class="col-6 caixa-de-selecao">
        <label v-show="label" class="form-label">{{label}}</label>
        <select v-if="!onSelecionar" v-model="valor[campo]" class="form-control" v-bind:disabled="visualizando">
            <option v-for="v in valores" v-bind:value="v[chaveCombo]">
                {{v[campoCombo]}}
            </option>
        </select>
        <select v-if="onSelecionar"  @change="onSelecionar" v-model="valor[campo]" class="form-control" v-bind:disabled="visualizando">
            <option v-if="chaveCombo!=='this'" v-for="v in valores" v-bind:value="v[chaveCombo]">
                {{v[campoCombo]}}
            </option>
            <option v-if="chaveCombo==='this'" v-for="v in valores" v-bind:value="v">
                {{v[campoCombo]}}
            </option>
        </select>
    </div>`,
});