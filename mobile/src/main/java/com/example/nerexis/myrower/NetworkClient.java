package com.example.nerexis.myrower;

import android.os.AsyncTask;
import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.example.nerexis.myrower.NetworkPackets.LoginReply;
import com.example.nerexis.myrower.NetworkPackets.LoginRequest;

import java.io.IOException;

/**
 * Created by Nerexis on 26.01.2016.
 */
public class NetworkClient {
    Client client;
    public LoginActivity currentLoginActivity;
    Kryo kryo;
    public static NetworkClient currentNetworkClient;

    private LoginReply loginData;

    public  NetworkClient()
    {
        currentNetworkClient = this;
        client = new Client();
        kryo = client.getKryo();

        kryo.register(LoginRequest.class);
        kryo.register(LoginReply.class);
        client.addListener(new Listener() {
            @Override
            public void received (Connection connection, Object object) {

                if(object instanceof LoginReply)
                {
                    LoginReply userData = (LoginReply) object;
                    loginData = userData;
                        LoginActivity.currentInstance.connectionStateLabel.post(new Runnable() {
                            @Override
                            public void run() {
                                if(loginData.id!=0) {
                                    LoginActivity.currentInstance.connectionStateLabel.setText("Zalogowano");
                                    LoginActivity.currentInstance.setResult(1);
                                    LoginActivity.currentInstance.finish();
                                } else
                                {
                                    LoginActivity.currentInstance.connectionStateLabel.setText("Błędny login/hasło");
                                    LoginActivity.currentInstance.loginTextBox.setText("");
                                    LoginActivity.currentInstance.setResult(1);
                                    LoginActivity.currentInstance.passwordTextBox.setText("");
                                }
                            }
                        });

                }

            }
        });


        new Thread(client).start();


    }

    private class ConnectToServer extends AsyncTask<String, int[], String>
    {
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                client.connect(1500, "192.168.1.12", 1234);
                return "Połączono";
            } catch(IOException e) {
                e.printStackTrace();
                return "Nie można połączyć";

            }


        }
        @Override
        protected void onPostExecute(String result)
        {
            LoginActivity.currentInstance.connectionStateLabel.setText(result);
        }
    }
    public void ConnectToServer()
    {
        new ConnectToServer().execute();
    }
    public boolean IsConnected()
    {
        return client.isConnected();
    }
    public void SendLoginRequest(final String login, final String password)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Network", "Login request for " + login + " pass " + password);
                LoginRequest request = new LoginRequest(login, password);
                client.sendTCP(request);

                LoginActivity.currentInstance.connectionStateLabel.post(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.currentInstance.connectionStateLabel.setText("Logowanie...");
                    }
                });

            }
        }).start();

    }


}
