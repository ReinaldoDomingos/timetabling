function getErroFormatado(error) {
    if (error.response) {
        return limparStringDaLista(error.response.data.message);
    }
    return null;
}

function limparStringDaLista(texto) {
    return texto.replaceAll('[', '')
        .replaceAll(']', '')
        .replaceAll('\"', "");
}

function criarAlertaOptions() {
    return {tipo: 'ERRO', mensagemAlerta: null};
}