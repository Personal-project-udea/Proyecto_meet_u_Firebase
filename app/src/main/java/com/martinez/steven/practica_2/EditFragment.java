package com.martinez.steven.practica_2;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.martinez.steven.practica_2.model.Eventos;
import com.martinez.steven.practica_2.model.EventosUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private android.support.v7.widget.RecyclerView recyclerView;
    private RecyclerView.Adapter adapterEventos;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Eventos> eventoslist;
    private ArrayList<String> eventosuserslist;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;;

    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);


        ((PrincipalActivity) getActivity())
                .setActionBarTitle(toolbar);

        recyclerView = view.findViewById(R.id.vRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new android.support.v7.widget.LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        eventoslist = new ArrayList<>();
        eventosuserslist = new ArrayList<>();

        adapterEventos = new Adapter_eventos(eventoslist, R.layout.cardview_default, getActivity());

        recyclerView.setAdapter(adapterEventos);

        //buscando los eventos donde el usuario es creador

        final ArrayList lista = eventosEdit();

        try
        {
            Thread.sleep(100);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventoslist.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Eventos eventos = snapshot.getValue(Eventos.class);
                        for (int i = 0; i < lista.size(); i++) {
                            if (eventos.getId().equals(lista.get(i))) {
                                eventoslist.add(eventos);
                            }
                        }
                    }
                    adapterEventos.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;

    }

    public ArrayList eventosEdit(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final String id1 = firebaseUser.getUid();


        databaseReference.child("EventosUsuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventosuserslist.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        EventosUsuarios eventosusers = snapshot.getValue(EventosUsuarios.class);
                        if (eventosusers.getCreadorid().equals(id1)){
                            String eventid = eventosusers.getEventoid();
                            eventosuserslist.add(eventid);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return eventosuserslist;
    }
}

