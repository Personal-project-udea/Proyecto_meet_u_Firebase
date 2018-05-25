package com.martinez.steven.practica_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.martinez.steven.practica_2.model.Eventos;
import com.martinez.steven.practica_2.model.EventosUsuarios;
import com.martinez.steven.practica_2.model.Usuarios;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditEventActivity extends AppCompatActivity {

    private EditText tFecha, tLugar, tDescripcion;
    private TextView tNumeroAsistentes, tAnfitrion;
    private ImageView iDeporte;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> listAsistent;
    private Bundle extras;
    private String id;
    private Eventos evento;
    private EventosUsuarios eventousers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);


        extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getString("id");
        }
        tFecha = findViewById(R.id.tFecha);
        tLugar = findViewById(R.id.tLugar);
        tDescripcion = findViewById(R.id.tDescripcion);
        tNumeroAsistentes = findViewById(R.id.tNumeroAsistentes);
        tAnfitrion = findViewById(R.id.tAnfitrion);
        iDeporte = findViewById(R.id.iDeporte);

        listAsistent = new ArrayList<>();

        listAsistent.clear();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Eventos eventos = snapshot.getValue(Eventos.class);
                        if(eventos.getId().equals(id)) {
                            evento = eventos;
                            tDescripcion.setText(evento.getDescripcion());
                            tFecha.setText((evento.getFecha() + ", " + evento.getHora()));
                            tLugar.setText(evento.getCancha());
                            if (evento.getDeporte().equals("Fútbol")) {
                                iDeporte.setImageResource(R.drawable.futbol);
                            } else if (evento.getDeporte().equals("Básquetbol")) {
                                iDeporte.setImageResource(R.drawable.basquetbol);
                            } else if (evento.getDeporte().equals("Voleibol")) {
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
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        EventosUsuarios eventosusers = snapshot.getValue(EventosUsuarios.class);
                        if (eventosusers.getEventoid().equals(id)){
                            eventousers = eventosusers;
                            String asistentes[] = eventousers.getUsersins().split(",");
                            tNumeroAsistentes.setText(tNumeroAsistentes.getText()+ String.valueOf(asistentes.length));

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
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios user = snapshot.getValue(Usuarios.class);
                        if (user.getId().equals(eventousers.getCreadorid())){
                            tAnfitrion.setText(user.getNombre());
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

}
