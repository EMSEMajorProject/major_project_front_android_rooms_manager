package com.info.majeur.majeurapp.models;


public class Noise {

    private Long id;

    private Integer level;

    private Status status;

    public Noise(Integer level, Status status) {
        this.level = level;
        this.status = status;
    }
    public Noise(){

    }
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
