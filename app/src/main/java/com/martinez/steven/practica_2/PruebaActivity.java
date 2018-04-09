package com.martinez.steven.practica_2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.martinez.steven.practica_2.model.Usuarios;

public class PruebaActivity extends AppCompatActivity {

    private EditText eNombre, eTelefono, eCorreo;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        eNombre = findViewById(R.id.eNombre);
        eCorreo = findViewById(R.id.eCorreo);
        eTelefono = findViewById(R.id.eTelefono);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    public void bGuardar(View view) {
        Usuarios usuarios = new Usuarios (databaseReference.push().getKey(),
                eNombre.getText().toString(),
                eTelefono.getText().toString(),
                eCorreo.getText().toString(),
                 "url Foto");

        databaseReference.child("Usuaris").child(usuarios.getId()).setValue(usuarios);
    }
}
