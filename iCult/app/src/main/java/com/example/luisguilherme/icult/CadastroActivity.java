package com.example.luisguilherme.icult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luisguilherme.icult.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText entradaNome;
    private EditText entradaEmail;
    private EditText entradaSenha;
    private Button botaoCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private TextView textViewToLogin;
    private int qntSenha;
    private String senhacont;
    private ProgressDialog progressDialog;
    String nome,senha,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        entradaNome = (EditText) findViewById(R.id.entradaNome);
        entradaEmail = (EditText) findViewById(R.id.entradaEmail);
        entradaSenha = (EditText) findViewById(R.id.entradaSenha);
        botaoCadastrar = (Button) findViewById(R.id.botaoCadastrar);
        textViewToLogin = (TextView) findViewById(R.id.textViewToLogin);
        progressDialog = new ProgressDialog(this);

        textViewToLogin.setOnClickListener(this);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qntSenha = 0;
                qntSenha = entradaSenha.length();
                Log.i("Qnt de caracter da senha", String.valueOf(qntSenha));
                usuario = new Usuario();
                usuario.setNome(entradaNome.getText().toString());
                usuario.setSenha(entradaSenha.getText().toString());
                usuario.setEmail(entradaEmail.getText().toString());

                nome = entradaNome.getText().toString().trim();
                senha = entradaSenha.getText().toString().trim();
                email = entradaEmail.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    //Email está vazio
                    Toast.makeText(CadastroActivity.this, "Por favor insira seu e-mail", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(senha)){
                    //senha está vazio
                    Toast.makeText(CadastroActivity.this, "Por favor insira sua senha", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(nome)){
                    //nome está vazio
                    Toast.makeText(CadastroActivity.this, "Por favor insira seu nome", Toast.LENGTH_SHORT).show();
                }else if(entradaSenha.length() <= 7){
                    Toast.makeText(CadastroActivity.this, "Por favor, insira uma senha com mais de 7 caracteres", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.setMessage("Criando usuário...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    cadastrarUsuario();
                }
            }
        });
    }

    private void cadastrarUsuario(){

        autenticacao = FirebaseConfig.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvar();
                    enviarVerificacaoEmail();
                    //Intent chamaPaginaFeed = new Intent(CadastroActivity.this, MainActivity.class);
                    //startActivity(chamaPaginaFeed);
                }else{
                    Toast.makeText(CadastroActivity.this, "Não foi possível cadastrar, por favor tente novamente.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == textViewToLogin){
            Intent chamaPaginaLogin = new Intent(CadastroActivity.this, LoginActivity.class);
            startActivity(chamaPaginaLogin);
        }
    }
    private void enviarVerificacaoEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CadastroActivity.this, "Verifique seu email", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }
}
