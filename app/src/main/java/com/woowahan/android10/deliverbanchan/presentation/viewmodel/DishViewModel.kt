package com.woowahan.android10.deliverbanchan.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.woowahan.android10.deliverbanchan.data.local.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishViewModel @Inject constructor(

) : ViewModel() {

    companion object{
        const val TAG = "DishViewModel"
    }

}