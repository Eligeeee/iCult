package com.example.luisguilherme.icult;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class UpFotoCamActivity extends AppCompatActivity {

    private TextView tvUpFoto;
    private ImageView imgView;
    private EditText entradaTitulo;
    private EditText entradaDescricao;
    private Button botaoPostar;
    private Button botaoTirarFoto;
    public static  int REQUEST_CODE_CAMERA = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_foto_cam);

        tvUpFoto = (TextView) findViewById(R.id.tvUpFoto);
        imgView = (ImageView) findViewById(R.id.imgViewUpFoto);
        entradaTitulo = (EditText) findViewById(R.id.entradaTituloUpFoto);
        entradaDescricao = (EditText) findViewById(R.id.entradaDescricaoUpFoto);
        botaoPostar = (Button) findViewById(R.id.botaoUpFoto);
        botaoTirarFoto = (Button) findViewById(R.id.botaoTira);

        //Cria o diretório q armazenará as imagens
        final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
        File dirImg = new File(dir);
        dirImg.mkdirs();

        botaoPostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //envia a foto e os dados
                //para o firebase
            }
        });

        //xama a camera
        botaoTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                /*
                String file = dir+ DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";
                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                } catch (IOException e) {}
                Uri outputFileUri = Uri.fromFile(newfile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                */
                startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CAMERA){
            if(resultCode == RESULT_OK){
                if(data != null){
                    //add img no imgview
                    Bitmap agrvai = (Bitmap)data.getExtras().get("data");
                    imgView.setImageBitmap(agrvai);
                    //Intent takePictureIntent = getIntent();
                    //Bundle bundle = takePictureIntent.getExtras();
                    //Log.i("Valor de bundle", String.valueOf(bundle));
                }else {
                    //Data = null
                    Toast.makeText(UpFotoCamActivity.this, "Erro, data = null", Toast.LENGTH_LONG).show();
                }

            }else{
                //Erro no resultCode
                Toast.makeText(UpFotoCamActivity.this, "Erro, no resultCode", Toast.LENGTH_LONG).show();
            }
        }else{
            //Erro no requestCode
            Toast.makeText(UpFotoCamActivity.this, "Erro, requestCode", Toast.LENGTH_LONG).show();
        }
    }
}
