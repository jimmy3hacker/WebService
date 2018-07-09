package com.backyardev.webservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class PnrStatus extends AppCompatActivity {

    LinearLayout bigLinearLayout,smallLinearLayout;
    String URL;
    String data = null;
    Button btnStatus;
    EditText idPnrEdit;
    TextView opTextTNo,opTextFromStation,opTextToStation,opTextTrainName,opTextPassCount,opTextCurrStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pnr_status );

        btnStatus=findViewById( R.id.btnStatus );
        idPnrEdit=findViewById( R.id.idPnrEdit );
        opTextTrainName=findViewById( R.id.opTextTrainName );
        opTextToStation=findViewById( R.id.opTextToStation );
        opTextFromStation=findViewById( R.id.opTextFromStation );
        opTextTNo=findViewById( R.id.opTextTNo );
        bigLinearLayout=findViewById( R.id.bigLinearLayout );

        btnStatus.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PNR=idPnrEdit.getText().toString();
                URL="https://api.railwayapi.com/v2/pnr-status/pnr/"+PNR+"/apikey/3lmq7wz5ix";
                Log.d("PNR url test",URL);
                try{
                    run();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        } );
    }

    private void run() throws IOException{

        OkHttpClient client =new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        client.newCall(request).enqueue( new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String myResponse= response.body().string();
                PnrStatus.this.runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Log.d("response",myResponse);
                        try{
                            JSONObject responseObject = new JSONObject(myResponse);
                            JSONObject trainObject = responseObject.getJSONObject( "train" );
                            opTextTrainName.setText( trainObject.getString( "name" ));
                            opTextTNo.setText( trainObject.getString( "number" ) );
                            JSONObject fromStationObject = responseObject.getJSONObject( "from_station" );
                            opTextFromStation.setText( fromStationObject.getString( "name" ) );
                            JSONObject toStationObject = responseObject.getJSONObject( "to_station" );
                            opTextToStation.setText( toStationObject.getString( "name" ) );
                            JSONArray passArray = responseObject.getJSONArray( "passengers" );
                            for (int i=0;i<passArray.length();i++){
                                smallLinearLayout=new LinearLayout( PnrStatus.this );
                                smallLinearLayout.setOrientation( LinearLayout.VERTICAL );
                                TextView sNo = new TextView( PnrStatus.this );
                                TextView currStatus = new TextView( PnrStatus.this );
                                JSONObject passengerObj = passArray.getJSONObject( i);
                                String pass= "Passengers: "+ passengerObj.getString( "no" );
                                sNo.setText(pass);
                                String cS= "Current Status: "+ passengerObj.getString( "current_status" );
                                currStatus.setText( cS);
                                smallLinearLayout.addView( sNo );
                                smallLinearLayout.addView( currStatus );
                                bigLinearLayout.addView(smallLinearLayout);

                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                } );
            }
        } );


    }


}
