package com.example.moulik.myapplication;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PublishMessageActivity extends AppCompatActivity implements View.OnClickListener{

    static String carSelect = "car";

    private Button car1;
    private Button car2;
    private Button car3;
    private Button car4;
    private Button car5;

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

        car1 = (Button) findViewById(R.id.car1);
        car2 = (Button) findViewById(R.id.car2);
        car3 = (Button) findViewById(R.id.car3);
        car4 = (Button) findViewById(R.id.car4);
        car5 = (Button) findViewById(R.id.car4);

        speedLimit1 = (Button) findViewById(R.id.SL1);
        speedLimit2 = (Button) findViewById(R.id.SL2);
        speedLimit3 = (Button) findViewById(R.id.SL3);
        speedLimit4 = (Button) findViewById(R.id.SL4);
        reverseBtn  = (Button) findViewById(R.id.reverseBtn);
        switchBtn   = (Button) findViewById(R.id.switchBtn);
        radioGroupForReverse = (RadioGroup) findViewById(R.id.radioGroupForReverse);
        radioGroupForSwitch  = (RadioGroup) findViewById(R.id.radioGroupForSwitch);
        messageText = (TextView)findViewById(R.id.messageText);
        client = MainActivity.getMqttAndroidClient();
        client.setCallback(callBackForReceiver);
        /*try {
            client.subscribe("SL.1",0);
            client.subscribe("SL.2",0);
            client.subscribe("SL.3",0);
            client.subscribe("SL.4",0);
        } catch (MqttException e) {
           Log.e(TAG,"Error while subscribing a message");
        }*/
        try {
            client.subscribe("C1SL.1",2);
            client.subscribe("C1SL.2",2);
            client.subscribe("C1SL.3",2);
            client.subscribe("C1SL.4",2);
        } catch (MqttException e) {
           Log.e(TAG,"Error while subscribing a message");
        }
        car1.setOnClickListener(this);
        car2.setOnClickListener(this);
        car3.setOnClickListener(this);
        car4.setOnClickListener(this);
        car5.setOnClickListener(this);

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
                publishMessage(carSelect+"SL.1","1");
                break;
            case R.id.SL2:
                messageText.setText("");
                publishMessage(carSelect+"SL.2","1");
                break;
            case R.id.SL3:
                messageText.setText("");
                publishMessage(carSelect+"SL.3","1");
                break;
            case R.id.SL4:
                messageText.setText("");
                publishMessage(carSelect+"SL.4","1");
                break;
            case R.id.reverseBtn:
                break;
            case R.id.switchBtn:
                break;

            case R.id.car1:
                car1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                carSelect = car1.getText().toString();
                break;
            case R.id.car2:
                carSelect = car2.getText().toString();
                break;
            case R.id.car3:
                carSelect =  car3.getText().toString();
                break;
            case R.id.car4:
                carSelect = car4.getText().toString();
                break;
            case R.id.car5:
                carSelect = (String) car5.getText();
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
                publishMessage(carSelect+"Reverse","0");
                    break;
            case R.id.one_reverse:
                if (checked)
                    Log.i(TAG,"one of reverse Checked");
                publishMessage(carSelect+"Reverse","1");
                    break;
            case R.id.zero_switch:
                if (checked)
                    Log.i(TAG,"Zero of reverse clicked");
                publishMessage(carSelect+"Switch","0");
                break;
            case R.id.one_switch:
                if (checked)
                    Log.i(TAG,"one of reverse Checked");
                publishMessage(carSelect+"Switch","1");
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
            Log.d(TAG,"messageArrived");
            if(topic.equals("C1SL.1")){
                speedLimit1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            }else if(topic.equals("C1SL.2")){
                speedLimit2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            }else if(topic.equals("C1SL.3")){
                speedLimit3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            }else if(topic.equals("C1SL.4")){
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

                client.publish(topic, // topicName
                        message.getBytes(), 2, // QoS
                        false); // retained?

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

