package br.ufms.cpcx.timetabling.entity;

import javax.persistence.*;

@Entity
@Table(name = "TB_PROFESSOR")
public class Professor {
    @Id
    @Column(name = "PRO_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PRO_NOME", length = 100)
    private String nome;

    @Column(name = "PRO_CODIGO", length = 14)
    private Long codigo;

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

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
}
