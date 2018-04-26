package com.martinez.steven.practica_2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.martinez.steven.practica_2.model.Usuarios;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class PruebaActivity extends AppCompatActivity {

    EditText enombre, ecorreo, etelefono;
    ListView Listview;
    ArrayList<String> nombrelist;
    ArrayList<Usuarios> usuarioslist;
    ArrayAdapter arrayAdapter;
    String urlFoto="No ha cargado foto";
    String url = "https://firebasestorage.googleapis.com/v0/b/proyectomeetu.appspot.com/o/logom.png?alt=media&token=05873879-54e0-44b6-b199-9f7eee6e5a0b";
    ImageView iPhoto;
    private Bitmap bitmap;


    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        ecorreo = findViewById(R.id.ecorreo);
        etelefono = findViewById(R.id.etele);
        enombre = findViewById(R.id.enombre);
        Listview = findViewById(R.id.Listview);
        iPhoto  = findViewById(R.id.iPhoto);

        Picasso.get().load(url).into(iPhoto);

        //nombrelist = new ArrayList<>();
        usuarioslist = new ArrayList<>();

        //arrayAdapter = new ArrayAdapter(PruebaActivity.this, android.R.layout.simple_list_item_1, nombrelist);

        final UsuarioAdapter usuarioAdapter = new UsuarioAdapter(this, usuarioslist);
        Listview.setAdapter(usuarioAdapter);
        //Listview.setAdapter(arrayAdapter);
        Log.d("value", "oncreate 1");

        FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("value", "oncreate 2");

        databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioslist.clear();
                //nombrelist.clear();
                Log.d("value", "Clear");
                if (dataSnapshot.exists()) {
                    Log.d("value", "no null");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("value", "for");
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        Log.d("value", "usr");
                        String nombre = (String) usuarios.getNombre();
                        Log.d("value", "getname");
                        //nombrelist.add(nombre);
                        usuarioslist.add(usuarios);
                        Log.d("value", "fin");

                    }
                }
                //arrayAdapter.notifyDataSetChanged();
                usuarioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                String uid = usuarioslist.get(position).getId();
                usuarioslist.remove(position);
                nombrelist.remove(position);
                databaseReference.child("Usuarios").child(uid).removeValue();


                return false;
            }
        });


    }

    public void OnClickGuardar(View view) {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,baos);
        byte[] data = baos.toByteArray();

        storageReference.child("usuarios").child(databaseReference.push().getKey()).
                putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                urlFoto = taskSnapshot.getDownloadUrl().toString();

            }
        });



        Log.d("button", "Entra al boton ");
        Usuarios usuarios = new Usuarios(databaseReference.child("Usuarios").push().getKey(),
                enombre.getText().toString(),
                etelefono.getText().toString(),
                ecorreo.getText().toString(),
                urlFoto);
        Log.d("button", "Entra al boton marca 1 ");

        databaseReference.child("Usuarios").child(usuarios.getId()).setValue(usuarios);
        Log.d("button", "Entra al boton marca 2 ");

    }

    public void onClickImage(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, 1234);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==1234 && resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(this,"Error Cargando Imagen", Toast.LENGTH_SHORT);
            } else {
                Uri imagen = data.getData();
                try{
                    InputStream is = getContentResolver().openInputStream(imagen);
                    BufferedInputStream  bis = new BufferedInputStream(is);
                    bitmap = BitmapFactory.decodeStream(is);
                    iPhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    class UsuarioAdapter extends ArrayAdapter<Usuarios> {
        public UsuarioAdapter(@NonNull Context context, ArrayList<Usuarios> data) {
            super(context, R.layout.list_item, data);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(getContext());

            View item = inflater.inflate(R.layout.list_item, null);

            Usuarios usuarios = getItem(position);

            TextView nombre  = item.findViewById(R.id.tNombre);
            nombre.setText(usuarios.getNombre());

            TextView correo  = item.findViewById(R.id.tCorreo);
            correo.setText(usuarios.getCorreo());

            TextView telefono  = item.findViewById(R.id.tTelefono);
            telefono.setText(usuarios.getTelefono());
            return item;
        }
    }
}
