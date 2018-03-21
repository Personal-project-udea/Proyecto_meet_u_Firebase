package com.martinez.steven.practica_2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

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

        if (extras != null){
            user = extras.getString("usuario");
            pwd = extras.getString("password");
            email = extras.getString("correo");
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
                    Log.d("FirebaseUser", "Usuario Logeado: "+firebaseUser.getEmail());
                }else{
                    Log.d("FirebaseUser", "No hay usuario logeado ");
                }
            }
        };
    }
    public void OnClickButton_Loggin(View view) {
        inciarsesion(eUser.getText().toString(),ePassword.getText().toString());
    }

    private void inciarsesion(String correo, String contrasena){
        firebaseAuth.signInWithEmailAndPassword(correo, contrasena).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            goPrincipalActivity();
                        }else{
                            Toast.makeText(LogginActivity.this,"Error al iniciar secion",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goPrincipalActivity() {
        Intent i = new Intent(LogginActivity.this, PirncipalActivity.class);
        startActivity(i);
        finish();
    }
    public void OnClickText_Registro(View view) {
        int id = view.getId();
        if (id  == R.id.tRegisto){
            // CAMBIANDO DEL LOGGIN AL REGISTRO CUANDO NO SE HA REGISTRADO
            Intent intent = new Intent(LogginActivity.this, RegistroActivity.class);
            startActivityForResult(intent, 1000);
            //finish();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == RESULT_OK){
            user = data.getExtras().getString("usuario");
            pwd = data.getExtras().getString("password");
            email = data.getExtras().getString("correo");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //coment
}
