package com.woowahan.android10.deliverbanchan.presentation.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(): ViewModel() {

    val appBarTitle = MutableLiveData<String>("dd")
    val orderDetailMode = MutableLiveData(false)

    init {

    }

    fun setAppBarTitle(string: String){
        appBarTitle.value = string
    }

}