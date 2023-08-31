package com.example.ma2023.model;

import java.util.List;

public class Pitanje {
    private String tekstPitanja;
    private String tacanOdgovor;
    private List<String> pogresniOdgovori;

    public Pitanje() {}

    public Pitanje(String tekstPitanja, String tacanOdgovor, List<String> pogresniOdgovori) {
        this.tekstPitanja = tekstPitanja;
        this.tacanOdgovor = tacanOdgovor;
        this.pogresniOdgovori = pogresniOdgovori;
    }

    public String getTekstPitanja() {
        return tekstPitanja;
    }

    public void setTekstPitanja(String tekstPitanja) {
        this.tekstPitanja = tekstPitanja;
    }

    public String getTacanOdgovor() {
        return tacanOdgovor;
    }

    public void setTacanOdgovor(String tacanOdgovor) {
        this.tacanOdgovor = tacanOdgovor;
    }

    public List<String> getPogresniOdgovori() {
        return pogresniOdgovori;
    }

    public void setPogresniOdgovori(List<String> pogresniOdgovori) {
        this.pogresniOdgovori = pogresniOdgovori;
    }
}
