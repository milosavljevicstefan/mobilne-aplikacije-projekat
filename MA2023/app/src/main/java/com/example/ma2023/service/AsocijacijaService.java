package com.example.ma2023.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ma2023.model.Asocijacija;
import com.example.ma2023.model.Kolona;
import com.example.ma2023.model.Spojnica;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AsocijacijaService {
    private List<Asocijacija> asocijacije = new ArrayList<>();
    private OnAsocijacijeLoadedListener listener;

    public interface OnAsocijacijeLoadedListener {
        void onAsocijacijeLoaded(List<Asocijacija> asocijacije);
    }

    public AsocijacijaService(OnAsocijacijeLoadedListener listener) {
        this.listener = listener;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("asocijacije").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("AsocijacijaService", document.toString());

                                List<Kolona> kolone = new ArrayList<>();
                                List<Map<String, Object>> koloneData = (List<Map<String, Object>>) document.get("kolone");
                                for (Map<String, Object> kolonaData : koloneData) {
                                    String resenjeKolone = (String) kolonaData.get("resenjeKolone");
                                    List<String> polja = (List<String>) kolonaData.get("polja");

                                    Kolona kolona = new Kolona(polja, resenjeKolone);
                                    kolone.add(kolona);
                                }

                                String konacnoResenje = document.getString("konacnoResenje");

                                Asocijacija asocijacija = new Asocijacija(kolone, konacnoResenje);
                                asocijacije.add(asocijacija);
                            }
                            if (listener != null) {
                                listener.onAsocijacijeLoaded(getTwoRandomAsocijacije());
                            }
                        } else {
                            Log.d("AsocijacijaService", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public List<Asocijacija> getTwoRandomAsocijacije() {
        List<Asocijacija> randomAsocijacije = new ArrayList<>(asocijacije);
        Collections.shuffle(randomAsocijacije);

        return randomAsocijacije.subList(0, 2);
    }

    public List<Asocijacija> getAsocijacije() {
        return asocijacije;
    }
}
