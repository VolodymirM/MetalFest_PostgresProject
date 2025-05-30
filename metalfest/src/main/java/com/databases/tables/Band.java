package com.databases.tables;

public class Band {
    private int id;
    private String name;
    private String contry;
    private String biography;

    public Band(int id, String name, String contry, String biography) {
        this.id = id;
        this.name = name;
        this.contry = contry;
        this.biography = biography;
    }

    public void display() {
        System.out.println("Band Details:");
        System.out.printf("ID: %d%n", id);
        System.out.printf("Name: %s%n", name);
        System.out.printf("Country: %s%n", contry);
        System.out.printf("Biography: %s%n", biography);
        System.out.println("-----------------------------\n");
        System.out.println("Members:\n");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    

}
