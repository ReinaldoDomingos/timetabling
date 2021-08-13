Vue.component('titulo', {
    props: ['titulo', 'objeto', 'visualizando'],
    template: `
     <h4 class="titulo-secundario">
        <span v-show="!visualizando && objeto && !objeto.id">Adicionar</span>
        <span v-show="!visualizando && objeto && objeto.id">Editar</span>
        <span v-show="visualizando">Visualizar</span> {{titulo}}
     </h4>`
});