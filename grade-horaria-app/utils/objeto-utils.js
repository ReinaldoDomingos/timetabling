function isNull(valor) {
    return valor === undefined || valor === null;
}

function nonNull(valor) {
    return !isNull(valor);
}

function isEmpty(lista) {
    return isNull(lista) || lista.length === 0;
}

function isNotEmpty(lista) {
    return !isEmpty(lista);
}

function getValorOuValorPadrao(valor, valorPadrao) {
    return nonNull(valor) ? valor : valorPadrao;
}
