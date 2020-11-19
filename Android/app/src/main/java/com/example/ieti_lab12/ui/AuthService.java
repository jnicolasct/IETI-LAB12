package com.example.ieti_lab12.ui;

import com.example.ieti_lab12.LoginWrapper;
import com.example.ieti_lab12.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/auth")
    Call<Token> login(@Body LoginWrapper login);
}
