package com.example.luisguilherme.icult;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FragmentContato extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private TextView tvContato;
    private EditText entradaAssunto;
    private EditText entradaMensagem;
    private Button botaoEnviar;
    private TextView tvInfo;
    String assunto;
    String mensagem;
    String[] enderecos;
    public static final String emailProjeto = "icult.projeto@gmail.com";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentContato() {
        // Required empty public constructor
    }


    public static FragmentContato newInstance(String param1, String param2) {
        FragmentContato fragment = new FragmentContato();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contato, container, false);
        tvContato = (TextView) view.findViewById(R.id.tvContato);
        entradaAssunto = (EditText) view.findViewById(R.id.entradaAssunto);
        entradaMensagem = (EditText) view.findViewById(R.id.entradaMensagem);
        tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        botaoEnviar = (Button) view.findViewById(R.id.botaoEnviarEmail);

        firebaseAuth = FirebaseAuth.getInstance();
        //Funçãozinha para retornar o email do usuario
        //q ta logado no app
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String emailUsuario = user.getEmail();

        }else{
            Toast.makeText(getActivity(), "Não foi possível encontrar seus dados. Pro favor, informe o erro(007)", Toast.LENGTH_LONG).show();
        }

        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assunto = entradaAssunto.getText().toString();
                mensagem = entradaAssunto.getText().toString();
                Intent enviarEmail = new Intent(Intent.ACTION_SEND);
                enviarEmail.setType("message/rfc822");
                enviarEmail.putExtra(Intent.EXTRA_EMAIL  , new String[]{emailProjeto});
                enviarEmail.putExtra(Intent.EXTRA_SUBJECT, assunto);
                enviarEmail.putExtra(Intent.EXTRA_TEXT   , mensagem);
                startActivity(Intent.createChooser(enviarEmail, "Enviando..."));

            }
        });




        return view;
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
