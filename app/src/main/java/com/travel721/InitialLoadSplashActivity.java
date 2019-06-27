package com.travel721;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

/**
 * This is the Activity that first loads.
 * Snackbars (the small messages) are enabled.
 * @author Bhav
 */
public class InitialLoadSplashActivity extends SplashActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.splash_screen_layout);
//        Snackbar.make(findViewById(R.id.loading_spinner_view), getResources().getString(R.string.loading_app_tooltip), Snackbar.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);

    }
}
