package com.example.luisguilherme.icult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textoLogin;
    private TextView entradaLoginEmail;
    private TextView entradaLoginSenha;
    private Button botaoLogin;
    private TextView textViewToCadastro;
    private ProgressDialog progressDialog;
    private FirebaseAuth autenticacao;
    private TextView tvEsqueciSenha;
    //mudar as variaveis do firabase Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textoLogin = (TextView) findViewById(R.id.textoLogin);
        entradaLoginEmail = (EditText) findViewById(R.id.entradaLoginEmail);
        entradaLoginSenha = (EditText) findViewById(R.id.entradaLoginSenha);
        botaoLogin = (Button) findViewById(R.id.botaoLogin);
        textViewToCadastro = (TextView) findViewById(R.id.textViewToCadastro);
        progressDialog = new ProgressDialog(this);
        tvEsqueciSenha = (TextView) findViewById(R.id.textViewEsqueciSenha);

        botaoLogin.setOnClickListener(this);
        textViewToCadastro.setOnClickListener(this);
        tvEsqueciSenha.setOnClickListener(this);

        autenticacao = FirebaseAuth.getInstance();

        if(autenticacao.getCurrentUser() != null){
            Intent chamaPaginaFeed = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(chamaPaginaFeed);
            finish();
        }

    }

    private void userLogin(){
        String email = entradaLoginEmail.getText().toString().trim();
        String senha =entradaLoginSenha.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this, "Por favor, insira seu e-mail", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(senha)){
            Toast.makeText(this, "Por favor, insira sua senha", Toast.LENGTH_LONG).show();
            return;
        }
        autenticacao.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logado com sucesso", Toast.LENGTH_LONG).show();
                            finish();
                            Intent chamaPaginaMain = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(chamaPaginaMain);
                            //adicionar o comando para levar a paginaprincial (Main Activity)
                        }else{
                            Toast.makeText(LoginActivity.this, "Nao foi possível fazer o login, tente novamente.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v == botaoLogin){
            //fara o login do usuario
            userLogin();
        }
        if (v == textViewToCadastro){
            //levara o usuario a pagina de cadastro
            Intent chamaPaginaCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(chamaPaginaCadastro);
        }
        if (v == tvEsqueciSenha){
            //abre a pagina de recuperação de senha
            Intent p = new Intent(LoginActivity.this, SenhaActivity.class);
            startActivity(p);
        }
    }
}
