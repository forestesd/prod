package com.example.prod

import android.app.Application

class MyApplication: Application(){
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }


}