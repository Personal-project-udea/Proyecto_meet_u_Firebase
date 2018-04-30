package com.martinez.steven.practica_2.model;

public class Eventos {
    String id, descripcion, cancha, puntoEncuentro, deporte, fecha, hora;

    public Eventos() {
    }

    public Eventos(String id,String descripcion, String cancha, String puntoEncuentro, String deporte, String fecha, String hora) {

        this.id = id;
        this.descripcion = descripcion;
        this.cancha = cancha;
        this.puntoEncuentro = puntoEncuentro;
        this.deporte = deporte;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCancha() {
        return cancha;
    }

    public String getPuntoEncuentro() {
        return puntoEncuentro;
    }

    public String getDeporte() {
        return deporte;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCancha(String cancha) {
        this.cancha = cancha;
    }

    public void setPuntoEncuentro(String puntoEncuentro) {
        this.puntoEncuentro = puntoEncuentro;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getId() {
        return id;
    }
}
