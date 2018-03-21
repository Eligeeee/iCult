package com.example.luisguilherme.icult.modelo;

import com.example.luisguilherme.icult.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Luis Guilherme on 13/06/2017.
 */

public class Usuario {

    public Usuario(){

    }

    public void salvar(){
        DatabaseReference referenciaFirebase = FirebaseConfig.getFirebase();
        referenciaFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);
    }

    private String id;
    private String nome;
    private String email;
    private String senha;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}
