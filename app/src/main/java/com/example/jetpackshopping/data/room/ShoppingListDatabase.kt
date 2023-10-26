package com.example.jetpackshopping.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.jetpackshopping.data.room.converters.DateConverter
import com.example.jetpackshopping.data.room.models.Item
import com.example.jetpackshopping.data.room.models.ShoppingList
import com.example.jetpackshopping.data.room.models.Store

//It helps to connect with Database and daos


@Database(
    /*
    version --> 1
    entities --> Connect between models(ShoppingList , Item , Store)
     */
    version = 1 , entities = [ShoppingList::class , Item::class , Store::class] , exportSchema = false
)
@TypeConverters(value = [DateConverter::class])
abstract class ShoppingListDatabase : RoomDatabase(){

    abstract fun getListDao() : ListDao
    abstract fun getItemDao() : ItemDao
    abstract fun getStoreDao() : StoreDao


    companion object{
        @Volatile
        var INSTANCE : ShoppingListDatabase? = null

        //Method for get Database
        fun getDatabase(context: Context) : ShoppingListDatabase{
            return INSTANCE ?: synchronized(this){ //it helps to lock to single thread
                /*
                IF it is not null ,
                then it create new database for the instance ,
                and assign to the INSTANCE
                and return it
                 */
                val instance = Room.databaseBuilder(
                    context,
                    ShoppingListDatabase::class.java,
                    "shopping_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}