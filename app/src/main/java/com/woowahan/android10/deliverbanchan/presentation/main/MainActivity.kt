package com.woowahan.android10.deliverbanchan.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.room.ColumnInfo
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.data.local.model.*
import com.woowahan.android10.deliverbanchan.databinding.ActivityMainBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.viewmodel.DishViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main, "MainActivity") {

    private val dishViewModel: DishViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}