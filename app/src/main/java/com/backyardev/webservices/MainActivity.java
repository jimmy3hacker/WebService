package com.backyardev.webservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {


    Button btnPNR,btnTrain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        btnPNR=findViewById( R.id.btnPNR );
        btnTrain=findViewById( R.id.btnTrain );

        btnPNR.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,PnrStatus.class);
                startActivity( i );
            }
        } );

        btnTrain.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,TrainDetails.class);
                startActivity( i );
            }
        } );

    }

}
