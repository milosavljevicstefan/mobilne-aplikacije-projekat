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

import com.example.ma2023.model.Spojnica;


public class SpojnicaService {
    private List<Spojnica> spojnice = new ArrayList<>();
    private OnSpojniceLoadedListener listener;

    public interface OnSpojniceLoadedListener {
        void onSpojniceLoaded(List<Spojnica> spojnice);
    }

    public SpojnicaService(OnSpojniceLoadedListener listener) {
        this.listener = listener;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // ...

        firestore.collection("spojnice").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SpojniceService", document.toString());
                                String pitanjeText = document.getString("tekstPitanja");
                                Log.d("SpojniceService", "Pitanje Text: " + pitanjeText);

                                // Change the type of paroviData from String to Object
                                List<Map<String, Object>> paroviData = (List<Map<String, Object>>) document.get("parovi");
                                Log.d("SpojniceService", "Parovi Data: " + paroviData);

                                List<Par> parovi = new ArrayList<>();
                                Log.d("SpojniceService", "Initialized parovi list");

                                for (Map<String, Object> parData : paroviData) {
                                    Log.d("SpojniceService", "Processing parData: " + parData);

                                    // Change the type of key and value from String to Object
                                    Object key = parData.get("key");
                                    Object value = parData.get("value");

                                    Log.d("SpojniceService", "Key: " + key + ", Value: " + value);

                                    // Convert key and value to String if needed
                                    String keyString = key.toString();
                                    String valueString = value.toString();

                                    Log.d("SpojniceService", "Converted Key to String: " + keyString + ", Converted Value to String: " + valueString);

                                    Par par = new Par(keyString, valueString);
                                    parovi.add(par);
                                    Log.d("SpojniceService", "Par added to parovi list: " + par);
                                }

                                Spojnica spojnica = new Spojnica(pitanjeText, parovi);
                                spojnice.add(spojnica);
                                Log.d("SpojniceService", "Spojnica added: " + spojnica);
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

