package com.martinez.steven.practica_2;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.martinez.steven.practica_2.model.Eventos;
import com.martinez.steven.practica_2.model.EventosUsuarios;
import com.martinez.steven.practica_2.model.Usuarios;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InfoEventActivity extends AppCompatActivity {

    TextView tDescripcion, tFecha, tLugar, tAnfitrion, tAsistentes;
    ImageView iDeporte;

    private Bundle extras;
    private String id;

    private Eventos evento;
    private EventosUsuarios eventosusers;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> listAsistent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_event);

        extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getString("id");
        }

        tDescripcion = findViewById(R.id.tDescripcion);
        tFecha = findViewById(R.id.tFecha);
        tLugar = findViewById(R.id.tLugar);
        tAnfitrion = findViewById(R.id.tAnfitrion);
        tAsistentes = findViewById(R.id.tNumeroAsistentes);
        iDeporte = findViewById(R.id.iDeporte);

        listAsistent = new ArrayList<>();
        listAsistent.clear();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Eventos eventos = snapshot.getValue(Eventos.class);
                        if(eventos.getId().equals(id)){
                            evento = eventos;
                            tDescripcion.setText(evento.getDescripcion());
                            tFecha.setText(evento.getFecha() + ", " + evento.getHora());
                            tLugar.setText(evento.getCancha());

                            if(evento.getDeporte().equals("Fútbol")){
                                iDeporte.setImageResource(R.drawable.futbol);
                            }else if (evento.getDeporte().equals("Básquetbol")){
                                iDeporte.setImageResource(R.drawable.basquetbol);
                            }else if (evento.getDeporte().equals("Voleibol")) {
                                iDeporte.setImageResource(R.drawable.voleibol);
                            } else if (evento.getDeporte().equals("Micro")) {
                                iDeporte.setImageResource(R.drawable.micro);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("EventosUsuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        EventosUsuarios eventosUsers = snapshot.getValue(EventosUsuarios.class);
                        if (eventosUsers.getEventoid().equals(id)){
                            eventosusers = eventosUsers;
                            String asistentes[] = eventosUsers.getUsersins().split(",");
                            tAsistentes.setText(String.valueOf(asistentes.length));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Usuarios user = snapshot.getValue(Usuarios.class);
                        if (user != null) {
                            String currentUserID = user.getId();
                            if(eventosusers !=null) {
                                String creadorid = eventosusers.getCreadorid();
                                if (currentUserID.equals(creadorid)) {
                                    tAnfitrion.setText(user.getNombre());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        try
        {
            Thread.sleep(500);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

    }

    public void onClickButton_Asistir(View view) {
        //Recupero el id del usuario logueado
        final String id1 = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("EventosUsuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        EventosUsuarios eventosUsers = snapshot.getValue(EventosUsuarios.class);
                        if (eventosUsers.getEventoid().equals(id)){
                            eventosusers = eventosUsers;
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String asistentes[] = eventosusers.getUsersins().split(",");
        int asist = Integer.parseInt(String.valueOf(asistentes.length));
        int flag = 0 ;
        for(int i = 0; i< asist; i++){
            if(asistentes[i].equals(id1)){
                flag = flag+1;
            }
        }
        if(flag==0){
            databaseReference.child("EventosUsuarios").child(eventosusers.getId()).child("usersins").setValue(eventosusers.getUsersins()+"," + id1);
            tAsistentes.setText("");
        }else{
            Toast.makeText(this, "Ya se ha inscrito", Toast.LENGTH_SHORT).show();
        }
    }
}