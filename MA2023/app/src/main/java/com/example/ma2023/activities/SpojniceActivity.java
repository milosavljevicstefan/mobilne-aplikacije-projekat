package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

public class SpojniceActivity extends AppCompatActivity implements SpojnicaService.OnSpojniceLoadedListener {
    List<Spojnica> spojniceZaIgru;
    private Socket mSocket;
    private AtomicInteger runda = new AtomicInteger();

    ArrayList<Button> buttons = new ArrayList<>();
    private int turn;



    private ArrayList<Button> aButtons = new ArrayList<>();
    private ArrayList<Button> bButtons = new ArrayList<>();


    private List<Button> buttonsWithPairs = new ArrayList<>();

    private List<Button> clickedAButtons = new ArrayList<>();
    private List<Button> clickedButtons = new ArrayList<>();

    private boolean gameStarted = false;

    private String keyA = null;
    private String valueB = null;

    private Map<Button, Button> mapa = new HashMap<>();


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
        Log.d("ulogovanKorisnikA", "ulogovanKorisnik a" + aName);
        Log.d("ulogovanKorisnikB", "ulogovanKorisnik b" + bName);
        Log.d("reza", "rez a" + aScore);
        Log.d("rezb", "rez b" + bScore);


        Button a1 = findViewById(R.id.button5n1);
        Button a2 = findViewById(R.id.button5n2);
        Button a3 = findViewById(R.id.button5n3);
        Button a4 = findViewById(R.id.button5n4);
        Button a5 = findViewById(R.id.button5n5);

        Button b1 = findViewById(R.id.button5n6);
        Button b2 = findViewById(R.id.button5n7);
        Button b3 = findViewById(R.id.button5n8);
        Button b4 = findViewById(R.id.button5n9);
        Button b5 = findViewById(R.id.button5n10);

//        a1.setEnabled(false);
//        a2.setEnabled(false);
//        a3.setEnabled(false);
//        a4.setEnabled(false);
//        a5.setEnabled(false);


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

        if(userF.getDisplayName().toString() != aName.getText().toString()) {
            b1.setEnabled(false);
            b2.setEnabled(false);
            b3.setEnabled(false);
            b4.setEnabled(false);
            b5.setEnabled(false);
        }

// Inicijalizacija aButtons liste
        for (int i = 1; i <= 5; i++) {
            int buttonId = getResources().getIdentifier("button5n" + i, "id", getPackageName());
            Button aButton = findViewById(buttonId);
            aButtons.add(aButton);
        }


        // Kreirajte listu za dugmad od b1 do b5
        for (int i = 6; i <= 10; i++) {
            int buttonId = getResources().getIdentifier("button5n" + i, "id", getPackageName());
            Button bButton = findViewById(buttonId);
            bButtons.add(bButton);
        }


        mSocket.on("spremiIgru", (a) -> {
            Log.d("spojnice", "Usao u spremiIgru" + Integer.valueOf(String.valueOf(runda)));
            //jedna rudna = jedna spojnica(4 spajanja)
            if (Integer.valueOf(String.valueOf(runda)) < 3) {
                if (spojniceZaIgru != null && !spojniceZaIgru.isEmpty()) {
                    Spojnica spojnica = spojniceZaIgru.get(runda.getAndIncrement());
                    JSONObject runduData = prepareRunduData(spojnica);
                    this.mSocket.emit("runduDataRedirect", runduData.toString()); // Emit to all sockets
                }
            }
            else if (Integer.valueOf(String.valueOf(runda)) == 2) {
                intent.putExtra("aName", aName.getText());
                intent.putExtra("bName", bName.getText());
                intent.putExtra("aScore", aScore.getText());
                intent.putExtra("bScore", bScore.getText());
                this.mSocket.emit("pocniSLEDECU");
            }
        });

        mSocket.on("runduData", (data) -> {
            Log.d("spojnice", "usao u runduData");
            Log.d("spojnice", "Received data: " + data[0].toString());
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true );
                Spojnica spojnica = mapper.readValue(data[0].toString(), Spojnica.class);
//                Log.d("spojnica", "Received data: " + spojnica.toString());
//                spremiRundu(spojnica, buttons);
//                igraj(spojnica, buttons);
            } catch (IOException e) {
//                Log.d("spojnice", "Exception while parsing JSON");
                e.printStackTrace();
            }
        });




