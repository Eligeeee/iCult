package com.example.luisguilherme.icult;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luisguilherme.icult.modelo.ImageUpload;
import com.example.luisguilherme.icult.modelo.ImageUploadLoc;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class UpFotoFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    //Numero do request das permissoes:
    //1 -
    public int numeroGostei = 0;
    private ImageView imgview;
    private String mParam1;
    private String mParam2;
    private Uri imageUri;
    private Bundle imageUriDois;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    //referencia do bd
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference mImageReference;
    private FirebaseAuth firebaseAuth;

    private LinearLayout linearCamera;
    private LinearLayout linearGaleria;

    // Declara o botao que abrirá a galeria
    private Button botaoGaleria;
    //Declara o botão que abrirá a camera
    private Button botaoCamera;
    //Declara o botao que enviará as iamgens
    private Button botaoPostarCamera;

    private Button botaoPostarGaleria;

    //Declara os editText
    private EditText entradaTitulo;
    private EditText entradaDescricao;
    private Button tira;


    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_CODE_GALLERY = 2; // 2 para o retorno da galeria
    public static final int REQUEST_CODE_CAMERA = 1; // 1 para o retorno da camera
    public static final int REQUEST_CODE_CAMERA2 = 3; // 3 para o teste da intent da camera
    private Uri filePath;
    private String pais;
    private String cidade;
    private String endereco;
    private String complemento;
    private String latitude;
    private String longitude;
    int mLarguraImagem;
    int mAlturaImagem;
    File mCaminhoFoto;
    CarregarImageTask mTask;
    Uri caminhoArquivo;

    private OnFragmentInteractionListener mListener;

    LocationManager locationManager;
    LocationListener locationListener;
    Geocoder geocoder;
    List<Address> enderecos;

    @Override
    public void onRequestPermissionsResult(int requestCodePermission, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCodePermission, permissions, grantResults);
        switch (requestCodePermission) {
            case 3:
                configureBotao();
        }

    }

    public UpFotoFragment() {
        // Required empty public constructor
    }


    public static UpFotoFragment newInstance(String param1, String param2) {
        UpFotoFragment fragment = new UpFotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setRetainInstance(true);

        progressDialog = new ProgressDialog(getActivity());

        String caminhoFoto = Util.getUltimaMidia(getActivity(), Util.MIDIA_FOTO);
        if (caminhoFoto != null) {
            mCaminhoFoto = new File(caminhoFoto);
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up_foto, container, false);
        view.getViewTreeObserver().addOnGlobalLayoutListener(this);

        botaoCamera = (Button) view.findViewById(R.id.botaoCameraFragment);
        botaoGaleria = (Button) view.findViewById(R.id.botaoGaleriaFragment);
        botaoPostarCamera = (Button) view.findViewById(R.id.botaoPostarFotoFragmentCamera);
        botaoPostarGaleria = (Button) view.findViewById(R.id.botaoPostarFotoFragmentGaleria);

        imgview = (ImageView) view.findViewById(R.id.imgViewFragmentFoto);

        linearCamera = (LinearLayout) view.findViewById(R.id.linearBotaoUpCamera);
        linearGaleria = (LinearLayout) view.findViewById(R.id.linearBotaoUpGalera);


        entradaTitulo = (EditText) view.findViewById(R.id.entradaTituloFragment);
        entradaDescricao = (EditText) view.findViewById(R.id.entradaDescricaoFragment);

        //Coisas do firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        //Coisas para o retorno da localização
        enderecos = new ArrayList<>();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                enderecos.clear();

                try {
                    enderecos = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    latitude = Double.toString(location.getLatitude());
                    longitude = Double.toString(location.getLongitude());
                    pais = enderecos.get(0).getCountryName();
                    cidade = enderecos.get(0).getLocality();
                    endereco = enderecos.get(0).getThoroughfare();
                    complemento = enderecos.get(0).getSubThoroughfare();
                    Log.i("Voce está no pais: ", pais + ", cidade: " + cidade + ", endereco: " + endereco + ", complemento: " + complemento);

                } catch (IOException e) {
                    Log.i("exp", e.toString());
                    Log.e("tag", "onLocationChanged: ", e);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        //pede a permissao de usar a camera ao usuario

        //Pede a permissão ao usuario de localização
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Pede a permissao
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 3);

            } else {

                // temos a permissao
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }
        //abre a galeria
        botaoGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            //xama a pag de galeria
            public void onClick(View v) {
                //Inicia a intent q xama a galeria
                //e retorna o URI da imagem selecionada na camera
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Selecione a imagem:"), REQUEST_CODE_GALLERY);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }
        });

        //abre a camera
        //e pede localização

        botaoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xama a intent q inicia a camera
                //Dps cria usa o diretorio criado (dirImg)
                //Para fazer o armazenamento da foto
                //E pegar um URI, q sera os bang de hora aqui embaixo
                //Pede a permiassao de usar a camera
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //abre a camera para tirar a foto
                    abrirCamera();
                    //pedirLocalizacao();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
                }
            }
        });

        //Botao Para upload da imagem da camera
        botaoPostarCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama a função para fazer o upload da midia
                // enviarImagem(entradaTitulo.getText().toString(), entradaDescricao.getText().toString(), caminhoArquivo);
                pedirLocalizacao();
                if (cidade != null) {
                    String confTitulo = entradaTitulo.getText().toString();
                    String confDescricao = entradaDescricao.getText().toString();
                    if (TextUtils.isEmpty(confTitulo)) {
                        Toast.makeText(getActivity(), "Por favor, insira um título", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.isEmpty(confDescricao)) {
                            Toast.makeText(getActivity(), "Por favor, insira uma descrição para a imagem", Toast.LENGTH_SHORT).show();
                        } else {
                            enviarImagemComLocalizacao(entradaTitulo.getText().toString(), entradaDescricao.getText().toString(), caminhoArquivo, pais, cidade, latitude, longitude, 0);
                            progressDialog.setMessage("Adicionando ao feed...");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Não foi possível determinar sua localização, por favor tente novamente...", Toast.LENGTH_LONG).show();
                }


            }
        });

        botaoPostarGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarImagem(entradaTitulo.getText().toString(), entradaDescricao.getText().toString(), imageUri);

            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //REQUESTCODE_FOTO = 1
        switch (requestCode) {
            //request da camera, valor 1
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    carregarImagem();
                    Log.i("Valor de mCaminhoFoto", String.valueOf(mCaminhoFoto));
                    //faz o up dps de carregar a imagem no imgview
                    URI filePath = mCaminhoFoto.toURI();
                    Log.i("Valor de filePath", String.valueOf(filePath));
                    caminhoArquivo = Uri.fromFile(new File(String.valueOf(mCaminhoFoto)));
                    //enviarImagemCamera(entradaTitulo.getText().toString(), entradaDescricao.getText().toString(), caminhoArquivo);
                    //faz aparecer o botao de up da camera
                    //poq o request é da camera
                    linearCamera.setVisibility(View.VISIBLE);
                    if (cidade != null) {
                        Log.i("Valor de titulo", entradaTitulo.getText().toString());
                        Log.i("Valor de descricao ", entradaDescricao.getText().toString());
                        Log.i("Valor de uri ", String.valueOf(caminhoArquivo));
                        Log.i("Valor de pais", pais);
                        Log.i("Valor de cidade ", cidade);
                        Log.i("Valor de latitude", String.valueOf(latitude));
                        Log.i("Valor de longitude", String.valueOf(longitude));
                    } else {
                        Toast.makeText(getActivity(), "Não foi possível determinar sua localização, por favor tente novamente...", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            //request da galeria, valor 2
            case REQUEST_CODE_GALLERY:
                //Cod para add a foto da galeria no imgview
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        try {
                            imageUri = data.getData();
                            Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            imgview.setImageBitmap(bm);
                            //faz aparecer o botao da galeria
                            //poq o request é da galeria
                            linearGaleria.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //erro no data = null
                        Toast.makeText(getActivity(), "Não foi possível recuperar os dados da imagem. Por favor, informe o erro(001)", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Erro no requestCode
                    Toast.makeText(getActivity(), "Não foi possível recuperar os dados da imagem. Por favor, informe o erro(002)", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

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

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        mLarguraImagem = imgview.getWidth();
        mAlturaImagem = imgview.getHeight();
        //carregarImagem();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getimageExt(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void abrirCamera() {
        mCaminhoFoto = Util.novaMidia(Util.MIDIA_FOTO);
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCaminhoFoto));
        startActivityForResult(it, Util.REQUESTCODE_FOTO);
    }

    private void carregarImagem() {
        if (mCaminhoFoto != null && mCaminhoFoto.exists()) {
            if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
                mTask = new CarregarImageTask();
                mTask.execute();
            }
        }
    }

    class CarregarImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imgview.setImageBitmap(bitmap);
                Util.salverUltimaMidia(getActivity(), Util.MIDIA_FOTO, mCaminhoFoto.getAbsolutePath());
            }
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return Util.carregarImagem(mCaminhoFoto, mLarguraImagem, mAlturaImagem);


        }
    }

    public void enviarImagem(final String titulo, final String descricao, Uri caminhozinho) {
        StorageReference referencia = storageReference.child(FB_STORAGE_PATH + caminhozinho.getLastPathSegment());
        referencia.putFile(caminhozinho).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImageUpload imageUpload = new ImageUpload(titulo, descricao, taskSnapshot.getDownloadUrl().toString());
                //adquire o id para o bd do firebase
                String uploadId = databaseReference.push().getKey();
                //add a foto no child do id q foi criado ali em cima
                databaseReference.child(uploadId).setValue(imageUpload);
                Toast.makeText(getActivity(), "Imagem adionada do feed", Toast.LENGTH_LONG).show();
                Intent matheusViado = new Intent(getContext(), MainActivity.class);
                startActivity(matheusViado);
            }
        });


    }

    public void enviarImagemComLocalizacao(final String titulo, final String descricao, final Uri caminhozinho, final String mPais, final String mCidade, final String mLatitude, final String mLongitude, final int mGostei) {
        StorageReference referencia = storageReference.child(FB_STORAGE_PATH + caminhozinho.getLastPathSegment());
        referencia.putFile(caminhozinho).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //ImageUpload imageUpload = new ImageUpload(titulo, descricao, taskSnapshot.getDownloadUrl().toString());
                String autor = firebaseAuth.getCurrentUser().getEmail();
                ImageUploadLoc imageUploadLoc = new ImageUploadLoc(titulo, descricao, taskSnapshot.getDownloadUrl().toString(), mPais, mCidade, autor, mLatitude, mLongitude, mGostei);
                //adquire o id para o bd do firebase
                String uploadId = databaseReference.push().getKey();
                //add a foto no child do id q foi criado ali em cima
                databaseReference.child(uploadId).setValue(imageUploadLoc);
                Toast.makeText(getActivity(), "Imagem adionada do feed", Toast.LENGTH_LONG).show();
                Intent matheusViado = new Intent(getContext(), MainActivity.class);
                startActivity(matheusViado);
            }
        });


    }

    void configureBotao() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 3);
            }
            return;
        }
    }

    void pedirLocalizacao() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

}