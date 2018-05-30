package com.martinez.steven.practica_2;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.martinez.steven.practica_2.model.EventosUsuarios;
import com.martinez.steven.practica_2.model.Usuarios;

import java.util.ArrayList;

public class Adapter_EvenUsers extends RecyclerView.Adapter<Adapter_EvenUsers.EvenUsersViewholdes> {
    private ArrayList<EventosUsuarios> evenusersList;
    private int resource;
    private Activity activity;

    public Adapter_EvenUsers(ArrayList<EventosUsuarios> evenusersList) {
        this.evenusersList = evenusersList;
    }

    public Adapter_EvenUsers(ArrayList<EventosUsuarios> evenusersList, int resource, Activity activity) {
        this.evenusersList = evenusersList;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public EvenUsersViewholdes onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(activity, "EvenUsers", Toast.LENGTH_SHORT).show();

            }
        });

        return new EvenUsersViewholdes(itemview);
    }

    @Override
    public void onBindViewHolder(EvenUsersViewholdes holder, int position) {
        EventosUsuarios info_evenusers = evenusersList.get(position);
        holder.binInfoevento(info_evenusers, activity);

    }

    @Override
    public int getItemCount() {
        return evenusersList.size();
    }


    public class EvenUsersViewholdes extends RecyclerView.ViewHolder{
        private TextView tCreador, tEventoid, tUserins;

        public EvenUsersViewholdes(View itemView) {
            super(itemView);
            tCreador = itemView.findViewById(R.id.tCreadorid);
            tEventoid = itemView.findViewById(R.id.tEventoid);
            tUserins = itemView.findViewById(R.id.tUserins);
        }


        public void binInfoevento(EventosUsuarios info_evenusers, Activity activity) {
            tCreador.setText(info_evenusers.getCreadorid());
            tEventoid.setText(info_evenusers.getEventoid());
            tUserins.setText(info_evenusers.getUsersins());

        }
    }



}