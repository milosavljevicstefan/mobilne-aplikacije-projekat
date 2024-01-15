package com.example.ma2023.model;

import java.util.List;
import java.util.Map;

public class Spojnica {
    private String tekstPitanja;
    private List<Par> parovi;

    public Spojnica() {}

    public Spojnica(String tekstPitanja, List<Par> parovi) {
        this.tekstPitanja = tekstPitanja;
        this.parovi = parovi;
    }

    public String getTekstPitanja() {
        return tekstPitanja;
    }

    public void setTekstPitanja(String tekstPitanja) {
        this.tekstPitanja = tekstPitanja;
    }

    public List<Par> getParovi() {
        return parovi;
    }

    public void setParovi(List<Par> parovi) {
        this.parovi = parovi;
    }
}
