package com.martinez.steven.practica_2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.martinez.steven.practica_2.model.Usuarios;
import com.squareup.picasso.Picasso;

public class PerfilActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    String pwd = "", email = "", user = "";
    Bundle extras;
    EditText eUsuario , eCorreo, eTelefono;
    ImageView iFoto;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        eUsuario = findViewById(R.id.eUsuario);
        eCorreo = findViewById(R.id.eCorreo);
        iFoto = findViewById(R.id.iFoto);
        eTelefono = findViewById(R.id.eTelefono);

        extras = getIntent().getExtras();
        if(extras != null) {
            user = extras.getString("usuario");
            pwd = extras.getString("password");
            email = extras.getString("correo");
            eUsuario.setText(user);
            eCorreo.setText(email);
        }
        FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("value", "oncreate 2");

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
                    eUsuario.setText(firebaseUser.getDisplayName());
                    eTelefono.setText(firebaseUser.getPhoneNumber());
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
            onBackPressed();



        }else if(id == R.id.mLogout){
            Toast.makeText(this, "Cerrar Sesión presionado", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            if(Auth.GoogleSignInApi != null) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            goLoginActivity();
                        } else {
                            Toast.makeText(PerfilActivity.this, "Error cerrando sesion con Google", Toast.LENGTH_SHORT).show();
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

    public void OnClickButton_Guardar(View view) {
        Log.d("button", "Entra al boton ");
        Usuarios usuarios = new Usuarios(databaseReference.child("Usuarios").push().getKey(),
                eUsuario.getText().toString(),
                eTelefono.getText().toString(),
                eCorreo.getText().toString(),
                "url foto");
        Log.d("button", "Entra al boton marca 1 ");

        databaseReference.child("Usuarios").child(usuarios.getId()).setValue(usuarios);
        Log.d("button", "Entra al boton marca 2 ");
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
                        Toast.makeText(PerfilActivity.this, "Error cerrando sesion con Google", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
