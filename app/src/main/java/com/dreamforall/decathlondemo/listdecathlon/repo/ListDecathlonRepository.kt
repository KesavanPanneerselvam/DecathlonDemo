package com.dreamforall.decathlondemo.listdecathlon.repo

import android.util.Log
import com.dreamforall.decathlondemo.Constants
import com.dreamforall.decathlondemo.adapter.DecathlonResponseModel
import com.dreamforall.decathlondemo.listdecathlon.api.GetDecathlonList
import com.dreamforall.decathlondemo.network.APIResponseListener
import com.dreamforall.decathlondemo.network.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListDecathlonRepository {

    fun getDecathlonList(listener: APIResponseListener) {
        val retrofitService = NetworkModule(Constants.BASE_URL)
            .getApiInstance(GetDecathlonList::class.java)

        listener.onLoading()

        val apiInterface: Call<DecathlonResponseModel> = retrofitService.getSportsInfo()

        apiInterface.enqueue(object : Callback<DecathlonResponseModel> {
            override fun onResponse(
                call: Call<DecathlonResponseModel>?,
                response: Response<DecathlonResponseModel>?
            ) {
                if (response?.body() != null) {
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                    Log.e("responnse", "response $response")
                    Log.e("responsesize", "size ${response?.body()?.data?.size}")
                }
            }

            override fun onFailure(call: Call<DecathlonResponseModel>?, t: Throwable?) {
                listener.onError("Error Occurred")
            }
        })
    }

}