function s2ab(s) {
    let buf = new ArrayBuffer(s.length);
    let view = new Uint8Array(buf);
    for (let i = 0; i !== s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
}

function exportarXls(nomeArquivo, dados) {
    let byteCharacters = atob(dados.relatorio);
    let ab = s2ab(byteCharacters);
    let TIPO_XLS = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
    let blob = new Blob([ab], {type: TIPO_XLS + ';'});

    let link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = nomeArquivo + '.xlsx';

    document.body.appendChild(link);

    link.click();

    document.body.removeChild(link);
}
