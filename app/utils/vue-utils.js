function gerarDataListOptions(item, atributo) {
    return {id: item.id, label: item[atributo]};
}