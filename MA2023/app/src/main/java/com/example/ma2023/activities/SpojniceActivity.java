package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ma2023.Konekcija;
import com.example.ma2023.R;
import com.example.ma2023.model.Par;
import com.example.ma2023.model.Pitanje;
import com.example.ma2023.model.Spojnica;
import com.example.ma2023.service.SpojnicaService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import com.example.ma2023.model.Spojnica;
import com.example.ma2023.model.Par;


public class SpojniceActivity extends AppCompatActivity implements SpojnicaService.OnSpojniceLoadedListener {
    List<Spojnica> spojniceZaIgru;
    private Socket mSocket;
    private AtomicInteger runda = new AtomicInteger();

    ArrayList<Button> buttons = new ArrayList<>();
    private int turn;

    private Button a1, a2, a3, a4, a5;
    private Button b1, b2, b3, b4, b5;
    private Button sledecaIgra;

    private ArrayList<Button> aButtons = new ArrayList<>();
    private ArrayList<Button> bButtons = new ArrayList<>();


    private List<Button> buttonsWithPairs = new ArrayList<>();

    private List<Button> clickedAButtons = new ArrayList<>();
    private List<Button> allBButtons = new ArrayList<>();

    private List<Button> clickedButtons = new ArrayList<>();

    private boolean gameStarted = false;

    private String keyA = null;
    private String valueB = null;

    private Map<Button, Button> mapa = new HashMap<>();

    private JSONObject data;
    private Spojnica spojnicaRundaJedan;
    private List<Pair<Button, Button>> reseniParovi = new ArrayList<>();
    private Button trenutnoKliknutoDugmeA;
    private List<Button> pokusajiA = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Konekcija app = (Konekcija) SpojniceActivity.this.getApplication();
        this.mSocket = app.getSocket();

        SpojnicaService spojnicaService = new SpojnicaService(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userF = auth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spojnice);
        Intent intent = getIntent();


        TextView aName = findViewById(R.id.textViewAName);
        TextView bName = findViewById(R.id.textViewBName);
        TextView aScore = findViewById(R.id.editTextBName2);
        TextView bScore = findViewById(R.id.editTextBName5);
        aName.setText(intent.getStringExtra("aName"));
        bName.setText(intent.getStringExtra("bName"));
        aScore.setText(intent.getStringExtra("aScore"));
        bScore.setText(intent.getStringExtra("bScore"));


        a1 = findViewById(R.id.button5n1);
        a2 = findViewById(R.id.button5n2);
        a3 = findViewById(R.id.button5n3);
        a4 = findViewById(R.id.button5n4);
        a5 = findViewById(R.id.button5n5);

        b1 = findViewById(R.id.button5n6);
        b2 = findViewById(R.id.button5n7);
        b3 = findViewById(R.id.button5n8);
        b4 = findViewById(R.id.button5n9);
        b5 = findViewById(R.id.button5n10);

        sledecaIgra = findViewById(R.id.button5n11);
        sledecaIgra.setEnabled(false);
        buttons.add(a1);
        buttons.add(a2);
        buttons.add(a3);
        buttons.add(a4);
        buttons.add(a5);

        buttons.add(b1);
        buttons.add(b2);
        buttons.add(b3);
        buttons.add(b4);
        buttons.add(b5);

        aButtons.add(a1);
        aButtons.add(a2);
        aButtons.add(a3);
        aButtons.add(a4);
        aButtons.add(a5);

        bButtons.add(b1);
        bButtons.add(b2);
        bButtons.add(b3);
        bButtons.add(b4);
        bButtons.add(b5);

