package com.example.moulik.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class CommunicationActivity extends AppCompatActivity {

    public static final String TAG = "CommunicationActivity";
    String action;
    private EditText topicValue;
    private EditText messageValue;
    LinearLayout messageLayout;
    Button actionButton;
    TextView messageArea;
    MqttAndroidClient client;
    StringBuffer subscribedMessage = new StringBuffer();
    StringBuffer publishedMessage = new StringBuffer("Published Message:-");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_activity);
        Intent intent = getIntent();
        topicValue = (EditText) findViewById(R.id.topicValue);
        messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
        messageValue = (EditText) findViewById(R.id.messageValue);
        messageArea = (TextView)findViewById(R.id.textArea);
        actionButton = (Button)findViewById(R.id.sendButton);

        /*client = (MqttAndroidClient) intent.getSerializableExtra("MQTTCLIENT");

       if(client != null && client.isConnected())*/
        client = MainActivity.getMqttAndroidClient();
           client.setCallback(callBackForReceiver);

        action = intent.getStringExtra("action");
        if(action!=null){
            if(action.equals("publish")){
                messageLayout.setVisibility(View.VISIBLE);
                actionButton.setText("publish");
                actionButton.setOnClickListener(publishListener);
            }else{
                actionButton.setText("subscribe");
                actionButton.setOnClickListener(subscribeListener);
            }
        }
    }
    MqttCallback callBackForReceiver = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            Log.e("Connection","connectionLost");
            subscribedMessage.append("ConnectionLostTryAgain"+"\n");
            messageArea.setText(subscribedMessage);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.d("message","messagearrived");
            subscribedMessage.append(message.toString()+"\n");
            messageArea.setText(subscribedMessage);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };


    private View.OnClickListener subscribeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                IMqttToken token = client.subscribe(topicValue.getText().toString(),2);
            } catch (MqttException e) {
               Log.e(TAG,"Error while subscribing");
                messageArea.setText("Error while subscribing");
            }
        }
    };

    private View.OnClickListener publishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (client.isConnected()) {
                try {
                    client.publish(topicValue.getText().toString(), // topic
                            messageValue.getText().toString().getBytes(), 2, // QoS
                            true); // retained?
                    publishedMessage.append(messageValue.getText().toString()+"\n");
                    messageArea.setText(publishedMessage);
                    messageValue.setText("");

                } catch (MqttException e) {
                    Log.e(TAG,"Error while publishing message");
                    publishedMessage.append("Error while publishing message");
                    messageArea.setText(publishedMessage);
                }

            }else {
                messageArea.setText("Client is not connected");
            }
        }
    };

}
