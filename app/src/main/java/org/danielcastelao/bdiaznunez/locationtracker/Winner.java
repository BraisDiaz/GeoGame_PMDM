package org.danielcastelao.bdiaznunez.locationtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Winner extends AppCompatActivity {

    TextView txtWinner;

    String lecturaQR;

    Intent intent, winIntent;

    private boolean tes1, tes2, tes3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        txtWinner = (TextView) findViewById(R.id.textViewWin);
        lecturaQR = getIntent().getExtras().getString("LecturaQR");
        Button btnVolver = (Button) findViewById(R.id.btnVolver);

        intent = new Intent(getApplicationContext(),LocationActivity.class);

        winIntent =this.getIntent();

        intent.putExtra("tes1",winIntent.getExtras().getBoolean("tes1"));
        intent.putExtra("tes2",winIntent.getExtras().getBoolean("tes2"));
        intent.putExtra("tes3",winIntent.getExtras().getBoolean("tes3"));

        tes1=winIntent.getExtras().getBoolean("tes1");
        tes2=winIntent.getExtras().getBoolean("tes2");
        tes3=winIntent.getExtras().getBoolean("tes3");

        if(lecturaQR.matches("Lectura QR del primer tesoro")){

            txtWinner.setText("No has empezado mal, ya has encontrado el primer tesoro.");
            intent.putExtra("tes1", true);
            if(tes2 && tes3 == true){

                txtWinner.setText("Felicidades, has ganado!!!");

            }


        }else if(lecturaQR.matches("Lectura QR del segundo tesoro")){

            txtWinner.setText("Enhorabuena, el segundo tesoro ya es tuyo, ya queda poco!!!");
            intent.putExtra("tes2", true);
            if(tes1 && tes3 == true){

                txtWinner.setText("Felicidades, has ganado!!!");


            }

        }else if(lecturaQR.matches("Lectura QR del tercer tesoro")){

            txtWinner.setText("Has encontrado el último tesoro!!!");
            intent.putExtra("tes3", true);
            if(tes1 && tes2 == true){

                txtWinner.setText("Felicidades, has ganado!!!");


            }


        }else{

            txtWinner.setText("Ops, lo sentimos pero te has equivocado de QR :");

        }


        btnVolver.setOnClickListener(new View.OnClickListener(){

        public void onClick(View view){

            startActivity(intent);

        }

    });

}

    @Override

    protected void onStop() {

        super.onStop();
        finish();

    }
}
