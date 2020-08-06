package com.me.gasapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val dt: MutableLiveData<Long> = MutableLiveData()
    val dist: MutableLiveData<Double> = MutableLiveData()
    val gas: MutableLiveData<Double> = MutableLiveData()

}