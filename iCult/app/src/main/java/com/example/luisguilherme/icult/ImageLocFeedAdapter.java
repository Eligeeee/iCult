package com.example.luisguilherme.icult;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luisguilherme.icult.modelo.ImageUploadLoc;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Luis Guilherme on 08/09/2017.
 */

public class ImageLocFeedAdapter extends ArrayAdapter<ImageUploadLoc>{

    private DatabaseReference databaseReference;
    private int gosteiClicado = 1; //0 = n foi clicado, 1 = clicado, 2 = desclicado
    public int qntGostei;
    public int qntdeGostei;
    private Activity context;
    private int resource;
    private List<ImageUploadLoc> listImage;

    public ImageLocFeedAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ImageUploadLoc> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(R.id.textViewTituloImagem);
        TextView tvDescricao = (TextView) v.findViewById(R.id.textViewDescricaoImagem);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        TextView tvCidade = (TextView) v.findViewById(R.id.textViewCidade);
        TextView tvPais = (TextView) v.findViewById(R.id.textViewPais);
        final TextView tvGostei = (TextView) v.findViewById(R.id.tvGostei);
        final Button botaoGostei  = (Button) v.findViewById(R.id.botaoGostei);
        Button botaoMapa = (Button) v.findViewById(R.id.botaoVerNoMapa);


        tvName.setText(listImage.get(position).getTitulo());
        tvDescricao.setText(listImage.get(position).getDescricao());
        Glide.with(context).load(listImage.get(position).getUri()).into(img);
        tvCidade.setText(listImage.get(position).getCidade());
        tvPais.setText(listImage.get(position).getPais());
        tvGostei.setText(String.valueOf(listImage.get(position).getGostei()));

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Pesquisa dos dados

        //Passos para aletrar aqntde de gostei
        //mostra no tvGostei
        //retorna da classe
        //incrementa
        //salva no bd

        //Lógica do botao de gostei
        botaoGostei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //altura o valor de gostei clicado

               switch (gosteiClicado){

                   case 1:
                       // foi clicado
                       // retorna a qntde de like
                       //incrementa ela
                       //mostra a usuario
                       //muda o valor de gosteiClicado
                       qntGostei = listImage.get(position).getGostei();
                       qntGostei ++;
                       Log.i("Quantidade de like aumentada: ",String.valueOf(qntGostei));
                       botaoGostei.setTextColor(Color.parseColor("#FF5722"));
                       tvGostei.setText(String.valueOf(qntGostei));
                       gosteiClicado = 2;
                       break;

                   case 2:
                       //foi desclicado
                       //retorna a qntde de like
                       //diminui ela
                       //mostra ao usuario
                       //muda o valor de  gosteiClicado
                       qntGostei --;
                       Log.i("Qntde de like diminuido", String.valueOf(qntGostei));
                       botaoGostei.setTextColor(Color.parseColor("#000000"));
                       tvGostei.setText(String.valueOf(qntGostei));
                       gosteiClicado = 1;
                       break;
               }
            }
        });

        botaoMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Abrindo o mapa...", Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }
}
