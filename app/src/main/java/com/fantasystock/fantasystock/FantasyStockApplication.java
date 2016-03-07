package com.fantasystock.fantasystock;

import android.content.res.Resources;

import com.activeandroid.app.Application;
import com.parse.Parse;

/**
 * Created by chengfu_lin on 3/7/16.
 */
public class FantasyStockApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Resources res = getResources();
        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(res.getString(R.string.parse_app_id)) // should correspond to APP_ID env variable
                .clientKey(res.getString(R.string.parse_client_key))  // set explicitly unless clientKey is explicitly configured on Parse server
                .server(res.getString(R.string.parse_server_url)).build());
    }
}