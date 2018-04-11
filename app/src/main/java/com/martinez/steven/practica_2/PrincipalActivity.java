package com.martinez.steven.practica_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class PrincipalActivity extends AppCompatActivity {

    String user = "", pwd = "", email = "";
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        extras = getIntent().getExtras();

        if (extras != null) {
            user = extras.getString("usuario");
            pwd = extras.getString("password");
            email = extras.getString("correo");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mProfile){
            Toast.makeText(this, "Perfil presionado", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(PrincipalActivity.this, PerfilActivity.class);
            intent1.putExtra("usuario",user);
            intent1.putExtra("password", pwd );
            intent1.putExtra("correo", email);
            //setResult(RESULT_OK, intent1);
            //startActivityForResult(intent1, 0011);
            startActivity(intent1);
            //onBackPressed();



        }else if(id == R.id.mLogout){
            Toast.makeText(this, "Cerrar Sesión presionado", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(PrincipalActivity.this, LogginActivity.class);
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
