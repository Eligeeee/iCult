package com.example.luisguilherme.icult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luisguilherme.icult.modelo.ImageUploadLoc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailsActivity extends AppCompatActivity {

    private String tituloLocal;
    private DatabaseReference databaseReference;
    private List<ImageUploadLoc> imgList;
    private ListView listView;
    private ImageLocFeedAdapter adapter;
    private Button botaoVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);


        listView = (ListView) findViewById(R.id.listViewPlaceDetails);
        imgList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        botaoVoltar = (Button) findViewById(R.id.botaoVoltar);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("TITULO_DO_LOCAL")!= null){
            tituloLocal = bundle.getString("TITULO_DO_LOCAL");
            Log.i("Valor do titulozinho: ", tituloLocal);
        }else{
            Toast.makeText(this, "Não foi possível retornar informações do local, por favor informe o erro (009)", Toast.LENGTH_LONG).show();
        }



        //Faz a pesquisa das informações da imagem pelo titulo
        //Pesquisa realizada no nosso BD e na Places API
        Query query = databaseReference.child("image").orderByChild("titulo").equalTo(tituloLocal);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImageUploadLoc imagem = snapshot.getValue(ImageUploadLoc.class);
                    imgList.add(imagem);
                }
                adapter = new ImageLocFeedAdapter(PlaceDetailsActivity.this, R.layout.image_item_without_actions, imgList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlaceDetailsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }





}
