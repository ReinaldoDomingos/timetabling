package br.ufms.cpcx.timetabling.entity;

import javax.persistence.*;

@Entity
@Table(name = "TB_DISCIPLINA")
public class Disciplina {
    @Id
    @Column(name = "DIS_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DIS_NOME", length = 100)
    private String nome;

    @Column(name = "DIS_CODIGO", length = 14)
    private String codigo;

    @Column(name = "DIS_CARGA_HORARIA", length = 14)
    private Long cargaHoraria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Long cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
}
