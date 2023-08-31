package com.example.ma2023.model;

import java.util.List;

public class KorakPoKorak {
    private List<String> koraci;
    private String resenje;

    public KorakPoKorak() {}

    public KorakPoKorak(List<String> koraci, String resenje) {
        this.koraci = koraci;
        this.resenje = resenje;
    }

    @Override
    public String toString() {
        return "KorakPoKorak{" +
                "koraci=" + koraci +
                ", resenje='" + resenje + '\'' +
                '}';
    }

    public List<String> getKoraci() {
        return koraci;
    }

    public void setKoraci(List<String> koraci) {
        this.koraci = koraci;
    }

    public String getResenje() {
        return resenje;
    }

    public void setResenje(String resenje) {
        this.resenje = resenje;
    }
}
