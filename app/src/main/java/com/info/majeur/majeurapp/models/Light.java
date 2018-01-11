package com.info.majeur.majeurapp.models;


public class Light {

    private Long id;

    private Integer level;

    private Status status;

    public Light() {
    }

    public Light(Integer level, Status status) {
        this.level = level;
        this.status = status;
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
