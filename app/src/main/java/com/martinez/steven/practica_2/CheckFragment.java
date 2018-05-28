package com.martinez.steven.practica_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.martinez.steven.practica_2.model.Eventos;
import com.martinez.steven.practica_2.model.EventosUsuarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterEvenUsers;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Eventos> eventoslist;
    private ArrayList<String> eventosuserslist;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    public ArrayList<String> lista;
    private int select = 0;

    public CheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        lista = new ArrayList<>();
        lista.clear();
        ((PrincipalActivity) getActivity())
                .setActionBarTitle(toolbar);

        recyclerView = view.findViewById(R.id.vRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        eventoslist = new ArrayList<>();
        eventosuserslist = new ArrayList<>();

        adapterEvenUsers = new Adapter_eventos(eventoslist, R.layout.cardview_default, getActivity(), select);

        recyclerView.setAdapter(adapterEvenUsers);

        lista = eventosEdit();

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

        mostrarEvento();

        return view;


        //todo full

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
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        EventosUsuarios eventosusers = snapshot.getValue(EventosUsuarios.class);
                        String s = eventosusers.getUsersins();
                        List<String> myList = new ArrayList<String>(Arrays.asList(s.split(",")));
                        for (int j = 0; j < myList.size(); j++) {
                            if ((myList.get(j)).equals(id1)) {
                                String eventid = eventosusers.getEventoid();
                                Log.d("paso", eventid);
                                eventosuserslist.add(eventid);
                            }
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

    public void mostrarEvento(){

        databaseReference.child("Eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventoslist.clear();
                Log.d("paso", "1");

                if (dataSnapshot.exists()) {
                    Log.d("paso", "2");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("paso", "3");
                        Eventos eventos = snapshot.getValue(Eventos.class);
                        for (int i = 0; i < lista.size(); i++) {
                            Log.d("paso", "4"+ eventos.getId()+lista.get(i));
                            if (eventos.getId().equals(lista.get(i))) {
                                Log.d("paso", "5");
                                Log.d("paso", "6"+eventos.getId());
                                eventoslist.add(eventos);
                            }
                        }
                    }
                    Log.d("paso", "6"+String.valueOf(eventoslist));
                    adapterEvenUsers.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
