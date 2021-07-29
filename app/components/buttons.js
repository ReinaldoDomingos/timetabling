Vue.component('botao-voltar', {
    props: ['funcao'],
    template: `
    <div class="col-12 px-0 mb-5 mt-2">
        <a @click="funcao()" class="btn-voltar">
            <i class="material-icons">keyboard_backspace</i>Voltar
        </a>
    </div>`,
});

Vue.component('botao-cancelar', {
    props: ['funcao'],
    template: `
    <button type="button" class="btn btn-light botao-cancelar float-right mt-5"
     @click="funcao()">
        Cancelar
    </button>`,
});

Vue.component('botao-salvar', {
    props: ['funcao', 'titulo'],
    template: `
    <button type="button" class="btn btn-success botao-principal float-right mt-5"
     @click="funcao">
      {{titulo ? titulo : 'Salvar'}}
    </button>`,
});