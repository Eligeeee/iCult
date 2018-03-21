package com.example.luisguilherme.icult;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luisguilherme.icult.modelo.ImageUploadLoc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class FeedFragment extends Fragment {


    int numeroGostei = 0;
    private DatabaseReference databaseReference;
    private List<ImageUploadLoc> imgList;
    private ListView lvfragment;
    private ImageLocFeedAdapter adapter;
    private ProgressDialog progressDialog;
    private FirebaseAuth autenticacao;
    private Button botaoGostei;
    private Button botaoVerMapa;
    private TextView tvLike;
    int qntLike = 0;
    private Boolean likeClicado = false;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);


        imgList = new ArrayList<>();
        lvfragment = (ListView) view.findViewById(R.id.listview);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Carregando feed de imagens");
        progressDialog.show();
        tvLike = (TextView) view.findViewById(R.id.textViewContador);
        databaseReference = FirebaseDatabase.getInstance().getReference(UpFotoFragment.FB_DATABASE_PATH);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUploadLoc imageUploadLoc = snapshot.getValue(ImageUploadLoc.class);
                    imgList.add(imageUploadLoc);
                }

                //Adapter
                adapter = new ImageLocFeedAdapter(getActivity(), R.layout.image_item, imgList);
                //Set adapter for listview
                lvfragment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


        lvfragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               int posicao = view.getVerticalScrollbarPosition();
               Log.i("---------Posição da vie do list", String.valueOf(posicao));
                Toast.makeText(getActivity(), "Item clicado" + position, Toast.LENGTH_LONG).show();

            }
        });






        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    void adicionarGostei(){
        numeroGostei ++;
    }
}