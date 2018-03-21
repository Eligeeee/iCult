package com.example.luisguilherme.icult;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.luisguilherme.icult.modelo.ImageUploadLoc;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luis Guilherme on 25/09/2017.
 */

public class GMapFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap mGoogleMap;
    MapView mapView;
    View mView;
    Long numeroLocais;
    int i = 0;
    //map para salvar as latitudes
    final Map<Integer, String> latitudes = new HashMap<Integer, String>();
    //map para salvar as longitudes
    final Map<Integer, String> longitudes = new HashMap<Integer, String>();
    //map pata salvar os titulos
    final Map<Integer, String> titulos = new HashMap<Integer, String>();


    private DatabaseReference databaseReference;

    public GMapFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        Query query = databaseReference.child("image");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numeroLocais = dataSnapshot.getChildrenCount();
                Log.i("Numero de childs", String.valueOf(numeroLocais));
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUploadLoc imagem = snapshot.getValue(ImageUploadLoc.class);
                    //Add latitudes no Map
                    latitudes.put(i, imagem.getLatitude());
                    Log.i("Valor de latitudes", String.valueOf(latitudes.get(i)));
                    //Add longitudes no Map
                    longitudes.put(i, imagem.getLongitude());
                    Log.i("Valor de longitudes", String.valueOf(longitudes.get(i)));
                    //Add titulos no map
                    titulos.put(i, imagem.getTitulo());
                    Log.i("Valor de titulo:", String.valueOf(titulos.get(i)));
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_gmap, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) mView.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //comandos para add o marker nos lugares
        mGoogleMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MapsInitializer.initialize(getContext());
            mGoogleMap = googleMap;
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } else {
            //cria o toast falando do suporte
            Toast.makeText(getActivity(), "Seu dispositivo não possui suporte ao mapa, por favor informe o erro(008)", Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        for(int contador = 0; contador < Double.valueOf(numeroLocais); contador ++){
            // Log.i("Valor das lati",latitudes.get(contador));
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitudes.get(contador)),Double.parseDouble(longitudes.get(contador)))).title(titulos.get(contador)));
        }

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String tituloMarcador = marker.getTitle();
                Log.i("Titulo do marcador", tituloMarcador);
                Toast.makeText(getActivity(), "Selecionado: "+ tituloMarcador, Toast.LENGTH_LONG).show();
                //Inicia a activity de detalhes dos lugares
                //E passa para ela a viariável tituloMarcador
                //Q será usada para a pesquisa da imagem e dos dados
                Intent intent = new Intent(getActivity(), PlaceDetailsActivity.class);
                intent.putExtra("TITULO_DO_LOCAL", tituloMarcador);
                startActivity(intent);
            }
        });

    }

}
