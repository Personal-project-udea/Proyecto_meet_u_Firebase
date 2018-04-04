package com.martinez.steven.practica_2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LogginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    private SignInButton btnSingInGoogle;
    int LOGIN_CON_GOOGLE = 1;


    EditText eUser, ePassword;
    TextView tRegistro;
    ImageButton bLoggin;
    String user = "", pwd = "", email = "";
    Bundle extras;
    String epwd = "", euser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);

        eUser = findViewById(R.id.eUser);
        ePassword = findViewById(R.id.ePassword);
        tRegistro = findViewById(R.id.tRegisto);
        bLoggin = findViewById(R.id.bLoggin);
        extras = getIntent().getExtras();
        btnSingInGoogle = findViewById(R.id.btnSingInGoogle);

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
                    Log.d("FirebaseUser", "Usuario Logeado: "+firebaseUser.getEmail());
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

        btnSingInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(i, LOGIN_CON_GOOGLE);
            }
        });

    }

    public void OnClickButton_Loggin(View view) {
        int id  = view.getId();

        if (id == R.id.bLoggin){

            iniciarsesion(eUser.getText().toString(), ePassword.getText().toString());
        }
    }

    public void OnClickText_Registro(View view) {
        int id = view.getId();
        if (id  == R.id.tRegisto){
            // CAMBIANDO DEL LOGGIN AL REGISTRO CUANDO NO SE HA REGISTRADO
            Intent intent = new Intent(LogginActivity.this, RegistroActivity.class);
            startActivity(intent);


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_CON_GOOGLE) {
            GoogleSignInResult googleSingInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            singInGoogle(googleSingInResult);
        }

    }


    private void iniciarsesion(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            goMainActivity();
                            Toast.makeText(LogginActivity.this, "Inicio correcto", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogginActivity.this, "Error al iniciar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void singInGoogle (GoogleSignInResult googleSingInResult){

        if(googleSingInResult.isSuccess()){
            AuthCredential authCredential = GoogleAuthProvider.getCredential(
                    googleSingInResult.getSignInAccount().getIdToken(), null);
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    goMainActivity();
                }
            });
        }else {
            Toast.makeText(LogginActivity.this, "Autenticación con google no exitosa",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainActivity(){
        Intent i = new Intent(LogginActivity.this, PirncipalActivity.class);
        startActivity(i);
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
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

