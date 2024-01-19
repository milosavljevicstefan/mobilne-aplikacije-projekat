package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ma2023.Konekcija;
import com.example.ma2023.R;
import com.example.ma2023.model.KorakPoKorak;
import com.example.ma2023.service.KorakPoKorakService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.socket.client.Socket;

public class KorakPoKorakActivity extends AppCompatActivity implements KorakPoKorakService.OnKoraciPoKorakuLoadedListener{
    private Socket mSocket;
    private List<KorakPoKorak> koraciZaIgru;

    private List<TextView> koraciPoKorakuTextView;
    private TextView korak1TextView, korak2TextView, korak3TextView, korak4TextView, korak5TextView, korak6TextView, korak7TextView;
    private JSONObject data;
    private KorakPoKorak korakPoKorakRundaJedan;
    private String trenutniIgrac;
    private CountDownTimer countDownTimer;
    private TextView timerTextView;
    private int trenutnaRunda = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korak_po_korak);

        Intent intent = getIntent();

        TextView aName = findViewById(R.id.textViewAName3);
        TextView bName = findViewById(R.id.textViewBName3);
        TextView aScore = findViewById(R.id.editTextBName23);
        TextView bScore = findViewById(R.id.editTextBName53);

        aName.setText(intent.getStringExtra("aName"));
        bName.setText(intent.getStringExtra("bName"));
        aScore.setText(intent.getStringExtra("aScore"));
        bScore.setText(intent.getStringExtra("bScore"));

        Konekcija app = (Konekcija) KorakPoKorakActivity.this.getApplication();
        this.mSocket = app.getSocket();
        KorakPoKorakService spojnicaService = new KorakPoKorakService(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userF = auth.getCurrentUser();

        // Ovde se dohvata lista koraka iz servisa
//        koraciPoKorakuList = korakPoKorakService.getKoraciPoKoraku();

//        KorakPoKorak prviKorak = koraciPoKorakuList.get(0);

        // Postavljanje vrednosti za prvi korak
        korak1TextView = findViewById(R.id.korak1);
//        korak1TextView.setText(prviKorak.getKoraci().get(0));
        korak2TextView = findViewById(R.id.korak2);
        korak3TextView = findViewById(R.id.korak3);
        korak4TextView = findViewById(R.id.korak4);
        korak5TextView = findViewById(R.id.korak5);
        korak6TextView = findViewById(R.id.korak6);
        korak7TextView = findViewById(R.id.korak7);

        koraciPoKorakuTextView = new ArrayList<>();

        koraciPoKorakuTextView.add(korak1TextView);
        koraciPoKorakuTextView.add(korak2TextView);
        koraciPoKorakuTextView.add(korak3TextView);
        koraciPoKorakuTextView.add(korak4TextView);
        koraciPoKorakuTextView.add(korak5TextView);
        koraciPoKorakuTextView.add(korak6TextView);
        koraciPoKorakuTextView.add(korak7TextView);

        mSocket.on("spremiKorakPoKorak", (data) -> {
//            Log.d("LOOOOG", "LOOOOOGGG: Received data: " + data[0].toString());
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                JSONArray koraciArray = new JSONArray(new JSONObject(data[0].toString()).getString("koraci"));
                List<String> koraci = new ArrayList<>();
                for (int i = 0; i < koraciArray.length(); i++) {
                    String korak = koraciArray.getString(i);
                    koraci.add(korak);
                }
                korakPoKorakRundaJedan  = new KorakPoKorak();
                korakPoKorakRundaJedan.setResenje(new JSONObject(data[0].toString()).getString("resenje"));
                korakPoKorakRundaJedan.setKoraci(koraci);
//                prikaziKorakPoKorak(korakPoKorakRundaJedan);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        mSocket.on("prvaRunda", (data) -> {
            String poruka = data[0].toString();
            if ("prvi".equals(poruka)) {
                runOnUiThread(() -> {
                    Toast.makeText(KorakPoKorakActivity.this, "Prva runda - PLAVI ", Toast.LENGTH_SHORT).show();
                    trenutniIgrac = "prvi";
                    trenutnaRunda = 1;
                    mSocket.emit("tajmer", "spojnice");
                });
            } else if ("drugi".equals(poruka)) {
                runOnUiThread(() -> {
                    Toast.makeText(KorakPoKorakActivity.this, "Prva runda - CRVENI", Toast.LENGTH_SHORT).show();
                    trenutniIgrac = "drugi";
                    trenutnaRunda = 1;
                    mSocket.emit("tajmer", "spojnice");
                });
            }
        });

        timerTextView = findViewById(R.id.timerTextView2);
        int initialTimeInSeconds = 5;
        updateTimer(initialTimeInSeconds);

        mSocket.on("zapocniTajmer", (data) -> {
            runOnUiThread(() -> {
                startTimer(initialTimeInSeconds);
            });
        });

    }
    @Override
    public void onKoraciPoKorakuLoaded(List<KorakPoKorak> koraci){
        koraciZaIgru = koraci;
        if (koraciZaIgru != null && !koraciZaIgru.isEmpty()) {
//            Log.d("KorakPoKorakActivity", "Koraci loaded successfully");
                // if (Integer.valueOf(String.valueOf(runda)) < 3) {
            int randomIndex = new Random().nextInt(koraciZaIgru.size());
            //Log.d("KorakPoKorakActivity", "Random index selected: " + randomIndex);
            KorakPoKorak randomKorak = koraciZaIgru.get(randomIndex);
//            Log.d("KorakPoKorakActivity", "Random KorakPoKorak selected: " + randomKorak.toString());
            data = prepareRunduData(randomKorak);
//            Log.d("KorakPoKorakActivity", "Data prepared for the round: " + data.toString());
            mSocket.emit("spremiIgru", "korakPoKorak", data, 1);
//            Log.d("KorakPoKorakActivity", "Data emitted to server");
        }
    }

    private JSONObject prepareRunduData(KorakPoKorak korakPoKorak) {
        JSONObject data = new JSONObject();
        try {
            data.put("resenje", korakPoKorak.getResenje());
            Log.d("resenje", "Log 5: Received data: " + korakPoKorak.getResenje());
            data.put("koraci", new JSONArray(korakPoKorak.getKoraci()));
            Log.d("koraci", "Log 6: Received data: " + korakPoKorak.getKoraci());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void prikaziKorakPoKorak(KorakPoKorak korakPoKorak)
    {
        runOnUiThread(() -> {
            List<String> koraci = korakPoKorak.getKoraci();
            for (int i = 0; i < koraci.size(); i++) {
                String korakText = koraci.get(i);
                int textViewId = getResources().getIdentifier("korak" + (i + 1), "id", getPackageName());
                TextView korakTextView = findViewById(textViewId);
                if (korakTextView != null) {
                    korakTextView.setText(korakText);
                }
            }
            String resenjeText = korakPoKorak.getResenje();
            TextView resenjeTextView = findViewById(R.id.resenje);
            if (resenjeTextView != null) {
                resenjeTextView.setText(resenjeText);
            }
        });
    }

    private void startTimer(int seconds) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                updateTimer((int) (millisUntilFinished / 1000));
            }
            public void onFinish() {
                updateTimer(0);
//                mSocket.emit("krajIgre", "korakPoKorak");
            }
        }.start();
    }

    private void updateTimer(int seconds) {
        timerTextView.setText(String.valueOf(seconds));
    }

}