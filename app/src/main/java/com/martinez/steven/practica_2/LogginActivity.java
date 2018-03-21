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

        /*if (extras != null){
            user = extras.getString("usuario");
            pwd = extras.getString("password");
            email = extras.getString("correo");
        }*/

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
        int id  = view.getId();

        if (id == R.id.bLoggin){
            /*if (!ePassword.getText().toString().isEmpty() && !eUser.getText().toString().isEmpty()){
                euser = eUser.getText().toString();
                epwd = ePassword.getText().toString();
                Log.d("Variables", euser + epwd + user + email + pwd);
                if (euser.equals(user) || euser.equals(email)){
                    if (epwd.equals(pwd)) {
                        // CAMBIANDO DEL LOGGIN AL PERFIL TRAS REALIZAR EL LOGGIN
                        Intent intent = new Intent(LogginActivity.this, PirncipalActivity.class);
                        intent.putExtra("usuario", user);
                        intent.putExtra("password", pwd);
                        intent.putExtra("correo", email);
                        //startActivityForResult(intent, 0010);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(LogginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    eUser.setText("");
                    ePassword.setText("");
                }
            }else{
                Toast.makeText(LogginActivity.this, "Llene los campos", Toast.LENGTH_SHORT).show();
            }*/
            iniciarsesion(eUser.getText().toString(), ePassword.getText().toString());
        }
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


    private void iniciarsesion(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            goMainActivity();
                        } else {
                            Toast.makeText(LogginActivity.this, "Error al iniciar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goMainActivity(){
        Intent i = new Intent(LogginActivity.this, PirncipalActivity.class);
        startActivity(i);
        finish();
    }
}
