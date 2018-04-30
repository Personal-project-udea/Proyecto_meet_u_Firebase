package com.martinez.steven.practica_2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.martinez.steven.practica_2.model.Eventos;
import com.martinez.steven.practica_2.model.Usuarios;
import com.squareup.picasso.Picasso;

import java.util.logging.LogManager;

public class PrincipalActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    String user = "", pwd = "", email = "";
    Bundle extras;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    private FragmentManager fm;
    private FragmentTransaction ft;

    BottomNavigationView navigation;

    private PagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public Bundle arg = new Bundle();

    //---------variables perfil------

    EditText eUsuario , eCorreo, eTelefono;
    ImageView iFoto;

    private DatabaseReference databaseReference;
    //------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //inicializando variables perfil---

        eUsuario = findViewById(R.id.eUsuario);
        eCorreo = findViewById(R.id.eCorreo);
        iFoto = findViewById(R.id.iFoto);
        eTelefono = findViewById(R.id.eTelefono);

        //--------------------------------


        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();


        inicializar();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ft = fm.beginTransaction();
                switch (item.getItemId()) {

                    case R.id.mEvent:
                        //mTextMessage.setText(R.string.Superman);
                        EventosFragment fragment = new EventosFragment();
                        ft.replace(android.R.id.content, fragment).commit();
                        return true;
                    case R.id.mEdit:
                        //mTextMessage.setText(R.string.Batman);
                        EditFragment fragment2 = new EditFragment();
                        ft.replace(android.R.id.content, fragment2).commit();
                        return true;
                    case R.id.mCheck:
                        //mTextMessage.setText(R.string.Flash);
                        CheckFragment fragment3 = new CheckFragment();
                        ft.replace(android.R.id.content, fragment3).commit();
                        return true;
                    case R.id.mProfile:
                        //mTextMessage.setText(R.string.Flash);
                        PerfilFragment fragment4 = new PerfilFragment();
                        fragment4.setArguments(arg);
                        ft.addToBackStack("nombre");
                        ft.addToBackStack("correo");
                        ft.addToBackStack("telefono");
                        ft.addToBackStack("foto");
                        ft.addToBackStack("id");

                        ft.replace(android.R.id.content, fragment4).commit();
                        return true;
                }
                return false;
            }
        });


        EventosFragment fragmentini = new EventosFragment();
        ft.replace(android.R.id.content, fragmentini).commit();
    }
    private void inicializar() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                  //  Toast.makeText(PrincipalActivity.this, "Usuario logeado"+firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    Log.d("FirebaseUser", "Usuario Logeado: "+firebaseUser.getDisplayName());

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                                    Usuarios usuarios = snapshot.getValue(Usuarios.class);
                                    if (usuarios.getCorreo().equals(firebaseUser.getEmail())) {
                                        arg.putString("nombre", usuarios.getNombre());
                                        arg.putString("correo", usuarios.getCorreo());
                                        arg.putString("telefono", usuarios.getTelefono());
                                        arg.putString("foto", usuarios.getFoto());
                                        arg.putString("id", usuarios.getId());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    /*arg.putString("nombre", firebaseUser.getDisplayName());


                    arg.putString("correo", firebaseUser.getEmail());
                    arg.putString("telefono", firebaseUser.getPhoneNumber());
                    arg.putString("foto", firebaseUser.getPhotoUrl().toString());
                    */

                }else{
                    //Toast.makeText(PrincipalActivity.this, "Usuario no Logeado: "+firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    Log.d("FirebaseUser", "No hay usuario logeado ");
                    goLoginActivity();

                }
            }
        };

        //Iniciar cuenta de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleApiClient  = new GoogleApiClient.Builder(this).
                enableAutoManage(this, this).
                addApi(Auth.GOOGLE_SIGN_IN_API, gso).
                build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mCrearEvento){
            Intent intent = new Intent(PrincipalActivity.this, CrearEvento_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void goLoginActivity(){
        Intent intent2 = new Intent(PrincipalActivity.this, LogginActivity.class);
        startActivity(intent2);
        finish();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
        googleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    FutbolkFragment tab1 = new FutbolkFragment();
                    return tab1;
                case 1:
                    BasquetbolFragment tab2 = new BasquetbolFragment();
                    return tab2;
                case 2:
                    FSalaFragment tab3 = new FSalaFragment();
                    return tab3;
                case 3:
                    VoleibolFragment tab4 = new VoleibolFragment();
                    return tab4;
                default:
                    return null;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Fútbol";
                case 1:
                    return "Básquetbol";
                case 2:
                    return "F. Sala";
                case 3:
                    return "Voleibol";
            }
            return super.getPageTitle(position);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;

        }
    }

    public void setActionBarTitle(Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
    }


    public void OnClickButton_Cerrar(View view) {
        Toast.makeText(this, "Cerrar Sesión presionado", Toast.LENGTH_SHORT).show();
        firebaseAuth.signOut();
        if(Auth.GoogleSignInApi != null) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        goLoginActivity();
                    } else {
                        Toast.makeText(PrincipalActivity.this, "Error cerrando sesion con Google", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if(LoginManager.getInstance() != null){
            LoginManager.getInstance().logOut();
            goLoginActivity();
        }else{
            goLoginActivity();
        }

    }


}
