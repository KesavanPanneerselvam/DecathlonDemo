package com.dreamforall.decathlondemo.listdecathlon.api

import com.dreamforall.decathlondemo.adapter.DecathlonResponseModel
import retrofit2.Call
import retrofit2.http.GET

interface GetDecathlonList {

    @GET("/sports?has_icon=true")
    fun getSportsInfo(): Call<DecathlonResponseModel>


}