//        mSocket.on("updateBar", (value) -> {
//            updateBar(Integer.valueOf(String.valueOf(value)));
//        });
//
//        mSocket.on("obradaBodovaKoZnaZna", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                if (args.length >= 2) {
//                    int bodoviA = (int) args[0];
//                    int bodoviB = (int) args[1];
//
//                    // Update the UI with the received points
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            int currentAScore = Integer.parseInt(aScore.getText().toString());
//                            int currentBScore = Integer.parseInt(bScore.getText().toString());
//
//                            aScore.setText(String.valueOf(currentAScore + bodoviA));
//                            bScore.setText(String.valueOf(currentBScore + bodoviB));
//                            mSocket.emit("sledecaRundaKoZnaZna");
//                        }
//                    });
//                }
//            }
//        });
//
//        //sledeca igra
//        mSocket.on("pocetakIGRA", (data) -> {
//
//            //dodaj sledecu igru
//            Intent intentIGRA = new Intent(SpojniceActivity.this, SpojniceActivity.class);
//            //put data in intent
//            intentIGRA.putExtra("aName", aName.getText());
//            intentIGRA.putExtra("bName", bName.getText());
//            intentIGRA.putExtra("aScore", aScore.getText());
//            intentIGRA.putExtra("bScore", bScore.getText());
////            intentIGRA.putExtra("turn", turn);
//            startActivity(intentIGRA);
//        });

//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSocket.emit("potez");
//            }
//        });


        //SLEDECA IGRA dugme
        final Button btn5n11 = findViewById(R.id.button5n11);
        btn5n11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SpojniceActivity.this, AsocijacijeActivity.class);
                startActivity(intent);
            }
        });


        /////////////////////

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();
                Log.d("mapa", mapa.toString());
                mapa.clear();
                mapa.put(clickedButton, null);
                Log.d("dugmeA1klik", "Klik na a1 dugme");
                clickedAButtons.add(clickedButton);
                clickedButtons.add(clickedButton);

            }
        });

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();
                mapa.clear();

                mapa.put(clickedButton, null);
                clickedAButtons.add(clickedButton);
                clickedButtons.add(clickedButton);

            }
        });

        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();
                mapa.clear();

                mapa.put(clickedButton, null);
                clickedAButtons.add(clickedButton);
                clickedButtons.add(clickedButton);

            }
        });

        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();
                mapa.clear();

                mapa.put(clickedButton, null);
                clickedAButtons.add(clickedButton);
                clickedButtons.add(clickedButton);

            }
        });

        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();
                mapa.clear();

                mapa.put(clickedButton, null);
                clickedAButtons.add(clickedButton);
                clickedButtons.add(clickedButton);

            }
        });

        /////////////////////////
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button ValueButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();

                Map.Entry<Button, Button> prvaStavka = mapa.entrySet().iterator().next();

                Button KeyButton = prvaStavka.getKey();

                proveraSpojnice(KeyButton, ValueButton);
                clickedAButtons.add(ValueButton);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button ValueButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();

                Map.Entry<Button, Button> prvaStavka = mapa.entrySet().iterator().next();

                Button KeyButton = prvaStavka.getKey();

                proveraSpojnice(KeyButton, ValueButton);
                clickedAButtons.add(ValueButton);

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button ValueButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();

                Map.Entry<Button, Button> prvaStavka = mapa.entrySet().iterator().next();

                Button KeyButton = prvaStavka.getKey();

                proveraSpojnice(KeyButton, ValueButton);
                clickedAButtons.add(ValueButton);

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button ValueButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();

                Map.Entry<Button, Button> prvaStavka = mapa.entrySet().iterator().next();

                Button KeyButton = prvaStavka.getKey();

                proveraSpojnice(KeyButton, ValueButton);
                clickedAButtons.add(ValueButton);

            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button ValueButton = (Button) v;
                onemoguciKeyDugmad();
                omoguciValuesDugmad();

                Map.Entry<Button, Button> prvaStavka = mapa.entrySet().iterator().next();

                Button KeyButton = prvaStavka.getKey();

                proveraSpojnice(KeyButton, ValueButton);
                clickedAButtons.add(ValueButton);

            }
        });






    }



