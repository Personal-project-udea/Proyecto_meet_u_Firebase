package com.martinez.steven.practica_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.martinez.steven.practica_2.model.Eventos;
import com.martinez.steven.practica_2.model.EventosUsuarios;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterEvenUsers;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<EventosUsuarios> evenuserslist;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    public CheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);


        ((PrincipalActivity) getActivity())
                .setActionBarTitle(toolbar);

        recyclerView = view.findViewById(R.id.vRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        evenuserslist = new ArrayList<EventosUsuarios>();

        adapterEvenUsers = new Adapter_EvenUsers(evenuserslist, R.layout.card_evenusers, getActivity());

        recyclerView.setAdapter(adapterEvenUsers);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("EventosUsuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                evenuserslist.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        EventosUsuarios evenusers = snapshot.getValue(EventosUsuarios.class);
                        evenuserslist.add(evenusers);

                    }
                    adapterEvenUsers.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;


        //todo full

    }

}
