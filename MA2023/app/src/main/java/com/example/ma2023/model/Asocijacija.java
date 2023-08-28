package com.example.ma2023.model;

import java.util.List;

public class Asocijacija {
    private List<Kolona> kolone;
    private String konacnoResenje;

    public Asocijacija() {}

    public Asocijacija(List<Kolona> kolone, String konacnoResenje) {
        this.kolone = kolone;
        this.konacnoResenje = konacnoResenje;
    }

    public List<Kolona> getKolone() {
        return kolone;
    }

    public void setKolone(List<Kolona> kolone) {
        this.kolone = kolone;
    }

    public String getKonacnoResenje() {
        return konacnoResenje;
    }

    public void setKonacnoResenje(String konacnoResenje) {
        this.konacnoResenje = konacnoResenje;
    }
}

