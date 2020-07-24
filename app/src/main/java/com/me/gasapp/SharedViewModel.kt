package com.me.gasapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


//class SharedViewModel : ViewModel() {
//    private val dataEntries: MutableLiveData<DataList> = MutableLiveData<DataList>()
//
////        fun select(item: DataList) {
////        dataEntries.value = item
////    }
//
//    fun getDataEntries(): DataList? {
//        return dataEntries.value
//    }
//}


class SharedViewModel : ViewModel() {
    val data: MutableLiveData<DataList> = MutableLiveData()

//    init {
//        Log.d("SVM","SVM Created")
//    }
}