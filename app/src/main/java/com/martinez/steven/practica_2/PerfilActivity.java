package com.martinez.steven.practica_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class PerfilActivity extends AppCompatActivity {

    String pwd = "", email = "", user = "";
    Bundle extras;
    EditText eUsuario , eCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        eUsuario = findViewById(R.id.eUsuario);
        eCorreo = findViewById(R.id.eCorreo);

        extras = getIntent().getExtras();
        if(extras != null) {
            user = extras.getString("usuario");
            pwd = extras.getString("password");
            email = extras.getString("correo");
            eUsuario.setText(user);
            eCorreo.setText(email);
        }



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
            Toast.makeText(this, "Cerrar Sesión presionado", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(PerfilActivity.this, LogginActivity.class);
            intent2.putExtra("usuario",user);
            intent2.putExtra("password", pwd );
            intent2.putExtra("correo", email);
            //setResult(RESULT_OK, intent2);
            startActivity(intent2);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