//    private void spremiRundu(Spojnica spojnica, ArrayList<Button> buttons) {
//
//
//        int finalI = 0;
//        Log.d("spojnica", spojnica.getTekstPitanja() + spojnica.getParovi());
//        Random random = new Random();
//        TextView pitanjeTextView = findViewById(R.id.textView17);
//        pitanjeTextView.setText(spojnica.getTekstPitanja());
//
//        // Shufflajte parove kako biste dobili random raspored
//        List<Par> shuffledParovi = new ArrayList<>(spojnica.getParovi());
//        Collections.shuffle(shuffledParovi);
//
//        // Izmešajte ključeve
//        List<String> shuffledKeys = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            shuffledKeys.add(shuffledParovi.get(i).getKey()); // Dodajte ključeve u izmešani niz
//        }
//        Collections.shuffle(shuffledKeys); // Izmešajte ključeve
//
//        // Izmešajte vrednosti
//        List<String> shuffledValues = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            shuffledValues.add(shuffledParovi.get(i).getValue()); // Dodajte vrednosti u izmešani niz
//        }
////        log.d();
//        Collections.shuffle(shuffledValues); // Izmešajte vrednosti
//
//        // Postavite ključeve na dugmadima od a1 do a5
//        for (int i = 0; i < 5; i++) {
//            buttons.get(i).setText(shuffledKeys.get(i));
//        }
//
//        // Postavite vrednosti na dugmadima od b1 do b5
//        for (int i = 0; i < 5; i++) {
//            buttons.get(i + 5).setText(shuffledValues.get(i));
//        }
//
//
//        simulateProgressBar();
//    }




    private void simulateProgressBar() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int value = 0;

            @Override
            public void run() {
                value += 10;
                if (value <= 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateBar(value);
                        }
                    });
                } else {
                    mSocket.emit("obradaSpojnice");
                    timer.cancel();
                }
            }
        };

        timer.schedule(task, 0, 2500); // Run task every 2.5 seconds

    }

    private void updateBar(int value) {
        ProgressBar pBar = findViewById(R.id.progressBar);
        pBar.setProgress(value);
    }
    
