package com.example.luisguilherme.icult;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SenhaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText entradaEmail;
    private Button botaoReset;
    private TextView tvVoltar;
    private String emailSoneca;
    private Snackbar snackbar;
    private LinearLayout linearLayout;
    private FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha);

        entradaEmail = (EditText) findViewById(R.id.entradaEmailRecupSenha);
        botaoReset = (Button) findViewById(R.id.botaoReset);
        tvVoltar = (TextView) findViewById(R.id.tvVoltar);
        linearLayout = (LinearLayout) findViewById(R.id.linearSenha); 

        botaoReset.setOnClickListener(this);
        tvVoltar.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == botaoReset) {
            //comandos para recup da senha
            //verifica se o usuario digitou seu email
            emailSoneca = entradaEmail.getText().toString().trim();
            if (TextUtils.isEmpty(emailSoneca)) {
                snackbar = Snackbar.make(linearLayout, "Insira seu e-mail", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                recupSenha(emailSoneca);
            }
        }
        if (v == tvVoltar){
            Intent p = new Intent(SenhaActivity.this, LoginActivity.class);
            startActivity(p);
        }
    }

    private void recupSenha(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    snackbar = Snackbar.make(linearLayout, "Enviamos um e-mail para: "+ emailSoneca, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    snackbar = Snackbar.make(linearLayout, "Não foi possível enviar o e-mail", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }
}
