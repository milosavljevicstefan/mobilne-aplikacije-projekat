package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ma2023.Konekcija;
import com.example.ma2023.R;
import com.example.ma2023.model.Asocijacija;
import com.example.ma2023.model.Kolona;
import com.example.ma2023.service.AsocijacijaService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class AsocijacijeActivity extends AppCompatActivity implements AsocijacijaService.OnAsocijacijeLoadedListener {

    private Button a1Button, a2Button, a3Button, a4Button;
    private Button b1Button, b2Button, b3Button, b4Button;
    private Button c1Button, c2Button, c3Button, c4Button;
    private Button d1Button, d2Button, d3Button, d4Button;
    private Button aButton, bButton, cButton, dButton, finalWordButton;

    private int value = 0;

    private Button selectedButton = null;
    private List<Button> clues = null;
    private List<String> cluesText = null;
    List<Asocijacija> asocijacijeZaIgru;

    private Asocijacija trenutnaAsocijacija;
    private AtomicInteger runda = new AtomicInteger();
    private Socket mSocket;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Konekcija app = (Konekcija) AsocijacijeActivity.this.getApplication();
        this.mSocket = app.getSocket();
        auth = FirebaseAuth.getInstance();
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





        b1Button = findViewById(R.id.button6nB1);
        b2Button = findViewById(R.id.button6nB2);
        b3Button = findViewById(R.id.button6nB3);
        b4Button = findViewById(R.id.button6nB4);
        finalWordButton = findViewById(R.id.button6nKonacno);
        aButton = findViewById(R.id.button6nA);
        bButton = findViewById(R.id.button6nB);
        cButton = findViewById(R.id.button6nC);
        c4Button = findViewById(R.id.button6nC4);
        c3Button = findViewById(R.id.button6nC3);
        c2Button = findViewById(R.id.button6nC2);
        c1Button = findViewById(R.id.button6nC1);
        dButton = findViewById(R.id.button6nD);
        d4Button = findViewById(R.id.button6nD4);
        d3Button = findViewById(R.id.button6nD3);
        d2Button = findViewById(R.id.button6nD2);
        d1Button = findViewById(R.id.button6nD1);
        a1Button = findViewById(R.id.button6nA1);
        a2Button = findViewById(R.id.button6nA2);
        a3Button = findViewById(R.id.button6nA3);
        a4Button = findViewById(R.id.button6nA4);
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



        mSocket.on("spremiIgru", (a) -> {
            Log.d("asocijacije", "Usao u spremiIgru" + Integer.valueOf(String.valueOf(runda)));
            if (Integer.valueOf(String.valueOf(runda)) < 3) {
                if (asocijacijeZaIgru != null && !asocijacijeZaIgru.isEmpty()) {
                    Asocijacija asocijacija = asocijacijeZaIgru.get(runda.getAndIncrement());
                    JSONObject runduData = prepareRunduData(asocijacija);
                    this.mSocket.emit("runduDataRedirect", runduData.toString()); // Emit to all sockets
                }
            }
            else if (Integer.valueOf(String.valueOf(runda)) == 3) {
                    Log.d("asocijacije", "kraj");
//                intent.putExtra("aName", aName.getText());
//                intent.putExtra("bName", bName.getText());
//                intent.putExtra("aScore", aScore.getText());
//                intent.putExtra("bScore", bScore.getText());
//                this.mSocket.emit("pocniSpojnice");
            }
        });

        mSocket.on("runduData", (data) -> {
            Log.d("asocijacije", "usao u runduData");
            Log.d("asocijacije", "Received data: " + data[0].toString());
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true );
                Asocijacija asocijacija = mapper.readValue(data[0].toString(), Asocijacija.class);
                Log.d("asocijacije", "Received data: " + asocijacija.toString());
                spremiRundu(asocijacija);
                FirebaseUser currentUser = auth.getCurrentUser();
                Log.d("asocijacije", bName.getText().toString() + currentUser);
                if (currentUser.getDisplayName().trim().equals(bName.getText().toString().trim())) {
                    changeTurn();
                } else {
                    simulateProgressBar();
                }
            } catch (IOException e) {
                Log.d("asocijacije", "Exception while parsing JSON");
                e.printStackTrace();
            }
        });


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

                // Check if this device is the current player
                boolean isCurrentPlayer = mSocket.id().equals(currentPlayer);

                // Update the UI based on the currentPlayer
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isCurrentPlayer) {
                            toggleAllButtons(true);
                            if (isCurrentPlayer) {
                                simulateProgressBar();
                            }
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

                        for (Button button : buttons) {
                            boolean isRevealed = (boolean) button.getTag();
                            if (!isRevealed) {
                                button.setEnabled(enable);
                            }
                        }
                    }
                });
            }
        });


        mSocket.on("respondOdgovorAsocijacije", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (args.length > 0) {
                            JSONObject data = (JSONObject) args[0];
                            try {
                                int bodovi = data.getInt("bodovi");
                                int playerIndex = data.getInt("playerIndex");
                                if (playerIndex == 0) {
                                    TextView aScore = findViewById(R.id.editTextBName6);
                                    if (aScore != null) {
                                        Log.d("asocijacije", "Nasao button!" + aScore.getText());
                                        aScore.setText(String.valueOf(Integer.valueOf(aScore.getText().toString().trim()) + bodovi));
                                    } else {
                                        Log.d("asocijacije", "Greska u aScoru!" + aScore.getText());
                                    }
                                } else if (playerIndex == 1) {
                                    TextView bScore = findViewById(R.id.editTextBName7);
                                    if (bScore != null) {
                                        Log.d("asocijacije", "Nasao button!" + bScore.getText());
                                        bScore.setText(String.valueOf(Integer.valueOf(bScore.getText().toString().trim()) + bodovi));
                                    } else {
                                        Log.d("asocijacije", "Greska u bScoru!" + bScore.getText());
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });



        //SLEDECA IGRA dugme
        final Button btn6n1 = findViewById(R.id.buttonSubmit);
        btn6n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AsocijacijeActivity.this, SkockoActivity.class);
                startActivity(intent);
            }
        });



    }

    private void spremiRundu(Asocijacija asocijacija) {
        trenutnaAsocijacija = asocijacija;
        b1Button = findViewById(R.id.button6nB1);
        b2Button = findViewById(R.id.button6nB2);
        b3Button = findViewById(R.id.button6nB3);
        b4Button = findViewById(R.id.button6nB4);
        finalWordButton = findViewById(R.id.button6nKonacno);
        aButton = findViewById(R.id.button6nA);
        bButton = findViewById(R.id.button6nB);
        cButton = findViewById(R.id.button6nC);
        c4Button = findViewById(R.id.button6nC4);
        c3Button = findViewById(R.id.button6nC3);
        c2Button = findViewById(R.id.button6nC2);
        c1Button = findViewById(R.id.button6nC1);
        dButton = findViewById(R.id.button6nD);
        d4Button = findViewById(R.id.button6nD4);
        d3Button = findViewById(R.id.button6nD3);
        d2Button = findViewById(R.id.button6nD2);
        d1Button = findViewById(R.id.button6nD1);
        a1Button = findViewById(R.id.button6nA1);
        a2Button = findViewById(R.id.button6nA2);
        a3Button = findViewById(R.id.button6nA3);
        a4Button = findViewById(R.id.button6nA4);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.button6nA1:
                        displayAndDisableButton(a1Button, asocijacija.getKolone().get(0).getPolja().get(0),asocijacija.getKolone().get(0).getResenjeKolone());
                        break;

                    case R.id.button6nA2:
                        displayAndDisableButton(a2Button, asocijacija.getKolone().get(0).getPolja().get(1), asocijacija.getKolone().get(0).getResenjeKolone());
                        break;

                    case R.id.button6nA3:
                        displayAndDisableButton(a3Button, asocijacija.getKolone().get(0).getPolja().get(2), asocijacija.getKolone().get(0).getResenjeKolone());
                        break;

                    case R.id.button6nA4:
                        displayAndDisableButton(a4Button, asocijacija.getKolone().get(0).getPolja().get(3), asocijacija.getKolone().get(0).getResenjeKolone());
                        break;

                    case R.id.button6nB1:
                        displayAndDisableButton(b1Button, asocijacija.getKolone().get(1).getPolja().get(0), asocijacija.getKolone().get(1).getResenjeKolone());
                        break;

                    case R.id.button6nB2:
                        displayAndDisableButton(b2Button, asocijacija.getKolone().get(1).getPolja().get(1), asocijacija.getKolone().get(1).getResenjeKolone());
                        break;

                    case R.id.button6nB3:
                        displayAndDisableButton(b3Button, asocijacija.getKolone().get(1).getPolja().get(2), asocijacija.getKolone().get(1).getResenjeKolone());
                        break;

                    case R.id.button6nB4:
                        displayAndDisableButton(b4Button, asocijacija.getKolone().get(1).getPolja().get(3), asocijacija.getKolone().get(1).getResenjeKolone());
                        break;

                    case R.id.button6nC1:
                        displayAndDisableButton(c1Button, asocijacija.getKolone().get(2).getPolja().get(0), asocijacija.getKolone().get(2).getResenjeKolone());
                        break;

                    case R.id.button6nC2:
                        displayAndDisableButton(c2Button, asocijacija.getKolone().get(2).getPolja().get(1), asocijacija.getKolone().get(2).getResenjeKolone());
                        break;

                    case R.id.button6nC3:
                        displayAndDisableButton(c3Button, asocijacija.getKolone().get(2).getPolja().get(2), asocijacija.getKolone().get(2).getResenjeKolone());
                        break;

                    case R.id.button6nC4:
                        displayAndDisableButton(c4Button, asocijacija.getKolone().get(2).getPolja().get(3), asocijacija.getKolone().get(2).getResenjeKolone());
                        break;

                    case R.id.button6nD1:
                        displayAndDisableButton(d1Button, asocijacija.getKolone().get(3).getPolja().get(0), asocijacija.getKolone().get(3).getResenjeKolone());
                        break;

                    case R.id.button6nD2:
                        displayAndDisableButton(d2Button, asocijacija.getKolone().get(3).getPolja().get(1), asocijacija.getKolone().get(3).getResenjeKolone());
                        break;

                    case R.id.button6nD3:
                        displayAndDisableButton(d3Button, asocijacija.getKolone().get(3).getPolja().get(2), asocijacija.getKolone().get(3).getResenjeKolone());
                        break;

                    case R.id.button6nD4:
                        displayAndDisableButton(d4Button, asocijacija.getKolone().get(3).getPolja().get(3), asocijacija.getKolone().get(3).getResenjeKolone());
                        break;
                    case R.id.button6nKonacno:
                        selectMainButtons(finalWordButton, asocijacija.getKonacnoResenje());
                        break;
                    case R.id.button6nA:
                        selectMainButtons(aButton, asocijacija.getKolone().get(0).getResenjeKolone());
                        break;
                    case R.id.button6nB:
                        selectMainButtons(bButton, asocijacija.getKolone().get(1).getResenjeKolone());
                        break;
                    case R.id.button6nC:
                        selectMainButtons(cButton, asocijacija.getKolone().get(2).getResenjeKolone());
                        break;
                    case R.id.button6nD:
                        selectMainButtons(dButton, asocijacija.getKolone().get(3).getResenjeKolone());
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
    }



    private JSONObject prepareRunduData(Asocijacija asocijacija) {
        JSONObject data = new JSONObject();
        try {
            JSONArray koloneArray = new JSONArray();

            // Iterate through the Kolona objects in the Asocijacija
            for (Kolona kolona : asocijacija.getKolone()) {
                JSONObject kolonaObject = new JSONObject();
                kolonaObject.put("resenjeKolone", kolona.getResenjeKolone());

                // Convert the List of polja in Kolona to a JSONArray
                JSONArray poljaArray = new JSONArray(kolona.getPolja());
                kolonaObject.put("polja", poljaArray);

                koloneArray.put(kolonaObject);
            }

            // Add the koloneArray and konacnoResenje to the data object
            data.put("kolone", koloneArray);
            data.put("konacnoResenje", asocijacija.getKonacnoResenje());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void selectMainButtons(Button button, String correctAnswer) {
        b1Button = findViewById(R.id.button6nB1);
        b2Button = findViewById(R.id.button6nB2);
        b3Button = findViewById(R.id.button6nB3);
        b4Button = findViewById(R.id.button6nB4);
        finalWordButton = findViewById(R.id.button6nKonacno);
        aButton = findViewById(R.id.button6nA);
        bButton = findViewById(R.id.button6nB);
        cButton = findViewById(R.id.button6nC);
        c4Button = findViewById(R.id.button6nC4);
        c3Button = findViewById(R.id.button6nC3);
        c2Button = findViewById(R.id.button6nC2);
        c1Button = findViewById(R.id.button6nC1);
        dButton = findViewById(R.id.button6nD);
        d4Button = findViewById(R.id.button6nD4);
        d3Button = findViewById(R.id.button6nD3);
        d2Button = findViewById(R.id.button6nD2);
        d1Button = findViewById(R.id.button6nD1);
        a1Button = findViewById(R.id.button6nA1);
        a2Button = findViewById(R.id.button6nA2);
        a3Button = findViewById(R.id.button6nA3);
        a4Button = findViewById(R.id.button6nA4);
        EditText editTextGuess = findViewById(R.id.editTextInput);
        Button buttonSubmitGuess = findViewById(R.id.buttonSubmit);
        Log.d("asocijacije", "usau u select" + selectedButton);
        if (selectedButton != null) {
//             Deselect the button
//            selectedButton.setBackgroundResource(R.color.purple_500);
            selectedButton = null;
            clues = null;
        } else {
            // Select the button
            selectedButton = button;
            clues = new ArrayList<>();
            cluesText = new ArrayList<>();
            switch (selectedButton.getText().toString()) {
                case "A":
                    clues.add(a1Button);
                    clues.add(a2Button);
                    clues.add(a3Button);
                    clues.add(a4Button);
                    cluesText = trenutnaAsocijacija.getKolone().get(0).getPolja();
                    break;
                case "B":
                    clues.add(b1Button);
                    clues.add(b1Button);
                    clues.add(b1Button);
                    clues.add(b1Button);
                    cluesText = trenutnaAsocijacija.getKolone().get(1).getPolja();
                    break;
                case "C":
                    clues.add(c1Button);
                    clues.add(c1Button);
                    clues.add(c1Button);
                    clues.add(c1Button);
                    cluesText = trenutnaAsocijacija.getKolone().get(2).getPolja();

                    break;
                case "D":
                    clues.add(d1Button);
                    clues.add(d1Button);
                    clues.add(d1Button);
                    clues.add(d1Button);
                    cluesText = trenutnaAsocijacija.getKolone().get(3).getPolja();

                    break;
                case "konacno":
                    clues.add(aButton);
                    clues.add(bButton);
                    clues.add(cButton);
                    clues.add(dButton);
                    cluesText.add(trenutnaAsocijacija.getKolone().get(0).getResenjeKolone());
                    cluesText.add(trenutnaAsocijacija.getKolone().get(1).getResenjeKolone());
                    cluesText.add(trenutnaAsocijacija.getKolone().get(2).getResenjeKolone());
                    cluesText.add(trenutnaAsocijacija.getKolone().get(3).getResenjeKolone());
                    break;
            }
            editTextGuess.setVisibility(View.VISIBLE);
            buttonSubmitGuess.setVisibility(View.VISIBLE);
            buttonSubmitGuess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userGuess = editTextGuess.getText().toString().trim();

                    // Check if selectedButton is null
                    if (selectedButton == null) {
                        // Display a toast message
                        ToastIzaberi();
                        return;
                    }

                    if (userGuess.equalsIgnoreCase(correctAnswer)) {
                        Log.d("asocijacije", "pogodak");
                        displayGuessedClues(correctAnswer);
                        if (button.getId() == R.id.button6nKonacno) {
                            JSONObject finalWordData = new JSONObject();
                            try {
                                finalWordData.put("correctAnswer", correctAnswer);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mSocket.emit("sledecaRundaKoZnaZna", finalWordData);
                        }
                        changeTurn();
                    } else {
                        Log.d("asocijacije", "odgodak");
                        changeTurn();
                    }
                    editTextGuess.getText().clear();
                    editTextGuess.setVisibility(View.GONE);
                    buttonSubmitGuess.setVisibility(View.GONE);


                }

            });
            Toast(selectedButton.getText().toString());
//            selectedButton.setBackgroundResource(R.color.purple_700);
        }
    }

    private void Toast(String text) {
        Toast.makeText(this, "Izabrano resenje kolone: " + text, Toast.LENGTH_SHORT).show();
    }

    private void displayAndDisableButton(Button button, String buttonText, String correctAnswer) {
        Log.d("asocijacije", "aktivirano? " + String.valueOf(button.isEnabled()));
        EditText editTextGuess = findViewById(R.id.editTextInput);
        Button buttonSubmitGuess = findViewById(R.id.buttonSubmit);

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
        Button[] buttons = new Button[]{
                findViewById(R.id.button6nB1),
                findViewById(R.id.button6nB2),
                findViewById(R.id.button6nB3),
                findViewById(R.id.button6nB4),
                findViewById(R.id.button6nC4),
                findViewById(R.id.button6nC3),
                findViewById(R.id.button6nC2),
                findViewById(R.id.button6nC1),
                findViewById(R.id.button6nD4),
                findViewById(R.id.button6nD3),
                findViewById(R.id.button6nD2),
                findViewById(R.id.button6nD1),
                findViewById(R.id.button6nA1),
                findViewById(R.id.button6nA2),
                findViewById(R.id.button6nA3),
                findViewById(R.id.button6nA4)
        };
        for (Button b : buttons) {
            b.setEnabled(false);
        }
        button.setTag(true);
        editTextGuess.setVisibility(View.VISIBLE);
        buttonSubmitGuess.setVisibility(View.VISIBLE);

        // Set an onClickListener for the submit Button
        buttonSubmitGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userGuess = editTextGuess.getText().toString().trim();

                // Check if selectedButton is null
                if (selectedButton == null) {
                    // Display a toast message
                    ToastIzaberi();
                    return;
                }

                if (userGuess.equalsIgnoreCase(correctAnswer)) {
                    Log.d("asocijacije", "pogodak");
                    displayGuessedClues(correctAnswer);
                    if (button.getId() == R.id.button6nKonacno) {
                        JSONObject finalWordData = new JSONObject();
                        try {
                            finalWordData.put("correctAnswer", correctAnswer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mSocket.emit("sledecaRundaKoZnaZna", finalWordData);
                    }
                    changeTurn();
                } else {
                    Log.d("asocijacije", "odgodak");
                    changeTurn();
                }
                editTextGuess.getText().clear();
                editTextGuess.setVisibility(View.GONE);
                buttonSubmitGuess.setVisibility(View.GONE);


            }

        });


    }

    private void displayGuessedClues(String correctAnswer) {
        int neotkrivenaPolja = 0;
        JSONObject data = new JSONObject();
        try {
            data.put("buttonId", selectedButton.getId());
            data.put("revealedValue", correctAnswer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("buttonClicked", data);
        for (int i = 0; i < 4; i++) {
            if (!(Boolean) clues.get(i).getTag()) {
                neotkrivenaPolja++;
            }
            data = new JSONObject();
            try {
                data.put("buttonId", clues.get(i).getId());
                data.put("revealedValue", cluesText.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("buttonClicked", data);
        }
        int bodovi = 2 + neotkrivenaPolja;

        mSocket.emit("tacanOdgovorAsocijacije", bodovi);
        Log.d("asocijacije", "broj neotkrivenih polja: " + neotkrivenaPolja);

    }

    private void ToastIzaberi() {
        Toast.makeText(this, "Molim vas odaberite resenje kolone koje zelite da odgovorite", Toast.LENGTH_SHORT).show();
    }


    private void changeTurn() {
        value = -15;
        mSocket.emit("requestTurnChange");
    }


    @Override
    public void onAsocijacijeLoaded(List<Asocijacija> asocijacije) {
        asocijacijeZaIgru = asocijacije;
        Log.d("asocijacije", "Pitanja ucitana");
        mSocket.emit("pitanjaReady");
    }

    private void simulateProgressBar() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {


            @Override
            public void run() {
                value += 10;
                if (value > 0 && value <= 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateBar(value);
                        }
                    });
                } else if (value < 0) {
                    timer.cancel();
                    value = 0;
                } else {
                    timer.cancel();
                    changeTurn();
                }
            }
        };

        timer.schedule(task, 0, 2500); // Run task every 2.5 seconds

    }
    private void updateBar(int value) {
        ProgressBar pBar = findViewById(R.id.progressBar2);
        pBar.setProgress(value);
    }
}