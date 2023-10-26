package com.example.jetpackshopping

import android.content.Context
import com.example.jetpackshopping.data.room.ShoppingListDatabase
import com.example.jetpackshopping.ui.repository.Repository

object Graph {
    //creating database as db , Required SINGLE INSTANCE of database
    lateinit var db : ShoppingListDatabase
        private set

    //Creating repository
    val repository by lazy {
        Repository(
            listDao = db.getListDao(),
            storeDao = db.getStoreDao(),
            itemDao = db.getItemDao()
        )
    }

    //providing database function
    fun provide(context: Context) {
        //Creating database
        db = ShoppingListDatabase.getDatabase(context)
    }
}