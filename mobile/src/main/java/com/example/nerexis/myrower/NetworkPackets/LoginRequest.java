package com.example.nerexis.myrower.NetworkPackets;

import android.util.Log;

/**
 * Created by Nerexis on 26.01.2016.
 */
public class LoginRequest {
    public String login;
    public String password;
    public LoginRequest(String login, String password)
    {
        this.login = login;
        this.password = password;
    }

}
