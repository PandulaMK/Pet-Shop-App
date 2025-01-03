package com.example.dognutrition;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button getStartedButton = findViewById(R.id.loginButton2);
        getStartedButton.setOnClickListener(v -> {
            Intent mainIntent = new Intent(Splash_Screen.this, Login_Activity.class);
            Splash_Screen.this.startActivity(mainIntent);
            Splash_Screen.this.finish();
        });
    }
}
