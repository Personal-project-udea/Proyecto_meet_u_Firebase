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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        extras = getIntent().getExtras();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        if (extras != null) {
            user = extras.getString("usuario");
            pwd = extras.getString("password");
            email = extras.getString("correo");
        }

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
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.d("FirebaseUser", "Usuario Logeado: "+firebaseUser.getDisplayName());
                }else{
                    Log.d("FirebaseUser", "No hay usuario logeado ");
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

        if (id == R.id.mProfile){
            Toast.makeText(this, "Perfil presionado", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(PrincipalActivity.this, PerfilActivity.class);
            intent1.putExtra("usuario",user);
            intent1.putExtra("password", pwd );
            intent1.putExtra("correo", email);
            //setResult(RESULT_OK, intent1);
            //startActivityForResult(intent1, 0011);
            startActivity(intent1);
            //onBackPressed();



        }else if(id == R.id.mLogout){
            Toast.makeText(this, "Cerrar Sesión presionado", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(PrincipalActivity.this, LogginActivity.class);
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
            /*Intent intent2 = new Intent(PirncipalActivity.this, LogginActivity.class);
            intent2.putExtra("usuario",user);
            intent2.putExtra("password", pwd );
            intent2.putExtra("correo", email);
            //setResult(RESULT_OK, intent2);
            startActivity(intent2);
            finish();*/

        }
        return super.onOptionsItemSelected(item);
    }
    private void goLoginActivity(){
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

}
