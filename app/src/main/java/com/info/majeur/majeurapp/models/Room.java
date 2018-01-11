package com.info.majeur.majeurapp.models;


public class Room {


    private Long id;

    private Light light;

    private Noise noise;

    private String name;

    public Room(Light light, Noise noise, String name) {
        this.light = light;
        this.noise = noise;
        this.name = name;
    }

    public Room (){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public Noise getNoise() {
        return noise;
    }

    public void setNoise(Noise noise) {
        this.noise = noise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}