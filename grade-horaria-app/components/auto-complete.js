Vue.component('auto-complete', {
    props: ['label', 'lista', 'value'],
    template: `
    <div>
      <label v-show="label">{{label}}</label>
      <input class="form-control" list="data" @change="selecionarOpcao" :value="getValor">
      <datalist id="data">
        <option v-for="opcao in lista">
          {{opcao.label}}
        </option>
      </datalist>
    </div>`,
    computed: {
        getValor() {
            return this.value ? this.value.label : '';
        }
    },
    methods: {
        selecionarOpcao(evento) {
            let valor = evento.currentTarget.value;
            if (valor) {
                console.log(valor)
                let selecao = this.lista.filter(opcao => opcao.label === valor);
                if (selecao.length === 1) {
                    this.selecionado = selecao[0];
                }
            } else {
                this.selecionado = undefined;
            }
            this.$emit('input', this.selecionado)
        }
    }

});

