package com.martinez.steven.practica_2;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.martinez.steven.practica_2.model.Eventos;
import com.martinez.steven.practica_2.model.Usuarios;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    private DatabaseReference databaseReference;

    private Bitmap bitmap;
    private Button button;

    EditText eUsuario , eCorreo, eTelefono;
    ImageView iFoto;

    View view;

    private String urlFoto = "No ha cargado", id;


    //---------------------------usuarios
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterUsuarios;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Usuarios> userslist;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference1;

    //-------------------------------------

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);


        eUsuario = view.findViewById(R.id.eUsuario);
        eCorreo = view.findViewById(R.id.eCorreo);
        iFoto = view.findViewById(R.id.iFoto);
        eTelefono = view.findViewById(R.id.eTelefono);

        FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("value", "oncreate 2");

        Bundle bundle = getArguments();
        eCorreo.setEnabled(false);

        eUsuario.setText(bundle.getString("nombre"));
        eCorreo.setText(bundle.getString("correo"));
        eTelefono.setText(bundle.getString("telefono"));
        urlFoto = bundle.getString("foto");
        id = bundle.getString("id");
        Picasso.get().load(bundle.getString("foto")).into(iFoto);


        Button button2 = (Button) view.findViewById(R.id.bCerrar);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("click", "boton cerrar");
                ((PrincipalActivity) getActivity()).OnClickButton_Cerrar(view);
            }
        });

        ImageView Foto = (ImageView) view.findViewById(R.id.iFoto);
        Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, 1234);
            }
        });

        button = (Button) view.findViewById(R.id.bSave);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("click", "boton guardar");

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference();
                Log.d("click", "boton1");
                ByteArrayOutputStream baos = new ByteArrayOutputStream(); //comprimir
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                Log.d("click", "boton2");
                storageReference.child("Usuarios").child(databaseReference.push().getKey()).putBytes(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("click", "boton3");
                                urlFoto = taskSnapshot.getDownloadUrl().toString();
                                savedatabase();
                            }
                        });
            }
        });


        //--------------usuarios

        Log.d("value", "oncreate 3");
        recyclerView = view.findViewById(R.id.vRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        userslist = new ArrayList<Usuarios>();
        Log.d("value", "oncreate 4");
        adapterUsuarios = new Adapter_usuarios(userslist, R.layout.users_card, getActivity());

        recyclerView.setAdapter(adapterUsuarios);
        Log.d("value", "oncreate 5");
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        Log.d("value", "oncreate 5.4");
        databaseReference1.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("value", "oncreate 5.5");
                userslist.clear();
                if (dataSnapshot.exists()){
                    Log.d("value", "oncreate 6");
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);

                        userslist.add(usuarios);


                    }
                    adapterUsuarios.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //---------------------

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234 && resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(getActivity(),"error cargando imagen", Toast.LENGTH_SHORT).show();

            }else{
                Log.d("click", "imagen");
                Uri imagen = data.getData();
                try {
                    button.setEnabled(true);
                    Log.d("click", "imagen1");
                    InputStream is = ((PrincipalActivity) getActivity()).getContentResolver().openInputStream(imagen);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bitmap = BitmapFactory.decodeStream(bis);
                    iFoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void savedatabase(){
        /*Log.d("button", "Entra al boton ");
        Usuarios usuarios = new Usuarios(databaseReference.child("Usuarios").push().getKey(),
                eUsuario.getText().toString(),
                eTelefono.getText().toString(),
                eCorreo.getText().toString(),
                urlFoto);
        Log.d("button", "Entra al boton marca 1 ");

        //databaseReference.child("Usuarios").child(usuarios.getId()).setValue(usuarios);
        //Log.d("button", "Entra al boton marca 2 ");


        databaseReference.child("Usuarios").child(usuarios.getId()).setValue(usuarios).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Entre1", "OK");

                } else {
                    Log.d("Entre2", "OK");
                    Log.d("Save:", task.getException().toString());
                }
            }
        });
        Log.d("Entre3", "OK");

        */
        databaseReference.child("Usuarios").child(id).child("nombre").setValue(eUsuario.getText().toString());
        databaseReference.child("Usuarios").child(id).child("telefono").setValue(eTelefono.getText().toString());
        databaseReference.child("Usuarios").child(id).child("foto").setValue(urlFoto);
        Log.d("Enter4", "ok4");



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


}
