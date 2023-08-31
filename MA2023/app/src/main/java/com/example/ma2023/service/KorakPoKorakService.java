package com.example.ma2023.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ma2023.model.KorakPoKorak;
import com.example.ma2023.model.Spojnica;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KorakPoKorakService {
    private List<KorakPoKorak> koraciPoKoraku = new ArrayList<>();
    private OnKoraciPoKorakuLoadedListener listener;

    public interface OnKoraciPoKorakuLoadedListener {
        void onKoraciPoKorakuLoaded(List<KorakPoKorak> koraciPoKoraku);
    }

    public KorakPoKorakService(OnKoraciPoKorakuLoadedListener listener) {
        this.listener = listener;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("korak-po-korak").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("KorakPoKorakService", document.toString());
                                List<String> koraci = (List<String>) document.get("koraci");
                                String resenje = document.getString("resenje");

                                KorakPoKorak korakPoKorak = new KorakPoKorak(koraci, resenje);
                                koraciPoKoraku.add(korakPoKorak);
                            }
                            if (listener != null) {
                                listener.onKoraciPoKorakuLoaded(getTwoRandomKPK());
                            }
                        } else {
                            Log.d("KorakPoKorakService", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public List<KorakPoKorak> getTwoRandomKPK() {
        List<KorakPoKorak> randomKPK = new ArrayList<>(koraciPoKoraku);
        Collections.shuffle(randomKPK);

        return randomKPK.subList(0, 2);
    }

    public List<KorakPoKorak> getKoraciPoKoraku() {
        return koraciPoKoraku;
    }
}

