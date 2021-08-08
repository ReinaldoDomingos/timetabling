Vue.component('vue-data-list', {
    props: ['label', 'options', 'value'],
    template: `
    <div>
      <label v-show="label" for="myinput">{{label}}</label>
      <input id="myinput" class="form-control" list="data" @change="change" :value="getValue">
      <datalist id="data">
        <option v-for="option in options">
          {{option.label}}
        </option>
      </datalist>
    </div>`,
    computed: {
        getValue() {
            return this.value ? this.value.label : '';
        }
    },
    methods: {
        change(e) {
            let value = e.currentTarget.value;
            if (value) {
                let sel = this.options.filter(c => c.label === value);
                if (sel.length === 1) {
                    this.selection = sel[0];
                } else {
                    this.selection = {
                        label: value,
                        isNew: true
                    }
                }
            } else {
                this.selection = undefined;
            }
            this.$emit('input', this.selection)
        }
    }

});

