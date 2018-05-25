package com.martinez.steven.practica_2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martinez.steven.practica_2.model.Eventos;

import java.util.ArrayList;

public class Adapter_eventos extends RecyclerView.Adapter<Adapter_eventos.EventosViewholdes> {

    private ArrayList<Eventos> eventosList;
    private int resource;
    private Activity activity;
    private int select;
    private int posev;
    private Bundle extras;

    private Context context;

    public Adapter_eventos(ArrayList<Eventos> eventosList) {
        this.eventosList = eventosList;
    }

    public Adapter_eventos(Context context) {
        this.context = context;
    }

    public Adapter_eventos(ArrayList<Eventos> eventosList, int resource, Activity activity, int selector) {
        this.eventosList = eventosList;
        this.resource = resource;
        this.activity = activity;
        this.select = selector;
    }

    @Override
    public EventosViewholdes onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select == 0) {
                    Eventos eventsel = eventosList.get(posev);
                    Intent intent = new Intent(view.getContext(), InfoEventActivity.class);
                    intent.putExtra("id", eventsel.getId());
                    view.getContext().startActivity(intent);
                    Toast.makeText(activity, "abre actividad con detalle", Toast.LENGTH_SHORT).show();
                }else if (select == 1){
                    Eventos eventsel = eventosList.get(posev);
                    Intent intent = new Intent(view.getContext(), EditEventActivity.class);
                    intent.putExtra("id", eventsel.getId());
                    view.getContext().startActivity(intent);
                    Toast.makeText(activity, "abre actividad con detalle editar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return new EventosViewholdes(itemview);
    }

    @Override
    public void onBindViewHolder(EventosViewholdes holder, int position) {
        posev = position;
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
            tlugar.setText(info_evento.getCancha());

            if (info_evento.getDeporte().equals("Fútbol")){
                igraf.setImageResource(R.drawable.futbol);
            }else if (info_evento.getDeporte().equals("Básquetbol")){
                igraf.setImageResource(R.drawable.basquetbol);
            }else if(info_evento.getDeporte().equals("Voleibol")){
                igraf.setImageResource(R.drawable.voleibol);
            }else if(info_evento.getDeporte().equals("Micro")){
                igraf.setImageResource(R.drawable.micro);
            }

        }
    }



}
