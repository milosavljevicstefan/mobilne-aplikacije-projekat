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


        //SLEDECA IGRA dugme
        final Button btn5n11 = findViewById(R.id.button5n11);
        btn5n11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SpojniceActivity.this, AsocijacijeActivity.class);
                startActivity(intent);
            }
        });



        mSocket.on("spremiIgru", (a) -> {
            if (Integer.valueOf(String.valueOf(runda)) < 3) {
                if (spojniceZaIgru != null && !spojniceZaIgru.isEmpty()) {
                    Spojnica spojnica = spojniceZaIgru.get(1);
                    JSONObject runduData = prepareRunduData(spojnica);
                    this.mSocket.emit("runduDataRedirect", runduData.toString()); // Emit to all sockets
                    this.mSocket.emit("prvaRundaSpojnice");
                }
            } else if (Integer.valueOf(String.valueOf(runda)) == 2) {
                intent.putExtra("aName", aName.getText());
                intent.putExtra("bName", bName.getText());
                intent.putExtra("aScore", aScore.getText());
                intent.putExtra("bScore", bScore.getText());
//                this.mSocket.emit("pocniSLEDECU");
            }
        });


        mSocket.on("runduData", (data) -> {
            Log.d("spojnice", "Received data: " + data[0].toString());
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                Spojnica spojnica = mapper.readValue(data[0].toString(), Spojnica.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.mSocket.emit("prvaRundaSpojnice");
        mSocket.on("onemoguciDugmad", (a) -> {
            Log.d("AAAAAAAAAA", "AAAAAAAAAAAAAAAAAAAA");
         onemoguciDugmad();
        });


    }


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

@Override
public void onSpojniceLoaded(List<Spojnica> spojnice) {
    spojniceZaIgru = spojnice;
    if (mSocket != null) {
        mSocket.emit("spremiIgruSA");
    }

    if (spojniceZaIgru != null && !spojniceZaIgru.isEmpty()) {
        int randomIndex = new Random().nextInt(spojniceZaIgru.size());
        Spojnica randomSpojnica = spojniceZaIgru.get(randomIndex);
        TextView pitanjeTextView = findViewById(R.id.textView17);
        pitanjeTextView.setText(randomSpojnica.getTekstPitanja());
//        shuffleButtons();

    }
}


public void onemoguciDugmad() {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            for (Button button : buttons) {
                button.setEnabled(false);
            }
        }
    });
}

    public void shuffleButtons() {
        // Shuffle key (a) buttons
        Collections.shuffle(buttons.subList(0, 5));

        // Shuffle value (b) buttons
        Collections.shuffle(buttons.subList(5, 10));

        // Set text for each button
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            if (i < 5) {
                // Set text for key (a) buttons
                button.setText("Key " + (i + 1)); // You can set the actual text based on your requirements
            } else {
                // Set text for value (b) buttons
                button.setText("Value " + (i - 4)); // You can set the actual text based on your requirements
            }

        }
    }


}







