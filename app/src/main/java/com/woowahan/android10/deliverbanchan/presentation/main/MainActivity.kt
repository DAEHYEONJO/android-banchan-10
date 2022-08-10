package com.woowahan.android10.deliverbanchan.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.data.repository.DishRemoteRepositoryImpl
import com.woowahan.android10.deliverbanchan.presentation.viewmodel.DishViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val dishViewModel: DishViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(dishViewModel){
            //getMainDish()
//            getSideDishes()
//            getSoupDishes()
//            getExhibitionDishes()
//            getDetailDish("HBDEF")
        }
    }
}