package com.example.moulik.myapplication;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PublishMessageActivity extends AppCompatActivity implements View.OnClickListener{

    private Button speedLimit1;
    private Button speedLimit2;
    private Button speedLimit3;
    private Button speedLimit4;
    private Button reverseBtn;
    private Button switchBtn;
    private RadioGroup radioGroupForReverse;
    private RadioGroup radioGroupForSwitch;
    private TextView messageText;
    MqttAndroidClient client;
    public static final String TAG = "PublishMessageActivity";
    private static int count ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        speedLimit1 = (Button) findViewById(R.id.SL1);
        speedLimit2 = (Button) findViewById(R.id.SL2);
        speedLimit3 = (Button) findViewById(R.id.SL3);
        speedLimit4 = (Button) findViewById(R.id.SL4);
        reverseBtn = (Button) findViewById(R.id.reverseBtn);
        switchBtn = (Button) findViewById(R.id.switchBtn);
        radioGroupForReverse = (RadioGroup) findViewById(R.id.radioGroupForReverse);
        radioGroupForSwitch = (RadioGroup) findViewById(R.id.radioGroupForSwitch);
        messageText = (TextView)findViewById(R.id.messageText);
        client = MainActivity.getMqttAndroidClient();
        client.setCallback(callBackForReceiver);
        try {
            client.subscribe("SL.1",0);
            client.subscribe("SL.2",0);
            client.subscribe("SL.3",0);
            client.subscribe("SL.4",0);
        } catch (MqttException e) {
           Log.e(TAG,"Error while subscribing a message");
        }

        speedLimit1.setOnClickListener(this);
        speedLimit2.setOnClickListener(this);
        speedLimit3.setOnClickListener(this);
        speedLimit4.setOnClickListener(this);
        reverseBtn.setOnClickListener(this);
        switchBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.SL1:
                messageText.setText("");
                publishMessage("SL.1","1");
                break;
            case R.id.SL2:
                messageText.setText("");
                publishMessage("SL.2","1");
                break;
            case R.id.SL3:
                messageText.setText("");
                publishMessage("SL.3","1");
                break;
            case R.id.SL4:
                messageText.setText("");
                publishMessage("SL.4","1");
                break;

            case R.id.reverseBtn:

                break;


            case R.id.switchBtn:

                break;

        }


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        messageText.setText("");
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.zero_reverse:

                if (checked)
                   Log.i(TAG,"Zero of reverse clicked");
                publishMessage("Reverse","0");
                    break;
            case R.id.one_reverse:
                if (checked)
                    Log.i(TAG,"one of reverse Checked");
                publishMessage("Reverse","1");
                    break;
            case R.id.zero_switch:
                if (checked)
                    Log.i(TAG,"Zero of reverse clicked");
                publishMessage("Switch","0");
                break;
            case R.id.one_switch:
                if (checked)
                    Log.i(TAG,"one of reverse Checked");
                publishMessage("Switch","1");
                break;
        }
    }


    MqttCallback callBackForReceiver = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            Log.e(TAG,"connectionLost");
            messageText.setText("ConnectionLost try again");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.d(TAG,"messagearrived");
            if(topic.equals("SL.1")){
                speedLimit1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            }else if(topic.equals("SL.2")){
                speedLimit2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            }else if(topic.equals("SL.3")){
                speedLimit3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            }else if(topic.equals("SL.4")){
                speedLimit4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            }
        }



        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            try {
                String message = token.getMessage().toString();

                messageText.setText(message+ " message published");
            } catch (MqttException e) {
               Log.e(TAG,"Error while delivering message");
            }
            Log.d(TAG,"Message Published successfully");
        }
    };

    private void publishMessage(String topic,String message){
        if (client.isConnected()) {
            try {
                String topicName = topic;
                client.publish(topicName, // topic
                        message.getBytes(), 2, // QoS
                        true); // retained?

            } catch (MqttException e) {
                Log.e(TAG,"Error while publishing a message");
                messageText.setText("Error while publishing a message");
            }

        }else {
            messageText.setText("Client is not connected.Please connect again");
        }


    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}

