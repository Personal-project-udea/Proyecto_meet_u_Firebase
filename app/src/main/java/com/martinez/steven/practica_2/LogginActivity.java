package com.martinez.steven.practica_2;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LogginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private GoogleApiClient googleApiClient;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private SignInButton btnSingInGoogle;
    int LOGIN_CON_GOOGLE = 1;


    EditText eUser, ePassword;
    TextView tError;
    Button bRegistro, bLoggin;
    String user = "", pwd = "", email = "";
    Bundle extras;
    String epwd = "", euser = "";

    public LogginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);

        eUser = findViewById(R.id.eUser);
        ePassword = findViewById(R.id.ePassword);
        tError = findViewById(R.id.tError);
        bRegistro = findViewById(R.id.bRegisto);
        bLoggin = findViewById(R.id.bLoggin);
        extras = getIntent().getExtras();

        btnSingInGoogle = findViewById(R.id.btnSingInGoogle);

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login Facebook", "OK");
                signInFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Login Facebook", "Cancelado por el usuario");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Login Facebook", "Error");
            }
        });

        inicializar();
        getHashes();

    }

    private void signInFacebook(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.
                getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(authCredential).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    goMainActivity();
                    Toast.makeText(LogginActivity.this, "Inicio correcto", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogginActivity.this, "Error:"+task.getException(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void getHashes() {
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
            if (eUser.getText().toString().equals("")||ePassword.getText().toString().equals("")){
                tError.setText("Ingrese correo y contraseña");
            }else {
                iniciarsesion(eUser.getText().toString(), ePassword.getText().toString());
            }
        }
    }

    public void OnClick_bRegistro(View view) {
        int id = view.getId();
        if (id  == R.id.bRegisto){
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
                            Toast.makeText(LogginActivity.this, "Inicio correcto",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            tError.setText("Correo o contraseña incorrectos");
                            Toast.makeText(LogginActivity.this, "Error al iniciar",
                                    Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(LogginActivity.this, PrincipalActivity.class);
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

    public void onClickEditUsuario(View view) {
        tError.setText("");
        eUser.setText("");
        ePassword.setText("");
    }
}

