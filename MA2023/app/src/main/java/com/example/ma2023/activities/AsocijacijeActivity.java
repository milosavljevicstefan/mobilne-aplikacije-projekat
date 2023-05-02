package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ma2023.R;

public class AsocijacijeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asocijacije);

//        //A1
//        final Button btn6nA1 = findViewById(R.id.button6nA1);
//        btn6nA1.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //A2
////        final Button btn6nA2 = findViewById(R.id.button6nA2);
////        btn6nA2.setOnClickListener(new ?.OnClickListener() {
////            public void onClick() {
////                Intent intent = new Intent();
////                startActivity(intent);
////            }
////        });

//        //A3
//        final Button btn6nA3 = findViewById(R.id.button6nA3);
//        btn6nA3.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //A4
//        final Button btn6nA4 = findViewById(R.id.button6nA4);
//        btn6nA4.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });


//        //A konacno
//        final Button btn6nA = findViewById(R.id.button6nA);
//        btn6nA.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });


//        //B1
//        final Button btn6nB1 = findViewById(R.id.button6nB1);
//        btn6nB1.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //B2
//        final Button btn6nB2 = findViewById(R.id.button6nB2);
//        btn6nB2.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //B3
//        final Button btn6nB3 = findViewById(R.id.button6nB3);
//        btn6nB3.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //B4
//        final Button btn6nB4 = findViewById(R.id.button6nB4);
//        btn6nB4.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });


//        //B konacno
//        final Button btn6nB = findViewById(R.id.button6nB);
//        btn6nB.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //C1
//        final Button btn6nC1 = findViewById(R.id.button6nC1);
//        btn6nC1.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });


//        //C2
//        final Button btn6nC2 = findViewById(R.id.button6nC2);
//        btn6nC2.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });


//        //C3
//        final Button btn6nC3 = findViewById(R.id.button6nC3);
//        btn6nC3.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });


//        //C4
//        final Button btn6nC4 = findViewById(R.id.button6nC4);
//        btn6nC4.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //C konacno
//        final Button btn6nC = findViewById(R.id.button6nC);
//        btn6nC.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });



//        //D1
//        final Button btn6nD1 = findViewById(R.id.button6nD1);
//        btn6nD1.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //D2
//        final Button btn6nD2 = findViewById(R.id.button6nD2);
//        btn6nD2.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //D3
//        final Button btn6nD3 = findViewById(R.id.button6nD3);
//        btn6nD3.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //D4
//        final Button btn6nD4 = findViewById(R.id.button6nD4);
//        btn6nD4.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        //D konacno
//        final Button btn6nD = findViewById(R.id.button6nD);
//        btn6nD.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });



        //SLEDECA IGRA dugme
        final Button btn6n1 = findViewById(R.id.button6nSledecaIgra);
        btn6n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AsocijacijeActivity.this, SkockoActivity.class);
                startActivity(intent);
            }
        });
    }


}