package com.example.nerexis.myrower;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esotericsoftware.kryonet.Client;

import org.w3c.dom.Text;

import java.io.IOException;



public class LoginActivity extends AppCompatActivity {
    public Button loginButton;
    public TextView connectionStateLabel;
    public EditText loginTextBox;
    public EditText passwordTextBox;
    public static LoginActivity currentInstance;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentInstance = this;

        loginTextBox = (EditText) findViewById(R.id.loginTextBox);
        passwordTextBox = (EditText) findViewById(R.id.passwordTextBox);


        connectionStateLabel = (TextView) findViewById(R.id.connectionStateLabel);
        loginButton = (Button) findViewById(R.id.loginButton);

        Log.d("abc", "Login activity started");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.activity_map);
                loginButton_onClick();
            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loginButton_onClick()
    {
        //setContentView(R.layout.activity_map);
        //Log.d("abc","clicked");
        //client.update(100);
        if(NetworkClient.currentNetworkClient != null) {

            if(!NetworkClient.currentNetworkClient.IsConnected())
            {
                connectionStateLabel.setText("Laczenie...");
                NetworkClient.currentNetworkClient.ConnectToServer();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(NetworkClient.currentNetworkClient.IsConnected())
            {

                NetworkClient.currentNetworkClient.SendLoginRequest(
                        loginTextBox.getText().toString(),
                        passwordTextBox.getText().toString());
            }

            //
        }
        //setResult(1);
        //finish();
    }


}
