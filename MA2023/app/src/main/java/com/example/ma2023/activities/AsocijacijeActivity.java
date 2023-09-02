package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ma2023.Konekcija;
import com.example.ma2023.R;
import com.example.ma2023.model.Asocijacija;
import com.example.ma2023.service.AsocijacijaService;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class AsocijacijeActivity extends AppCompatActivity implements AsocijacijaService.OnAsocijacijeLoadedListener {
    List<Asocijacija> asocijacijeZaIgru;
    private Socket mSocket;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Konekcija app = (Konekcija) AsocijacijeActivity.this.getApplication();
        this.mSocket = app.getSocket();
        AsocijacijaService asocijacijaService = new AsocijacijaService(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asocijacije);
        Intent intent = getIntent();
        TextView aName = findViewById(R.id.textViewBName2);
        TextView bName = findViewById(R.id.textViewAName2);
        TextView aScore = findViewById(R.id.editTextBName6);
        TextView bScore = findViewById(R.id.editTextBName7);
        aName.setText(intent.getStringExtra("aName"));
        bName.setText(intent.getStringExtra("bName"));
        aScore.setText(intent.getStringExtra("aScore"));
        bScore.setText(intent.getStringExtra("bScore"));

        //idemo
        Button b1Button = findViewById(R.id.button6nB1);
        Button b2Button = findViewById(R.id.button6nB2);
        Button b3Button = findViewById(R.id.button6nB3);
        Button b4Button = findViewById(R.id.button6nB4);
        Button finalWordButton = findViewById(R.id.button6nKonacno);
        Button aButton = findViewById(R.id.button6nA);
        Button bButton = findViewById(R.id.button6nB);
        Button cButton = findViewById(R.id.button6nC);
        Button c4Button = findViewById(R.id.button6nC4);
        Button c3Button = findViewById(R.id.button6nC3);
        Button c2Button = findViewById(R.id.button6nC2);
        Button c1Button = findViewById(R.id.button6nC1);
        Button dButton = findViewById(R.id.button6nD);
        Button d4Button = findViewById(R.id.button6nD4);
        Button d3Button = findViewById(R.id.button6nD3);
        Button d2Button = findViewById(R.id.button6nD2);
        Button d1Button = findViewById(R.id.button6nD1);
        Button a1Button = findViewById(R.id.button6nA1);
        Button a2Button = findViewById(R.id.button6nA2);
        Button a3Button = findViewById(R.id.button6nA3);
        Button a4Button = findViewById(R.id.button6nA4);
// ...



        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.button6nA1:
                        displayAndDisableButton(a1Button, asocijacijeZaIgru.get(0).getKolone().get(0).getPolja().get(0));
                        break;

                    case R.id.button6nA2:
                        displayAndDisableButton(a2Button, asocijacijeZaIgru.get(0).getKolone().get(0).getPolja().get(1));
                        break;

                    case R.id.button6nA3:
                        displayAndDisableButton(a3Button, asocijacijeZaIgru.get(0).getKolone().get(0).getPolja().get(2));
                        break;

                    case R.id.button6nA4:
                        displayAndDisableButton(a4Button, asocijacijeZaIgru.get(0).getKolone().get(0).getPolja().get(3));
                        break;

                    case R.id.button6nB1:
                        displayAndDisableButton(b1Button, asocijacijeZaIgru.get(0).getKolone().get(1).getPolja().get(0));
                        break;

                    case R.id.button6nB2:
                        displayAndDisableButton(b2Button, asocijacijeZaIgru.get(0).getKolone().get(1).getPolja().get(1));
                        break;

                    case R.id.button6nB3:
                        displayAndDisableButton(b3Button, asocijacijeZaIgru.get(0).getKolone().get(1).getPolja().get(2));
                        break;

                    case R.id.button6nB4:
                        displayAndDisableButton(b4Button, asocijacijeZaIgru.get(0).getKolone().get(1).getPolja().get(3));
                        break;
                        
                    case R.id.button6nC1:
                        displayAndDisableButton(c1Button, asocijacijeZaIgru.get(0).getKolone().get(2).getPolja().get(0));
                        break;

                    case R.id.button6nC2:
                        displayAndDisableButton(c2Button, asocijacijeZaIgru.get(0).getKolone().get(2).getPolja().get(1));
                        break;

                    case R.id.button6nC3:
                        displayAndDisableButton(c3Button, asocijacijeZaIgru.get(0).getKolone().get(2).getPolja().get(2));
                        break;

                    case R.id.button6nC4:
                        displayAndDisableButton(c4Button, asocijacijeZaIgru.get(0).getKolone().get(2).getPolja().get(3));
                        break;

                    case R.id.button6nD1:
                        displayAndDisableButton(d1Button, asocijacijeZaIgru.get(0).getKolone().get(3).getPolja().get(0));
                        break;

                    case R.id.button6nD2:
                        displayAndDisableButton(d2Button, asocijacijeZaIgru.get(0).getKolone().get(3).getPolja().get(1));
                        break;

                    case R.id.button6nD3:
                        displayAndDisableButton(d3Button, asocijacijeZaIgru.get(0).getKolone().get(3).getPolja().get(2));
                        break;

                    case R.id.button6nD4:
                        displayAndDisableButton(d4Button, asocijacijeZaIgru.get(0).getKolone().get(3).getPolja().get(3));
                        break;
                    case R.id.button6nKonacno:
                        // Handle the click on the final word button here
                        break;
                }
            }
        };

        b1Button.setOnClickListener(buttonClickListener);
        b2Button.setOnClickListener(buttonClickListener);
        b3Button.setOnClickListener(buttonClickListener);
        b4Button.setOnClickListener(buttonClickListener);
        finalWordButton.setOnClickListener(buttonClickListener);
        aButton.setOnClickListener(buttonClickListener);
        bButton.setOnClickListener(buttonClickListener);
        cButton.setOnClickListener(buttonClickListener);
        c4Button.setOnClickListener(buttonClickListener);
        c3Button.setOnClickListener(buttonClickListener);
        c2Button.setOnClickListener(buttonClickListener);
        c1Button.setOnClickListener(buttonClickListener);
        dButton.setOnClickListener(buttonClickListener);
        d4Button.setOnClickListener(buttonClickListener);
        d3Button.setOnClickListener(buttonClickListener);
        d2Button.setOnClickListener(buttonClickListener);
        d1Button.setOnClickListener(buttonClickListener);
        a1Button.setOnClickListener(buttonClickListener);
        a2Button.setOnClickListener(buttonClickListener);
        a3Button.setOnClickListener(buttonClickListener);
        a4Button.setOnClickListener(buttonClickListener);
        a1Button.setTag(false);
        a2Button.setTag(false);
        a3Button.setTag(false);
        a4Button.setTag(false);
        b1Button.setTag(false);
        b2Button.setTag(false);
        b3Button.setTag(false);
        b4Button.setTag(false);
        c1Button.setTag(false);
        c2Button.setTag(false);
        c3Button.setTag(false);
        c4Button.setTag(false);
        d1Button.setTag(false);
        d2Button.setTag(false);
        d3Button.setTag(false);
        d4Button.setTag(false);
        aButton.setTag(false);
        bButton.setTag(false);
        cButton.setTag(false);
        dButton.setTag(false);
        finalWordButton.setTag(false);


        mSocket.on("buttonClickedClient", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                int buttonId = data.optInt("buttonId");
                String revealedValue = data.optString("revealedValue");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button button = findViewById(buttonId);
                        button.setText(revealedValue);
                        button.setEnabled(false);
                        button.setTag(true);
                    }
                });
            }
        });

        mSocket.on("turnChange", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String currentPlayer = data.optString("currentPlayer");
                Log.d("TurnChange", "Current Player: " + currentPlayer);
                // Update the UI based on the currentPlayer
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mSocket.id().equals(currentPlayer)) {
                            toggleAllButtons(true);
                        } else {
                            toggleAllButtons(false);
                        }
                    }

                    private void toggleAllButtons(boolean enable) {

                        Button[] buttons = new Button[]{
                                b1Button, b2Button, b3Button, b4Button, finalWordButton, aButton, bButton,
                                cButton, c4Button, c3Button, c2Button, c1Button, dButton,
                                d4Button, d3Button, d2Button, d1Button, a1Button, a2Button, a3Button, a4Button
                        };

                        for(Button button: buttons) {
                            boolean isRevealed = (boolean) button.getTag();
                            if (!isRevealed) {
                                button.setEnabled(enable);
                            }
                        }
                    }


                });
            }
        });


        //SLEDECA IGRA dugme
        final Button btn6n1 = findViewById(R.id.button6nSledecaIgra);
        btn6n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AsocijacijeActivity.this, SkockoActivity.class);
                startActivity(intent);
            }
        });



    }




    private void displayAndDisableButton(Button button, String buttonText) {
        Log.d("asocijacije", button.getId() + " tekst:" + buttonText);
        JSONObject data = new JSONObject();
        try {
            data.put("buttonId", button.getId());
            data.put("revealedValue", buttonText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("buttonClicked", data);

        button.setEnabled(false);

        button.setTag(true);
        changeTurn();
    }


    private void changeTurn() {
        mSocket.emit("requestTurnChange");
    }


    @Override
    public void onAsocijacijeLoaded(List<Asocijacija> asocijacije) {
        asocijacijeZaIgru = asocijacije;
        Log.d("asocijacije", "Pitanja ucitana");
    }
}