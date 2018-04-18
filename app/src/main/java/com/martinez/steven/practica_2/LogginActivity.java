package com.martinez.steven.practica_2;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.martinez.steven.practica_2.model.Usuarios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class LogginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    private SignInButton btnSingInGoogle;
    int LOGIN_CON_GOOGLE = 1;
    private LoginButton btnSingInFacebook;
    private CallbackManager callbackManager;


    EditText eUser, ePassword;
    TextView tRegistro;
    ImageButton bLoggin;
    String user = "", pwd = "", email = "";
    Bundle extras;
    String epwd = "", euser = "";

    ArrayList<String> nombrelist;
    ArrayList<Usuarios> usuarioslist;

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
        btnSingInFacebook = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        nombrelist = new ArrayList<>();
        usuarioslist = new ArrayList<>();


        inicializar();
        getHashes();
        btnSingInFacebook.setReadPermissions("email","public_profile");

        // Callback registration
        btnSingInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login Facebook", "Ok");
                singInFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Login Facebook", "Cancelado por el Usuario");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("Login Facebook", "Error");
            }
        });


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
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
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

    private void singInFacebook(AccessToken accessToken){
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    goMainActivity();
                }else{
                    Toast.makeText(LogginActivity.this, "Autenticación con Facebook no exitosa",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goMainActivity(){
        crearUsuario();
        Intent i = new Intent(LogginActivity.this,  PruebaActivity.class);

        //PirncipalActivity.class);

        startActivity(i);
        finish();
    }

    private void crearUsuario() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Usuarios").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usuarioslist.clear();
                        nombrelist.clear();
                        Log.d("value", "Clear");
                        if(dataSnapshot.exists()){

                            Log.d("Existe", "SI");
                        }else{
                            Log.d("Existe", "NO");
                            Usuarios usuarios = new Usuarios(firebaseUser.getUid(),
                                    firebaseUser.getDisplayName(),
                                    firebaseUser.getPhoneNumber(),
                                    firebaseUser.getEmail(),
                                    "url photo");

                            Log.d("button", "Entra al boton marca 1 ");

                            databaseReference.child("Usuarios").child(usuarios.getId()).setValue(usuarios);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    public void getHashes() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.martinez.steven.practica_2",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}

