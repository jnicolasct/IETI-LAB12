package com.example.ieti_lab12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ieti_lab12.ui.AuthService;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newFixedThreadPool( 1 );
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button signIn = findViewById(R.id.button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClicked(view);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") //localhost for emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authService = retrofit.create(AuthService.class);
    }

    public void onLoginClicked( View view ){
        EditText user = (EditText) findViewById(R.id.user);
        EditText password = (EditText) findViewById(R.id.password);
        String usertext = user.getText().toString();
        String passwordtext = password.getText().toString();

        LoginWrapper fullUser = new LoginWrapper(usertext, passwordtext);
        if (usertext.equals("")){
            user.setError("usuario invalido");
        }
        if (passwordtext.equals("")){
            password.setError("conraseña invalida");
        }

        if(!usertext.equals("") && !passwordtext.equals("")){
            Intent intent = new Intent(this, MainActivity.class);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {

                        String token = authService.login(fullUser).execute().body().getResponse();
                        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("TOKEN", token);
                        editor.commit();
                        startActivity(intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        }


    }
}