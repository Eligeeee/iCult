package com.example.luisguilherme.icult.modelo;

/**
 * Created by Luis Guilherme on 19/06/2017.
 */

public class ImageUpload {

    public String titulo;
    public String descricao;
    public String url;
    //public String latitude;
    //public String longitude;
    //public String cidade;
   // public String pais;

    public ImageUpload(){

    }

    public ImageUpload(String titulo, String descricao, String url) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.url = url;
        //this.latitude = latitude;
        //this.longitude = longitude;
        //this.pais = pais;
        //this.cidade = cidade;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setUri(String uri) {
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getUrl() {
        return url;
    }

    /*

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    */
}