        //SLEDECA IGRA dugme
        final Button btn5n11 = findViewById(R.id.button5n11);
        btn5n11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SpojniceActivity.this, AsocijacijeActivity.class);
                startActivity(intent);
            }
        });


        mSocket.on("spremiSpojnice", (data) -> {
            Log.d("LOOOOG", "LOOOOOGGG: Received data: " + data[0].toString());
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                JSONArray paroviArray = new JSONArray(new JSONObject(data[0].toString()).getString("parovi"));
                List<Par> parovi = new ArrayList<>();
                for (int i = 0; i < paroviArray.length(); i++) {
                    JSONObject parObject = paroviArray.getJSONObject(i);
                    String key = parObject.getString("key");
                    String value = parObject.getString("value");
                    parovi.add(new Par(key, value));
                }
                spojnicaRundaJedan  = new Spojnica();
                spojnicaRundaJedan.setTekstPitanja(new JSONObject(data[0].toString()).getString("tekstPitanja"));
                spojnicaRundaJedan.setParovi(parovi);
                prikaziSpojnicu(spojnicaRundaJedan);
                //prebaciti u eksternu metodu
//                runOnUiThread(() -> {
//                    TextView pitanjeTextView = findViewById(R.id.textView17);
//                    pitanjeTextView.setText(spojnicaRundaJedan.getTekstPitanja());
//                });
                // ispis spojnice
//                Log.d("LOOOOG", "Tekst pitanja: " + spojnica.getTekstPitanja());
//
//                List<Par> paroviA = spojnica.getParovi();
//                if (paroviA != null && !paroviA.isEmpty()) {
//                    for (Par par : paroviA) {
//                        Log.d("LOOOOG", "Par: key=" + par.getKey() + ", value=" + par.getValue());
//                    }
//                } else {
//                    Log.d("LOOOOG", "Nema parova u spojnici.");
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        mSocket.on("prvaRunda", (data) -> {
            String poruka = data[0].toString();
            if ("prvi".equals(poruka)) {
                runOnUiThread(() -> {
                    Toast.makeText(SpojniceActivity.this, "Prva runda", Toast.LENGTH_SHORT).show();
                    potezA();

                });
            } else if ("drugi".equals(poruka)) {
                runOnUiThread(() -> {
                    Toast.makeText(SpojniceActivity.this, "Prva runda - drugi", Toast.LENGTH_SHORT).show();
                    onemoguciSvuDugmad();
                });
            }
        });


     }


    @Override
    public void onSpojniceLoaded(List<Spojnica> spojnice) {
        spojniceZaIgru = spojnice;
        if (spojniceZaIgru != null && !spojniceZaIgru.isEmpty()) {
            if (Integer.valueOf(String.valueOf(runda)) < 3) {

                int randomIndex = new Random().nextInt(spojniceZaIgru.size());
                Spojnica randomSpojnica = spojniceZaIgru.get(randomIndex);
//                Log.d("spojnice", "Log 2: Received data: " + randomSpojnica.toString());

                TextView pitanjeTextView = findViewById(R.id.textView17);
//                Log.d("spojnice", "Log 3: Received data: " + randomSpojnica.getTekstPitanja());

                //POSTAVLJANEJ PITANJA
//                pitanjeTextView.setText(randomSpojnica.getTekstPitanja());
//        }
//            if (spojniceZaIgru != null && !spojniceZaIgru.isEmpty()) {

//                Spojnica spojnica = spojniceZaIgru.get(1);
//                Log.d("spojnice", "Log 4: Received data: " + randomSpojnica.toString());
//                Log.d("spojnice", "Log AAAA: Received data: " + randomSpojnica.getParovi());
//                Log.d("PREPREPARE RUNDU", "AAAA: " + randomSpojnica);

                data = prepareRunduData(randomSpojnica);
//                Log.d("spojnice", "Log 7: Received data: " + data.toString());
//                Log.d("POSLEPREPARE RUNDU", "BBBB: " + data);

                mSocket.emit("spremiIgru", "spojnice", data);
            }
        }
    }

    private JSONObject prepareRunduData(Spojnica spojnica) {
        JSONObject data = new JSONObject();
        try {
            data.put("tekstPitanja", spojnica.getTekstPitanja());
//            Log.d("spojnice", "Log 5: Received data: " + spojnica.getTekstPitanja());
            data.put("parovi", spojnica.getParovi());
//            Log.d("spojnice", "Log 6: Received data: " + spojnica.getParovi());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void prikaziSpojnicu(Spojnica spojnica) {
        runOnUiThread(() -> {
//            Log.d("SpojniceActivity", "Prikazivanje spojnice: " + spojnica.toString());

            TextView pitanjeTextView = findViewById(R.id.textView17);
            pitanjeTextView.setText(spojnica.getTekstPitanja());
//            Log.d("SpojniceActivity", "Postavljeno pitanje: " + spojnica.getTekstPitanja());

            List<Par> parovi = spojnica.getParovi();
//            Log.d("SpojniceActivity", "Broj parova u spojnici: " + parovi.size());

//            Log.d("SpojniceActivity", "Broj aButtons: " + aButtons.size());
//            Log.d("SpojniceActivity", "Broj bButtons: " + bButtons.size());

//            if (parovi != null && !parovi.isEmpty()) {
//                for (int i = 0; i < parovi.size(); i++) {
//                    Par par = parovi.get(i);
//
//                    if (i < aButtons.size()) {
//                        aButtons.get(i).setText(par.getKey());
////                        Log.d("SpojniceActivity", "Postavljeno aButton " + i + ": " + par.getKey());
//                    } else {
////                        Log.e("SpojniceActivity", "Nedovoljno aButtons za spojnicu.");
//                    }
//
//                    if (i < bButtons.size()) {
//                        bButtons.get(i).setText(par.getValue());
////                        Log.d("SpojniceActivity", "Postavljeno bButton " + i + ": " + par.getValue());
//                    } else {
////                        Log.e("SpojniceActivity", "Nedovoljno bButtons za spojnicu.");
//                    }
//                }

            if (parovi != null && !parovi.isEmpty()) {
                // Mešanje redosleda parova
                Collections.shuffle(parovi);

                // Mešanje redosleda dugmadi A
                Collections.shuffle(aButtons);
                // Postavljanje teksta dugmadi A
                for (int i = 0; i < parovi.size(); i++) {
                    Par par = parovi.get(i);
                    if (i < aButtons.size()) {
                        aButtons.get(i).setText(par.getKey());
                    } else {
                        Log.e("SpojniceActivity", "Nedovoljno aButtons za spojnicu.");
                    }
                }

                // Mešanje redosleda dugmadi B
                Collections.shuffle(bButtons);
                // Postavljanje teksta dugmadi B
                for (int i = 0; i < parovi.size(); i++) {
                    Par par = parovi.get(i);
                    if (i < bButtons.size()) {
                        bButtons.get(i).setText(par.getValue());
                    } else {
                        Log.e("SpojniceActivity", "Nedovoljno bButtons za spojnicu.");
                    }
                }



            } else {
                Log.e("SpojniceActivity", "Spojnica nema parova.");
            }
        });
    }



    private void onemoguciDugmadA() {
        for (Button aButton : aButtons) {
            aButton.setEnabled(false);
        }
    }

    private void onemoguciDugmadB() {
        for (Button bButton : bButtons) {
            bButton.setEnabled(false);
        }
    }

    private void omoguciDugmadA() {
        for (Button aButton : aButtons) {
            aButton.setEnabled(true);
        }
    }

    private void omoguciDugmadB() {
        for (Button bButton : bButtons) {
            bButton.setEnabled(true);
        }
    }
    private void onemoguciSvuDugmad() {
       for (Button button : buttons) {
            button.setEnabled(false);
       }
    }
    private void omoguciSvuDugmad() {
       for (Button button : buttons) {
            button.setEnabled(true);
       }
    }

//    private void potezA() {
//        for (Button buttonA : aButtons) {
//            buttonA.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onemoguciDugmadA();
//                    omoguciDugmadB();
//                    buttonA.setEnabled(true);
//                    Toast.makeText(SpojniceActivity.this, "Kliknuto na dugme A", Toast.LENGTH_SHORT).show();
//
//                    // Provera da li je par već rešen
//                    if (!isParAlreadySolved(buttonA)) {
//                        // Postavljanje listenera za dugmad B samo ako par nije već rešen
//                        potezB(buttonA);
//                    }
//                }
//            });
//        }
//    }

    private void potezA() {
        for (Button buttonA : aButtons) {
            buttonA.setOnClickListener(v -> {
                onemoguciDugmadA();
                omoguciDugmadB();
                buttonA.setEnabled(true);
                Toast.makeText(SpojniceActivity.this, "Kliknuto na dugme A", Toast.LENGTH_SHORT).show();

                if (!isParAlreadySolved(buttonA)) {
                    trenutnoKliknutoDugmeA = buttonA;
                    pokusajiA.add(buttonA); // Dodavanje trenutno kliknutog dugmeta A u listu pokušaja
                    potezB();
                }
            });
        }
    }


    private void potezB() {
        for (Button buttonB : bButtons) {
            trenutnoKliknutoDugmeA.setOnClickListener(null);
            buttonB.setOnClickListener(v -> {
                if (proveriPar(trenutnoKliknutoDugmeA, buttonB)) {
                    promeniBojuDugmeta(trenutnoKliknutoDugmeA, Color.GREEN);
                    promeniBojuDugmeta(buttonB, Color.GREEN);
                    onemoguciDugmad(trenutnoKliknutoDugmeA, buttonB);
                    reseniParovi.add(new Pair<>(trenutnoKliknutoDugmeA, buttonB));
                    resetujStanjeDugmadi(reseniParovi, trenutnoKliknutoDugmeA, buttonB);
                } else {
                    promeniBojuDugmeta(trenutnoKliknutoDugmeA, Color.RED);
                    onemoguciDugmad(trenutnoKliknutoDugmeA, buttonB);

                    omoguciSvaDugmadAExcept(reseniParovi, trenutnoKliknutoDugmeA);
                    onemoguciSvaDugmadBExcept(reseniParovi, buttonB);
                }
            });
        }
    }



//    private void potezB(Button buttonA) {
//        for (Button buttonB : bButtons) {
//            buttonB.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (proveriPar(buttonA, buttonB)) {
//                        promeniBojuDugmeta(buttonA, Color.GREEN);
//                        promeniBojuDugmeta(buttonB, Color.GREEN);
//                        onemoguciDugmad(buttonA, buttonB);
//
//                        reseniParovi.add(new Pair<>(buttonA, buttonB));
//
////                        if (reseniParovi.size() == spojnicaRundaJedan.getParovi().size()) {
////                            handleSviParoviReseni();
////                        }
//                    } else {
//                        promeniBojuDugmeta(buttonA, Color.RED);
//                    }
//                }
//            });
//        }
//    }

    private boolean isParAlreadySolved(Button buttonA) {
        for (Pair<Button, Button> reseniPar : reseniParovi) {
            if (reseniPar.first == buttonA || reseniPar.second == buttonA) {
                // Par već rešen
                return true;
            }
        }
        return false;
    }

    private boolean proveriPar(Button buttonA, Button buttonB) {
        String textA = buttonA.getText().toString();
        String textB = buttonB.getText().toString();

        List<Par> parovi = spojnicaRundaJedan.getParovi();

        for (Par par : parovi) {
            if (par.getKey().equals(textA) && par.getValue().equals(textB)) {
                return true;
            }
        }

        return false;
    }

    private void promeniBojuDugmeta(Button button, int color) {
        button.setBackgroundColor(color);}


//    private void handleSviParoviReseni() {
//        ??????
//    }

    private void onemoguciDugmad(Button... buttons) {
        for (Button button : buttons) {
            button.setEnabled(false);
        }
    }

    private void omoguciSvuDugmadExcept(List<Pair<Button, Button>> reseniParovi, Button buttonA) {
        for (Button button : buttons) {
            if (!containsButton(reseniParovi, button) && button != buttonA) {
                button.setEnabled(true);
            }
        }
    }

    private boolean containsButton(List<Pair<Button, Button>> reseniParovi, Button button) {
        for (Pair<Button, Button> reseniPar : reseniParovi) {
            if (reseniPar.first == button || reseniPar.second == button) {
                return true;
            }
        }
        return false;
    }

    private void onemoguciSvaDugmadBExcept(List<Pair<Button, Button>> reseniParovi, Button buttonB) {
        for (Button button : bButtons) {
            if (!containsButton(reseniParovi, button) && button != buttonB) {
                button.setEnabled(false);
            }
        }
    }

    private void omoguciSvaDugmadAExcept(List<Pair<Button, Button>> reseniParovi, Button buttonA) {
        for (Button button : aButtons) {
            if (!containsButton(reseniParovi, button) && button != buttonA) {
                button.setEnabled(true);
            }
        }
    }

// ...

    private void resetujStanjeDugmadi(List<Pair<Button, Button>> reseniParovi, Button buttonA, Button buttonB) {
        for (Button button : buttons) {
            boolean isException = false;
            for (Pair<Button, Button> reseniPar : reseniParovi) {
                if (button == reseniPar.first || button == reseniPar.second) {
                    isException = true;
                    break;
                }
            }

            if (!isException) {
                button.setEnabled(false); // Dodatno onemogući sva dugmad koja nisu u paru
            }
        }
        onemoguciDugmad(buttonA, buttonB);
        promeniBojuDugmeta(buttonA, Color.GREEN);
        promeniBojuDugmeta(buttonB, Color.GREEN);
        for (Pair<Button, Button> reseniPar : reseniParovi) {
            omoguciSvaDugmadAExcept(reseniParovi, reseniPar.first);
            onemoguciSvaDugmadBExcept(reseniParovi, reseniPar.second);
        }
    }

}





