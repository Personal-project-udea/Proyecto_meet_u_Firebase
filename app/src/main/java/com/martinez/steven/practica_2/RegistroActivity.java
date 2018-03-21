package com.martinez.steven.practica_2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    EditText erUser, erEmail1, erEmail2, erPass1, erPass2;
    ImageButton bRegistrar;
    String email1, email2, pass1, pass2, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        erUser = findViewById(R.id.erUser);
        erEmail1 = findViewById(R.id.erEmail1);
        erEmail2 = findViewById(R.id.erEmail2);
        erPass1 = findViewById(R.id.erPass1);
        erPass2 = findViewById(R.id.erPass2);
        bRegistrar = findViewById(R.id.bRegistrar);

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

    public void OnClickButton_Registrar(View view) {
        int id  = view.getId();
        //String email1, email2, pass1, pass2, user;
        if (id == R.id.bRegistrar){
            if (!erUser.getText().toString().isEmpty() && !erEmail1.getText().toString().isEmpty() &&
                    !erEmail2.getText().toString().isEmpty() && !erPass1.getText().toString().isEmpty() &&
                    !erPass2.getText().toString().isEmpty()){
                email1 = erEmail1.getText().toString();
                email2 = erEmail2.getText().toString();
                pass1 = erPass1.getText().toString();
                pass2 = erPass2.getText().toString();
                user = erUser.getText().toString();

                if (email1.equals(email2) && pass1.equals(pass2)){
                    Intent intent = new Intent();
                    intent.putExtra("usuario", user);
                    intent.putExtra("password", pass1);
                    intent.putExtra("correo", email1);

                    crearCuenta(email1,pass1);

                    setResult(RESULT_OK, intent);
                    //startActivity(intent);
                    Toast.makeText(RegistroActivity.this, "Registro completo", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegistroActivity.this, "Revise los campos. Informacion no coincide", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegistroActivity.this, "Complete los campos vacios", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crearCuenta(String email1, String pass1) {
        firebaseAuth.createUserWithEmailAndPassword(email1, pass1).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegistroActivity.this, "Cuenta Creada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistroActivity.this, "Error al crear", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
