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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_eventos extends RecyclerView.Adapter<Adapter_eventos.EventosViewholdes> {

    private ArrayList<Eventos> eventosList;
    private int resource;
    private Activity activity;

    public Adapter_eventos(ArrayList<Eventos> eventosList) {
        this.eventosList = eventosList;
    }

    public Adapter_eventos(ArrayList<Eventos> eventosList, int resource, Activity activity) {
        this.eventosList = eventosList;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public EventosViewholdes onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "abre actividadcon el detalle", Toast.LENGTH_SHORT).show();

            }
        });

        return new EventosViewholdes(itemview);
    }

    @Override
    public void onBindViewHolder(EventosViewholdes holder, int position) {
        Eventos info_evento = eventosList.get(position);
        holder.binInfoevento(info_evento, activity);

    }

    @Override
    public int getItemCount() {
        return eventosList.size();
    }


    public class EventosViewholdes extends RecyclerView.ViewHolder{
        private TextView tdeporte, tlugar, tfecha, thora;
        private ImageView igraf;

        public EventosViewholdes(View itemView) {
            super(itemView);
            tdeporte = itemView.findViewById(R.id.tDeporte);
            tfecha = itemView.findViewById(R.id.tFecha);
            thora = itemView.findViewById(R.id.tHora);
            tlugar  = itemView.findViewById(R.id.tLugar);
            igraf = itemView.findViewById(R.id.iGraf);
        }


        public void binInfoevento(Eventos info_evento, Activity activity) {
            tdeporte.setText(info_evento.getDeporte());
            tfecha.setText(info_evento.getFecha());
            thora.setText(info_evento.getHora());
            tlugar.setText(info_evento.getLugar());
            Picasso.get().load(info_evento.getFoto()).into(igraf);



        }
    }



}
