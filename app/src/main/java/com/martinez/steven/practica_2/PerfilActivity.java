package com.martinez.steven.practica_2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class PerfilActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    String pwd = "", email = "", user = "";
    Bundle extras;
    EditText eUsuario , eCorreo;
    ImageView iFoto;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        eUsuario = findViewById(R.id.eUsuario);
        eCorreo = findViewById(R.id.eCorreo);
        iFoto = findViewById(R.id.iFoto);

        extras = getIntent().getExtras();
        if(extras != null) {
            user = extras.getString("usuario");
            pwd = extras.getString("password");
            email = extras.getString("correo");
            eUsuario.setText(user);
            eCorreo.setText(email);
        }

    inicializar();

    }

    private void inicializar() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.d("FirebaseUser", "Usuario Logeado: "+firebaseUser.getDisplayName());
                    eCorreo.setText(firebaseUser.getEmail());
                    Picasso.get().load(firebaseUser.getPhotoUrl()).into(iFoto);
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
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mPrincipal){
            Toast.makeText(this, "Pprincipal presionado", Toast.LENGTH_SHORT).show();
            /*Intent intent1 = new Intent();
            intent1.putExtra("usuario",user);
            intent1.putExtra("password", pwd );
            intent1.putExtra("correo", email);
            //setResult(RESULT_OK, intent1);
            //startActivityForResult(intent1, 0100);
            startActivity(intent1);*/
            onBackPressed();



        }else if(id == R.id.mLogout){
            firebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()){
                        goLoginActivity();
                    }else{
                        Toast.makeText(PerfilActivity.this, "Error cerrando sesion con Google", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    private void goLoginActivity(){
        Intent intent2 = new Intent(PerfilActivity.this, LogginActivity.class);
        startActivity(intent2);
        finish();
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
