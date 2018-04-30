package com.martinez.steven.practica_2.model;

import java.util.ArrayList;

public class EventosUsuarios {
    String id ,creadorid, eventoid, usersins;

    public EventosUsuarios() {
    }

    public EventosUsuarios(String id, String creadorid, String eventoid, String usersins) {
        this.id = id;
        this.creadorid = creadorid;
        this.eventoid = eventoid;
        this.usersins = usersins;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreadorid() {
        return creadorid;
    }

    public void setCreadorid(String creadorid) {
        this.creadorid = creadorid;
    }

    public String getEventoid() {
        return eventoid;
    }

    public void setEventoid(String eventoid) {
        this.eventoid = eventoid;
    }

    public String getUsersins() {
        return usersins;
    }

    public void setUsersins(String usersins) {
        this.usersins = usersins;
    }
}
