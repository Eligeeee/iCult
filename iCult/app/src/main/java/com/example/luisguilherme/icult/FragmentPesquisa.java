package com.example.luisguilherme.icult;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.luisguilherme.icult.modelo.ImageUploadLoc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPesquisa.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPesquisa#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPesquisa extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private EditText entradaCidade;
    private EditText entradaPais;
    public RadioButton rbCidade;
    public RadioButton rbPais;
    private ImageButton botaoPesquisar;
    private ImageButton botaoPesquisaPais;
    private ImageButton botaoPesquisaCidade;
    private List<ImageUploadLoc> imgList;
    String local;
    private ImageLocFeedAdapter adapter;
    private RecyclerView recyclerView;
    private ListView listView;

    public RadioGroup rg;

    private LinearLayout linearCidade;
    private LinearLayout linearPais;

    private DatabaseReference databaseReference;
    //referencia de child de image
    private  DatabaseReference imageReference;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentPesquisa() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPesquisa.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPesquisa newInstance(String param1, String param2) {
        FragmentPesquisa fragment = new FragmentPesquisa();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        imageReference =  databaseReference.child("image");


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View lg = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        rg = (RadioGroup) lg.findViewById(R.id.radioGroupLocalidades);

        //recyclerView = (RecyclerView) lg.findViewById(R.id.rvPesquisa);

        linearCidade = (LinearLayout) lg.findViewById(R.id.linearPesquisaCidade);
        linearPais = (LinearLayout) lg.findViewById(R.id.linearPesquisaPais);

        botaoPesquisaCidade = (ImageButton) lg.findViewById(R.id.botaoPesquisaCidade);
        botaoPesquisaPais = (ImageButton) lg.findViewById(R.id.botaoPesquisaPais);

        rbCidade = (RadioButton) lg.findViewById(R.id.radioButtonCidade);
        rbPais = (RadioButton) lg.findViewById(R.id.radioButtonPais);

        listView = (ListView) lg.findViewById(R.id.listViewPesquisa);

        imgList = new ArrayList<>();
        entradaCidade = (EditText) lg.findViewById(R.id.entradaPesquisaCidade);
        entradaPais = (EditText) lg.findViewById(R.id.entradaPesquisaPais);

        //recyclerView = (RecyclerView) lg.findViewById(R.id.rvPesquisa);
        //listView = (ListView) lg.findViewById(R.id.listViewPesquisa);

        rbPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //faz sumir tudo q é de pesquisa de cidade
                linearPais.setVisibility(View.VISIBLE);
                linearCidade.setVisibility(View.INVISIBLE);

            }
        });

        rbCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //faz sumir tudo q é de pesquisa de pais
                linearPais.setVisibility(View.INVISIBLE);
                linearCidade.setVisibility(View.VISIBLE);
            }
        });

        botaoPesquisaPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                local = entradaPais.getText().toString();
                pesquisarLugares("pais", local);
            }
        });

        botaoPesquisaCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                local = entradaCidade.getText().toString();
                pesquisarLugares("cidade", local);
            }
        });

        return lg;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void pesquisarLugares(String child, String local){
        Query query = databaseReference.child("image").orderByChild(child).equalTo(local);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUploadLoc imageUploadLoc = snapshot.getValue(ImageUploadLoc.class);
                    imgList.add(imageUploadLoc);
                }
                adapter = new ImageLocFeedAdapter(getActivity(), R.layout.image_item, imgList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
