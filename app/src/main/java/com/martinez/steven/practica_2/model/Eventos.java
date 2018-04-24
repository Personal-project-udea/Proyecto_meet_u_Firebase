package com.martinez.steven.practica_2.model;

public class Eventos {
    String deporte, foto, fecha, hora, lugar;

    public Eventos() {
    }

    public Eventos(String deporte, String foto, String fecha, String hora, String lugar) {
        this.deporte = deporte;
        this.foto = foto;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
