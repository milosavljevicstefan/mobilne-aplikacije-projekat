package com.example.ma2023.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ma2023.model.Par;
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

public class SpojnicaService {
    private List<Spojnica> spojnice = new ArrayList<>();
    private OnSpojniceLoadedListener listener;

    public interface OnSpojniceLoadedListener {
        void onSpojniceLoaded(List<Spojnica> spojnice);
    }

    public SpojnicaService(OnSpojniceLoadedListener listener) {
        this.listener = listener;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("spojnice").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SpojniceService", document.toString());
                                String pitanjeText = document.getString("tekst_pitanja");
                                List<Map<String, String>> paroviData = (List<Map<String, String>>) document.get("parovi");

                                List<Par> parovi = new ArrayList<>();
                                for (Map<String, String> parData : paroviData) {
                                    String key = parData.get("key");
                                    String value = parData.get("value");
                                    Par par = new Par(key, value);
                                    parovi.add(par);
                                }

                                Spojnica spojnica = new Spojnica(pitanjeText, parovi);
                                spojnice.add(spojnica);
                            }
                            if (listener != null) {
                                listener.onSpojniceLoaded(getTwoRandomSpojnice());
                            }
                        } else {
                            Log.d("SpojniceService", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public List<Spojnica> getTwoRandomSpojnice() {
        List<Spojnica> randomSpojnice = new ArrayList<>(spojnice);
        Collections.shuffle(randomSpojnice);

        return randomSpojnice.subList(0, 2);
    }

    public List<Spojnica> getSpojnice() {
        return spojnice;
    }
}

