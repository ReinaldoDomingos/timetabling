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
        COLUNAS_DISCIPLINAS: [
            {
                classes: [],
                coluna: 'codigo',
                titulo: 'Código'
            },
            {
                classes: [],
                coluna: 'nome',
                titulo: 'Nome'
            },
            {titulo: 'C.H', coluna: 'cargaHoraria'}
        ],
        colunasDisciplinasGradeHoraria: [
            {
                titulo: 'Nome',
                coluna: 'nome',
                classes: []
            },
            {
                titulo: 'C.H',
                classes: ['col-1'],
                coluna: 'cargaHoraria'
            },
            {
                editavel: true,
                titulo: 'C.H.S',
                classes: ['col-1'],
                coluna: 'cargaHorariaSemanal'
            }
        ],
        COLUNAS_PROFESSORES: [
            {
                classes: [],
                coluna: 'id',
                titulo: 'Código'
            },
            {
                classes: [],
                coluna: 'nome',
                titulo: 'Nome'
            }
        ],
        COLUNAS_GRADE_HORARIAS: [
            {
                classes: [],
                titulo: 'Ano',
                coluna: 'ano'
            },
            {
                classes: [],
                titulo: 'Semestre',
                coluna: 'semestreAno'
            }
        ],
        COLUNAS_TURMAS: [
            {
                classes: [],
                coluna: 'codigo',
                titulo: 'Código'
            },
            {
                classes: [],
                titulo: 'Nome',
                coluna: 'nome'
            },
            {coluna: 'semestre', titulo: 'Semestre'}
        ],
        ABAS_MENU: [
            {id: 'disciplinas', titulo: 'Disciplinas'},
            {id: 'professores', titulo: 'Professores'},
            {id: 'turmas', titulo: 'Turmas'},
            {id: 'gradesHorarias', titulo: 'Grades Horárias'}
        ]
    }
};