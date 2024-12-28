package com.example.atelier10;

public class Livre {
    private String id;
    private String titre;
    private int nbpage;

    public Livre(String id, String titre, int nbpage) {
        this.id = id;
        this.nbpage = nbpage;
        this.titre = titre;
    }

    public Livre(String titre, int nbpage) {
        this.nbpage = nbpage;
        this.titre = titre;
    }

    public String getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public int getNbpage() {
        return nbpage;
    }

    public String toString() {
        return titre + "-" + nbpage;
    }
}
