var constantes = {
    ENUMS: {
        SEMESTRE: [
            {chave: 'PRIMEIRO_SEMESTRE', valor: '1° Semestre'},
            {chave: 'SEGUNDO_SEMESTRE', valor: '2° Semestre'}
        ],
        TIPO_ALERTA: [
            {chave: 'ERRO', valor: 'Erro'},
            {chave: 'AVISO', valor: 'Aviso'},
            {chave: 'SUCESSO', valor: 'Sucesso'}
        ]
    },
    ESQUEMAS: {
        colunasDisciplinas: [
            {titulo: 'Código', coluna: 'codigo'},
            {titulo: 'Nome', coluna: 'nome'},
            {titulo: 'C.H', coluna: 'cargaHoraria'}
        ],
        colunasDisciplinasGradeHoraria: [
            {titulo: 'Nome', coluna: 'nome'},
            {titulo: 'C.H', coluna: 'cargaHoraria'},
            {titulo: 'C.H.S', coluna: 'cargaHorariaSemanal', editavel: true}
        ],
        colunasProfessores: [
            {titulo: 'Código', coluna: 'id'},
            {titulo: 'Nome', coluna: 'nome'}
        ],
        colunasGradeHorarias: [
            {titulo: 'Ano', coluna: 'ano'},
            {titulo: 'Semestre', coluna: 'semestreAno'}
        ],
        colunasTurmas: [
            {coluna: 'codigo', titulo: 'Código'},
            {coluna: 'nome', titulo: 'Nome'},
            {coluna: 'semestre', titulo: 'Semestre'}
        ],
        abasMenu: [
            {id: 'disciplinas', titulo: 'Disciplinas'},
            {id: 'professores', titulo: 'Professores'},
            {id: 'turmas', titulo: 'Turmas'},
            {id: 'gradesHorarias', titulo: 'Grades Horárias'},
            {id: 'gradesHorariaAtual', titulo: 'Grades Horária Atual'},
        ]
    }
};