package com.backyardev.webservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TrainDetails extends AppCompatActivity {

    TextView opTextTName,dayTextView,runsTextView;
    Button btnDetails;
    EditText idTrainEdit;
    LinearLayout mainLinearLayout,subLinearLayout;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_train_details );

        btnDetails=findViewById( R.id.btnDetail );
        idTrainEdit=findViewById( R.id.idTrainEdit );
        opTextTName=findViewById( R.id.opTextTName );
        mainLinearLayout=findViewById( R.id.mainLinearLayout );
        btnDetails.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Train=idTrainEdit.getText().toString();
                URL="https://api.railwayapi.com/v2/name-number/train/"+Train+"/apikey/3lmq7wz5ix";
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
                TrainDetails.this.runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Log.d("response",myResponse);
                        try{
                            JSONObject responseObject = new JSONObject(myResponse);
                            JSONObject trainObject = responseObject.getJSONObject( "train" );
                            opTextTName.setText( trainObject.getString( "name" ));
                            JSONArray daysArray =  trainObject.getJSONArray( "days" );
                            for(int i=0;i<daysArray.length();i++){
                                JSONObject daysObject=daysArray.getJSONObject( i );
                                String dayCode = daysObject.getString( "code" );
                                String runsCode = daysObject.getString( "runs" );
                                dayTextView=new TextView(TrainDetails.this);
                                dayTextView.setText( runsCode );
                                runsTextView=new TextView( TrainDetails.this );
                                runsTextView.setText( dayCode );
                                subLinearLayout=new LinearLayout( TrainDetails.this );
                                subLinearLayout.setOrientation( LinearLayout.VERTICAL );
                                subLinearLayout.setPadding( 20,0,0,0 );
                                subLinearLayout.addView( runsTextView );
                                subLinearLayout.addView( dayTextView );
                                mainLinearLayout.addView( subLinearLayout );

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
