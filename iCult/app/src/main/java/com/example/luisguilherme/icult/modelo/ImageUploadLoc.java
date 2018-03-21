package com.example.luisguilherme.icult.modelo;

/**
 * Created by Luis Guilherme on 07/09/2017.
 */

public class ImageUploadLoc {

    public String titulo;
    public String descricao;
    public String uri;
    public String cidade;
    public String pais;
    public String autor;
    public String latitude;
    public String longitude;
    public int gostei; //usada para o contador de likes da foto




    public ImageUploadLoc(){

    }
    public  ImageUploadLoc(String titulo, String descricao, String uri, String pais, String cidade,String autor, String latitude, String longitude, int gostei){
        this.titulo = titulo;
        this.descricao = descricao;
        this.uri = uri;
        this.pais = pais;
        this.cidade = cidade;
        this.autor = autor;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gostei = gostei;
    }

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

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

    public int getGostei() {
        return gostei;
    }

    public void setGostei(int gostei) {
        this.gostei = gostei;
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

}
