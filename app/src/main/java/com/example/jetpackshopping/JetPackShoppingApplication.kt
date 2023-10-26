package com.example.jetpackshopping

import android.app.Application

class JetPackShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //when our application is created then it is called
        Graph.provide(this)
    }
}