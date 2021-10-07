package com.dreamforall.decathlondemo.network

interface APIResponseListener {

    fun onSuccess(data: Any)

    fun onError(data: Any)

    fun onLoading()
}