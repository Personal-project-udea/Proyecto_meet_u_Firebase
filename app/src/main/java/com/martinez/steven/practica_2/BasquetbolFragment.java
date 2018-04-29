package com.martinez.steven.practica_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.martinez.steven.practica_2.model.Eventos;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BasquetbolFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterEventos;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Eventos> eventoslist;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    public BasquetbolFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View itemView = inflater.inflate(R.layout.fragment_basquetbol, container, false);

        recyclerView = itemView.findViewById(R.id.vRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        eventoslist = new ArrayList<>();

        adapterEventos = new Adapter_eventos(eventoslist, R.layout.cardview_default, getActivity());

        recyclerView.setAdapter(adapterEventos);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventoslist.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Eventos eventos = snapshot.getValue(Eventos.class);
                        if (eventos.getDeporte().equals("Básquetbol")){
                            eventoslist.add(eventos);
                        }

                    }
                    adapterEventos.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return itemView;
    }


}