/*    @Override
    public void onSpojniceLoaded(List<Spojnica> spojnice) {
        spojniceZaIgru = spojnice;
        Log.d("spojnice", "Pitanja ucitana");
        if (mSocket != null) {
            Log.d("spojnice", "Ovde saljemo emit");
        }
    }*/

    private JSONObject prepareRunduData(Spojnica spojnica) {
        JSONObject data = new JSONObject();
        try {
            data.put("tekstPitanja", spojnica.getTekstPitanja());
            data.put("parovi", spojnica.getParovi());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }



    private View.OnClickListener onClickAButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int clickedButtonIndex = buttons.indexOf((Button) v);

        }
    };


    private void processButtonClick(Button aButton, Button bButton) {
        // Dobijte tekst ključa iz a dugmeta
        String kljucA = aButton.getText().toString();

        // Dobijte tekst vrednosti iz b dugmeta
        String vrednostB = bButton.getText().toString();

        // Promenljiva za praćenje podudaranja
        boolean pronadjenPar = false;

        // Prođite kroz listu spojnica i proverite da li postoji spojnica sa odgovarajućim parom
        for (Spojnica spojnica : spojniceZaIgru) {
            List<Par> parovi = spojnica.getParovi();

            for (Par par : parovi) {
                if (par.getKey().equals(kljucA)) {
                    // Pronađen je odgovarajući ključ u spojnici, sada proverite vrednost
                    if (par.getValue().equals(vrednostB)) {
                        // Ključ i vrednost se podudaraju
                        pronadjenPar = true;

                        // Opciono: Možete izvršiti dodatne akcije ako se podudaraju, kao što je promena boja
                        aButton.setEnabled(false);
                        bButton.setEnabled(false);
                        aButton.setBackgroundColor(Color.GREEN);
                        bButton.setBackgroundColor(Color.GREEN);
                        // Dodajte kod da onemogući ostala dugmad B i omogući sva dugmad A osim onog na kog je kliknuto


                        // Dodajte dugmad u listu buttonsWithPairs
                        buttonsWithPairs.add(aButton);
                        buttonsWithPairs.add(bButton);
                    }
                    break; // Prekida se petlja nakon što se pronađe ključ u spojnici
                }
            }

            if (pronadjenPar) {
                break; // Prekida se petlja nakon što se pronađe podudaranje
            }
        }

        // Ako se petljom nije pronašlo podudaranje, obavestite korisnika i vratite dugmad u prvobitno stanje
        if (!pronadjenPar) {
            Toast.makeText(this, "Podudaranje nije pronađeno.", Toast.LENGTH_SHORT).show();
            // Vratite dugmad u prvobitno stanje
        }
    }



    @Override
    public void onSpojniceLoaded(List<Spojnica> spojnice) {
        spojniceZaIgru = spojnice;
        Log.d("spojnice", "Pitanja ucitana");
        if (mSocket != null) {
            Log.d("spojnice", "Ovde saljemo emit");
        }

        if (spojniceZaIgru != null && !spojniceZaIgru.isEmpty()) {
            int randomIndex = new Random().nextInt(spojniceZaIgru.size());
            Spojnica randomSpojnica = spojniceZaIgru.get(randomIndex);
            TextView pitanjeTextView = findViewById(R.id.textView17);
            pitanjeTextView.setText(randomSpojnica.getTekstPitanja());


            rasporediDugmad(randomSpojnica.getParovi());

        }
    }


    private void rasporediDugmad(List<Par> parovi) {
        List<Par> shuffledParovi = new ArrayList<>(parovi);
        Collections.shuffle(shuffledParovi);

        List<String> shuffledKeys = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            shuffledKeys.add(shuffledParovi.get(i).getKey());
        }
        Collections.shuffle(shuffledKeys);

        List<String> shuffledValues = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            shuffledValues.add(shuffledParovi.get(i).getValue());
        }
        Collections.shuffle(shuffledValues);

        for (int i = 0; i < 5; i++) {
            buttons.get(i).setText(shuffledKeys.get(i));
        }

        for (int i = 0; i < 5; i++) {
            buttons.get(i + 5).setText(shuffledValues.get(i));
        }
    }


















    private void onemoguciKeyDugmad() {
        for (Button aButton : aButtons) {
            if (!buttonsWithPairs.contains(aButton) && !clickedAButtons.contains(aButton)) {
                aButton.setEnabled(false);
            }
        }
        Log.d("onemogucitiKeyDugmad", "Onemoguciti key dugmad");
    }

    private void omoguciValuesDugmad() {
        for (Button bButton : bButtons) {
            if (!buttonsWithPairs.contains(bButton) ) {
                bButton.setEnabled(true);
            }
        }
        Log.d("omogucitiValuesDugmad", "Omoguciti values dugmad");
    }

    private void omoguciNeresenaDugmad() {
        for (Button button : buttons) {
            if (!buttonsWithPairs.contains(button) && !clickedAButtons.contains(button)) {
                button.setEnabled(true);
            }
        }
        Log.d("omoguciNeresenaDugmad", "Omoguci neresena dugmad");

    }

    private void proveraSpojnice(Button keyButton, Button valueButton) {
        String keyA = keyButton.getText().toString();
        String valueB = valueButton.getText().toString();

        Log.d("proveraSpojnica", "Provera spojnica, keyA: " + keyA + "; valueB: "+ valueB);

        boolean rezultat = par(keyA, valueB);
        if (rezultat) {
            parPronadjen(keyButton, valueButton);
        } else {
            omoguciNeresenaDugmad();
            pogresnoDugme(keyButton, valueButton);
        }
    }





    public boolean par(String key, String value) {
        for (Spojnica spojnica : spojniceZaIgru) {
            if (spojnica.getParovi().stream().anyMatch(par -> par.getKey().equals(key))) {
                String valueFromSpojnica = spojnica.getParovi().stream()
                        .filter(par -> par.getKey().equals(key))
                        .map(Par::getValue)
                        .findFirst()
                        .orElse(null);

                if (valueFromSpojnica != null && valueFromSpojnica.equals(value)) {
                    return true;
                }}}
        return false;
    }

    public void parPronadjen(Button keyButton, Button valueButton){
        keyButton.setBackgroundColor(Color.GREEN);
        valueButton.setBackgroundColor(Color.GREEN);
        clickedAButtons.add(keyButton);
        buttonsWithPairs.add(keyButton);
        buttonsWithPairs.add(valueButton);
//        keyButton.setEnabled(false);
//        valueButton.setEnabled(false);

        Map.Entry<Button, Button> prvaStavka = mapa.entrySet().iterator().next();
        mapa.remove(prvaStavka.getKey());
        omoguciNeresenaDugmad();

    }

    public void pogresnoDugme(Button keyButton, Button valueButton){
//        final int originalButtonColor = ((ColorDrawable) keyButton.getBackground()).getColor();
//        final int originalButtonColor2 = ((ColorDrawable) valueButton.getBackground()).getColor();

        keyButton.setBackgroundColor(Color.RED);
        valueButton.setBackgroundColor(Color.RED);

//        new Handler().postDelayed(() -> {
//            keyButton.setBackgroundColor(Color.GREEN);
//            valueButton.setBackgroundColor(Color.GREEN);
//        }, 5000);


    }



}