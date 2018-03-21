package com.example.luisguilherme.icult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Luis Guilherme on 13/06/2017.
 */



public final class FirebaseConfig {
    //Referencia do Firebase
    private static DatabaseReference referenceFirebase;
    private static FirebaseAuth autenticacao;

    //Metodo que vai pegar a referencia do FireBase


    public static DatabaseReference getFirebase(){
        if( referenceFirebase == null) {

            referenceFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenceFirebase;
    }

    public static FirebaseAuth getAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}