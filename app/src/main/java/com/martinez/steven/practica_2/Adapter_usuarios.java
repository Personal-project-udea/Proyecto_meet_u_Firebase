package com.martinez.steven.practica_2;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martinez.steven.practica_2.model.Eventos;
import com.martinez.steven.practica_2.model.Usuarios;

import java.util.ArrayList;

public class Adapter_usuarios extends RecyclerView.Adapter<Adapter_usuarios.UsuariosViewholdes> {
    private ArrayList<Usuarios> usersList;
    private int resource;
    private Activity activity;

    public Adapter_usuarios(ArrayList<Usuarios> usersList) {
        this.usersList = usersList;
    }

    public Adapter_usuarios(ArrayList<Usuarios> usersList, int resource, Activity activity) {
        this.usersList = usersList;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public UsuariosViewholdes onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Usuario", Toast.LENGTH_SHORT).show();

            }
        });

        return new UsuariosViewholdes(itemview);
    }

    @Override
    public void onBindViewHolder(Adapter_usuarios.UsuariosViewholdes holder, int position) {
        Usuarios info_users = usersList.get(position);
        holder.binInfoevento(info_users, activity);

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


    public class UsuariosViewholdes extends RecyclerView.ViewHolder{
        private TextView tNombre, tTelefono, tCorreo;

        public UsuariosViewholdes(View itemView) {
            super(itemView);
            tNombre = itemView.findViewById(R.id.tNombre);
            tTelefono = itemView.findViewById(R.id.tTelefono);
            tCorreo = itemView.findViewById(R.id.tCorreo);
        }


        public void binInfoevento(Usuarios info_users, Activity activity) {
            tNombre.setText(info_users.getNombre());
            tCorreo.setText(info_users.getCorreo());
            tTelefono.setText(info_users.getTelefono());

        }
    }



}
