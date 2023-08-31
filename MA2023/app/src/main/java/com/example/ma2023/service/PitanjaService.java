package com.example.ma2023.service;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ma2023.model.Pitanje;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PitanjaService {
    private List<Pitanje> pitanja = new ArrayList<>();
    private OnPitanjaLoadedListener listener;

    public interface OnPitanjaLoadedListener {
        void onPitanjaLoaded(List<Pitanje> pitanja);
    }

    public PitanjaService(OnPitanjaLoadedListener listener) {
        this.listener = listener;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        firestore.collection("pitanja").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("koZnaZna", document.toString());
                                String pitanjeText = document.getString("tekst_pitanja");
                                List<String> pogresniOdgovori = (List<String>) document.get("pogresni_odgovori");
                                String tacanOdgovor = document.getString("tacan_odgovor");

                                Pitanje pitanje = new Pitanje(pitanjeText,tacanOdgovor,pogresniOdgovori);

                                pitanja.add(pitanje);
                            }
                            if (listener != null) {
                                listener.onPitanjaLoaded(getFiveRandomQuestions());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public List<Pitanje> getFiveRandomQuestions() {

        List<Pitanje> randomQuestions = new ArrayList<>(pitanja);
        Collections.shuffle(randomQuestions);

        return randomQuestions.subList(0, 5);
    }
}
