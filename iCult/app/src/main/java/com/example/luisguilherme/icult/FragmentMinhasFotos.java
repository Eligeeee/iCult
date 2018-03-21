package com.example.luisguilherme.icult;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.luisguilherme.icult.modelo.ImageUploadLoc;
import com.google.firebase.auth.FirebaseAuth;
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
 * {@link FragmentMinhasFotos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMinhasFotos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMinhasFotos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private String emailLogado;
    private List<ImageUploadLoc> imgList;
    private ImageLocFeedAdapter adapter;
    private ListView listView;


    public FragmentMinhasFotos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMinhasFotos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMinhasFotos newInstance(String param1, String param2) {
        FragmentMinhasFotos fragment = new FragmentMinhasFotos();
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
        imgList = new ArrayList<>();

        //retorna o email do usuario logado
        firebaseAuth = FirebaseAuth.getInstance();
        emailLogado = firebaseAuth.getCurrentUser().getEmail();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //faz a pesquisa das imagens upadas
        Query query = databaseReference.child("image").orderByChild("autor").equalTo(emailLogado);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImageUploadLoc imageUploadLoc = snapshot.getValue(ImageUploadLoc.class);
                    if(imageUploadLoc != null){
                        imgList.add(imageUploadLoc);
                    }

                }
                adapter = new ImageLocFeedAdapter(getActivity(), R.layout.image_item, imgList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View lg = inflater.inflate(R.layout.fragment_minhas_fotos, container, false);

        listView = (ListView) lg.findViewById(R.id.listViewMinhasImagens);

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
