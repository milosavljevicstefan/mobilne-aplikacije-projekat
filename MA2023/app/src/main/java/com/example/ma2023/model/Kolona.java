package com.example.ma2023.model;

import java.util.List;

public class Kolona {
    private List<String> polja;
    private String resenjeKolone;

    public Kolona() {}

    public Kolona(List<String> polja, String resenjeKolone) {
        this.polja = polja;
        this.resenjeKolone = resenjeKolone;
    }

    public List<String> getPolja() {
        return polja;
    }

    public void setPolja(List<String> polja) {
        this.polja = polja;
    }

    public String getResenjeKolone() {
        return resenjeKolone;
    }

    public void setResenjeKolone(String resenjeKolone) {
        this.resenjeKolone = resenjeKolone;
    }
}
