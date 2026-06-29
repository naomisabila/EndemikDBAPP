package com.example.endemikdbapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvProgress;
    private int progressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tvProgress);

        // Start the progress animation
        new Thread(new Runnable() {
            public void run() {
                long start = System.currentTimeMillis();
                long duration = 2200; // 2.2 seconds matches your example

                while (progressStatus < 100) {
                    long elapsed = System.currentTimeMillis() - start;
                    progressStatus = (int) Math.min(100, (elapsed * 100) / duration);

                    // Update the progress bar and text
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            tvProgress.setText("MEMUAT KATALOG - " + progressStatus + "%");
                        }
                    });

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Wait a bit after 100% then move to HomeActivity
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();
                    }
                }, 250);
            }
        }).start();
    }
}