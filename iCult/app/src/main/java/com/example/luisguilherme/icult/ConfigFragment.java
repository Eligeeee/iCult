package com.example.luisguilherme.icult;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luisguilherme.icult.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment {

    private TextView tvConfig;
    public TextView tvNome;
    private TextView tvEmail;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Button botaoSair;
    FirebaseUser user;
    String nome;
    String emailUsuario;
    final String email = "email";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2) {
        ConfigFragment fragment = new ConfigFragment();
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

        //comandos para retornar as info do user
        //Comandos para alterar o nome do usuario
        //1-pesquisar o nome do usuario pelo email dele
        //1.1 Retornar o email dele
        //1.2 Pesquisar e mostrar pra ele o nome que ta aqui
        //2- alterar o nome de acordo com o que ele informar

        //Faz a pesquisa pelo email do usuario
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);


        botaoSair = (Button) view.findViewById(R.id.botaoSair);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        TextView tvSenha = (TextView) view.findViewById(R.id.textViewToSenha);

        tvSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SenhaActivity.class);
                startActivity(i);

            }
        });

        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent mathviado = new Intent(getContext() , CadastroActivity.class);
                startActivity(mathviado);
                getActivity().finish();
            }
        });

        //retorna o email do usuario
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){

            //o usuario ta logado
            emailUsuario = user.getEmail();
            Log.i("Email do usuario logado:", emailUsuario);
            if(emailUsuario != null) {
                //tvEmail.setText(String.valueOf(emailUsuario));
            }

            //realiza a busca do nome atraves do email
            Query query = databaseReference.child("usuario").orderByChild("email").equalTo(emailUsuario);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                       Usuario usuario =  snapshot.getValue(Usuario.class);
                       nome = usuario.getNome();
                       Log.i("Nome do usuario", nome);
                       Toast.makeText(getActivity(), "nome" + nome, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            //O usuario n ta logado
            Intent idd = new Intent(getActivity(), LoginActivity.class);
            startActivity(idd);
            firebaseAuth.signOut();
            getActivity().finish();
        }

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
