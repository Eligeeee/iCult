package com.example.luisguilherme.icult;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ClipData.Item item;
    private FloatingActionButton fab;
    private FloatingActionButton fab_foto;
    private FloatingActionButton fab_video;
    public TextView tvEmailP;
    Animation FabOpen;
    Animation FabClose;
    Animation FabRClockwise;
    Animation FabRanticlockwise;
    //boolean utilizado no FAB
    boolean aberto = false;
    String emailUsuario;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_video = (FloatingActionButton) findViewById(R.id.fab_video);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        tvEmailP = (TextView) findViewById(R.id.textViewEmailPrincipal);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){

            emailUsuario = user.getEmail().toString();
            Log.i("Email logado aqui", emailUsuario);
            if(emailUsuario != null) {
                //tvEmailP.setText(emailUsuario);
            }
        }else{
            Toast.makeText(this, "Não foi possível encontrar seus dados. Pro favor, informe o erro(005)", Toast.LENGTH_LONG).show();
        }

        //Alterar o texto que aparece no tv de email principal



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aberto){

                    fab_video.startAnimation(FabClose);

                    fab.startAnimation(FabRanticlockwise);
                    fab_video.setClickable(false);

                    aberto = false;
                }else{
                    fab_video.startAnimation(FabOpen);

                    fab.startAnimation(FabRClockwise);

                    fab_video.setClickable(true);
                    aberto = true;
                }
            }
        });



        fab_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Carrega o fragment feed logo q é criado
        loadFragment(new FeedFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feedGlobal) {
            // Abrirá o fragmentfeed
            loadFragment(new FeedFragment());
        } else if (id == R.id.nav_map) {
            //Abrirá o mapa
            loadFragment(new GMapFragment());

        } else if(id == R.id.nav_pesquisa){
            //Abrirá o fragment de pesquisa de lugares
            loadFragment(new FragmentPesquisa());

        } else if(id == R.id.nav_sobre){
            //abrira o fragment sobre
            loadFragment(new FragmentSobreNos());

        }else if(id == R.id.nav_configuracao){
            loadFragment(new ConfigFragment());

        }else if(id == R.id.nav_contato){
            //abrira o fragment de contato
            loadFragment(new FragmentContato());
        }else if(id == R.id.nav_minhasFotos){
            loadFragment(new FragmentMinhasFotos());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void loadFragment(Fragment fragment) {
        // cria o FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //cria o fragmentTransaction para substituir o frameLayout
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // salva
    }

}
