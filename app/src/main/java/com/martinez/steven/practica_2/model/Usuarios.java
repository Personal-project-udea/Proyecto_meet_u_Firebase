package com.martinez.steven.practica_2.model;

public class Usuarios {
    private String id, nombre, telefono, correo, foto;

    public Usuarios() {
    }

    public Usuarios(String id, String nombre, String telefono, String correo, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getFoto() {
        return foto;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
