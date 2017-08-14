package com.example.moulik.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {

    private TextView connectionStatus;
    private Button connectToBroker;
    private Button publishButton;
    private Button subscribeButton;
    private EditText hostValue;
    private EditText portValue;
    private RelativeLayout buttonLayout;

    String ClientId = "RedmiMobile";
    static MqttAndroidClient client ;

    StringBuffer msg = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectToBroker = (Button) findViewById(R.id.connectButton);
        connectionStatus = (TextView) findViewById(R.id.statusMessage);
        hostValue = (EditText)findViewById(R.id.hostValue);
        portValue = (EditText) findViewById(R.id.portValue);

        buttonLayout = (RelativeLayout) findViewById(R.id.changeScreen);
        publishButton = (Button) findViewById(R.id.publishedButton);
        subscribeButton = (Button) findViewById(R.id.subscribeButton);

        connectToBroker.setOnClickListener(connectToBrokerListener);
        publishButton.setOnClickListener(clickListener);
        subscribeButton.setOnClickListener(clickListener);

    }

    private View.OnClickListener connectToBrokerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(hostValue.getText()!=null && portValue.getText()!=null
                    && hostValue.getText().length()>0 && portValue.getText().length()>0) {
                try {
                    String url = "tcp://" + hostValue.getText().toString() + ":" + portValue.getText().toString();
                    client = new MqttAndroidClient(getApplicationContext(),url , ClientId);
                    MqttConnectOptions options = new MqttConnectOptions();
                    options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
                    IMqttToken token = client.connect(options);
                    token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            //connectionStatus.setVisibility(View.VISIBLE);
                           // connectionStatus.setText("Connection Established Successfully... What do you want to do?");
                            //buttonLayout.setVisibility(View.VISIBLE);
                            Log.i("MainActivity","Connection Established Successfully...");
                            Intent intent = new Intent(MainActivity.this,PublishMessageActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                           // connectionStatus.setText("Something went wrong.. Try again");
                            Log.e("ConnectionOnFailure", exception.getMessage());

                        }
                    });
                }
                catch (MqttException e) {
                    Log.e("Connection",e.getMessage());
                }
            }
        }
    };

    public static MqttAndroidClient getMqttAndroidClient() {
        return client;
    }



    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,PublishMessageActivity.class);

            switch (view.getId()){

                case R.id.publishedButton:

                    intent.putExtra("action","publish");
                    break;

                case R.id.subscribeButton:
                    intent.putExtra("action","subscribe");
                    break;
            }

           // intent.putExtra("MQTTCLIENT", (Serializable) client);
            startActivity(intent);
        }
    };
}
