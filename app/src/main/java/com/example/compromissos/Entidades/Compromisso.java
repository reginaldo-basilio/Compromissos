package com.example.compromissos.Entidades;

public class Compromisso {

    private String titulo;
    private String descricao;
    private String date;
    private String keyCompromisso;
    private String status;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKeyCompromisso() {
        return keyCompromisso;
    }

    public void setKeyCompromisso(String keyCompromisso) {
        this.keyCompromisso = keyCompromisso;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
