package com.dreamforall.decathlondemo.listdecathlon.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dreamforall.decathlondemo.adapter.DecathlonResponseModel
import com.dreamforall.decathlondemo.listdecathlon.repo.ListDecathlonRepository
import com.dreamforall.decathlondemo.network.APIResponseListener

class ListDecathlonViewModel : ViewModel(), APIResponseListener {
    // TODO: Implement the ViewModel


    private val _decathlonSportsInfo = MutableLiveData<DecathlonResponseModel>()
    val decathlonSportsInfo: MutableLiveData<DecathlonResponseModel>
        get() = _decathlonSportsInfo

    private val _decathlonSportsInfoLoading = MutableLiveData<Boolean>()
    val decathlonSportsInfoLoading: MutableLiveData<Boolean>
        get() = _decathlonSportsInfoLoading

    init {
        getDecathlonSportsInfo()
    }

    fun getDecathlonSportsInfo() {
        val repo = ListDecathlonRepository()
        repo.getDecathlonList(this)
    }

    override fun onSuccess(data: Any) {
        if (data is DecathlonResponseModel)
            _decathlonSportsInfo.value = data

    }

    override fun onError(data: Any) {
    }

    override fun onLoading() {
        _decathlonSportsInfoLoading.value = true
    }
}