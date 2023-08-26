package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ma2023.Konekcija;
import com.example.ma2023.R;
import com.example.ma2023.model.Pitanje;
import com.example.ma2023.service.PitanjaService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class KoZnaZnaActivity extends AppCompatActivity implements PitanjaService.OnPitanjaLoadedListener {


    private Socket mSocket;
    List<Pitanje> pitanjaZaIgru;
    private AtomicInteger runda = new AtomicInteger();

    ArrayList<Button> buttons = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Konekcija app = (Konekcija) KoZnaZnaActivity.this.getApplication();
        this.mSocket = app.getSocket();
        PitanjaService pitanjaService = new PitanjaService(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ko_zna_zna);
        Intent intent = getIntent();
        TextView aName = findViewById(R.id.editTextAName);
        TextView bName = findViewById(R.id.editTextBName);
        TextView aScore = findViewById(R.id.editTextBName4);
        TextView bScore = findViewById(R.id.editTextBName3);
        aName.setText(intent.getStringExtra("aName"));
        bName.setText(intent.getStringExtra("bName"));
        //ovo ne radi
        aScore.setText(intent.getStringExtra("aScore"));
        bScore.setText(intent.getStringExtra("bScore"));


        //ODG1 dugme
        final Button btn4n1 = findViewById(R.id.button4n1);
        btn4n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

//        ODG2 dugme
        final Button btn4n2 = findViewById(R.id.button4n2);
        btn4n2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

//        ODG3 dugme
        final Button btn4n3 = findViewById(R.id.button4n3);
        btn4n3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

//        ODG4 dugme
        final Button btn4n4 = findViewById(R.id.button4n4);
        btn4n4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        buttons.add(btn4n1);
        buttons.add(btn4n2);
        buttons.add(btn4n3);
        buttons.add(btn4n4);

        mSocket.on("spremiIgru", (a) -> {
            Log.d("koZnaZna", "Usao u spremiIgru" + Integer.valueOf(String.valueOf(runda)));
            if (Integer.valueOf(String.valueOf(runda)) < 5) {
                if (pitanjaZaIgru != null && !pitanjaZaIgru.isEmpty()) {
                    Pitanje pitanje = pitanjaZaIgru.get(runda.getAndIncrement());
                    JSONObject runduData = prepareRunduData(pitanje);
                    this.mSocket.emit("runduDataRedirect", runduData.toString()); // Emit to all sockets
                }
            }
            else if (Integer.valueOf(String.valueOf(runda)) == 5) {
                intent.putExtra("aName", aName.getText());
                intent.putExtra("bName", bName.getText());
                intent.putExtra("aScore", aScore.getText());
                intent.putExtra("bScore", bScore.getText());
                this.mSocket.emit("pocniSpojnice");
            }
        });

        mSocket.on("runduData", (data) -> {
            Log.d("koZnaZna", "usao u runduData");
            Log.d("koZnaZna", "Received data: " + data[0].toString());
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true );
                Pitanje pitanje = mapper.readValue(data[0].toString(), Pitanje.class);
                Log.d("koZnaZna", "Received data: " + pitanje.toString());
                spremiRundu(pitanje, buttons);
            } catch (IOException e) {
                Log.d("koZnaZna", "Exception while parsing JSON");
                e.printStackTrace();
            }
        });

        mSocket.on("updateBar", (value) -> {
           updateBar(Integer.valueOf(String.valueOf(value)));
        });

        mSocket.on("obradaBodovaKoZnaZna", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length >= 2) {
                    int bodoviA = (int) args[0];
                    int bodoviB = (int) args[1];

                    // Update the UI with the received points
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int currentAScore = Integer.parseInt(aScore.getText().toString());
                            int currentBScore = Integer.parseInt(bScore.getText().toString());

                            aScore.setText(String.valueOf(currentAScore + bodoviA));
                            bScore.setText(String.valueOf(currentBScore + bodoviB));
                            mSocket.emit("sledecaRundaKoZnaZna");
                        }
                    });
                }
            }
        });

        mSocket.on("pocetakSpojniceJava", (data) -> {

            Intent intentSpojnice = new Intent(KoZnaZnaActivity.this, SpojniceActivity.class);
            //put data in intent
            intentSpojnice.putExtra("aName", aName.getText());
            intentSpojnice.putExtra("bName", bName.getText());
            intentSpojnice.putExtra("aScore", aScore.getText());
            intentSpojnice.putExtra("bScore", bScore.getText());
//            intentSpojnice.putExtra("turn", turn);
            startActivity(intentSpojnice);
        });
        //SLEDECA IGRA dugme
        final Button btn4n5 = findViewById(R.id.button4n5);
        btn4n5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.valueOf(String.valueOf(runda)) < 5) {
                    mSocket.emit("sledecaRundaKoZnaZna");
                } else {
                    Intent intent = new Intent(KoZnaZnaActivity.this, SpojniceActivity.class);
                    startActivity(intent);
                }
            }
        });
    }



    private void spremiRundu(Pitanje pitanje, ArrayList<Button> buttons) {
        int finalI = 0;
        Log.d("koZnaZna", pitanje.getTacanOdgovor() + pitanje.getTekstPitanja());
        Random random = new Random();
        TextView pitanjeTextView = findViewById(R.id.textView5);
        pitanjeTextView.setText(pitanje.getTekstPitanja());
        int randomNumber = random.nextInt(4) + 1;
        for(int i = 0; i < 4; i++) {
            if (i + 1 == randomNumber) {
                buttons.get(i).setText(pitanje.getTacanOdgovor());
                buttons.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("koZnaZna", "Uspesan listener");
                        mSocket.emit("tacanOdgovorKoZnaZna");
                    }
                });
            } else {
                buttons.get(i).setText(pitanje.getPogresniOdgovori().get(finalI++));
                buttons.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("koZnaZna", "Uspesan listener");
                        mSocket.emit("netacanOdgovorKoZnaZna");
                    }
                });
            }
        }
        simulateProgressBar();
    }

    @Override
    public void onPitanjaLoaded(List<Pitanje> pitanja) {
        pitanjaZaIgru = pitanja;
        Log.d("koZnaZna", "Pitanja ucitana");
        if (mSocket != null) {
            mSocket.emit("pitanjaReady", mSocket.id());
        }
    }

    private JSONObject prepareRunduData(Pitanje pitanje) {
        JSONObject data = new JSONObject();
        try {
            data.put("tekstPitanja", pitanje.getTekstPitanja());
            data.put("tacanOdgovor", pitanje.getTacanOdgovor());
            data.put("pogresniOdgovori", new JSONArray(pitanje.getPogresniOdgovori()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

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
                    mSocket.emit("obradaKoZnaZna");
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
}