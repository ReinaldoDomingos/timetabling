function mostrarErro(error) {
    if (error.response) {
        alert(limparStringDaLista(error.response.data.message));
    }
}

function limparStringDaLista(texto) {
    return texto.replaceAll('[', '')
        .replaceAll(']', '')
        .replaceAll('\"', "");
}