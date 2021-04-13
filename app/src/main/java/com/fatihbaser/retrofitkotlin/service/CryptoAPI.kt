package com.fatihbaser.retrofitkotlin.service

import com.fatihbaser.retrofitkotlin.model.CryptoModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {
    //get (veriyi cekerken) , post(veriyi yazma ve degistirmek icin post), update, delete

    //https://api.nomics.com/v1/
    // prices?key=2f410823829a2899de49fe78a4410f45

    @GET("prices?key=2f410823829a2899de49fe78a4410f45")

    fun getData(): Observable<List<CryptoModel>>


    //fun getData(): Call<List<CryptoModel>>
}