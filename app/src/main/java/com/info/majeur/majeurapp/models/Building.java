package com.info.majeur.majeurapp.models;

import java.util.List;

public class Building {

    private Long id;

    private String name;

    private List<Room> rooms;

    public Building() {
    }

    public Building(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
