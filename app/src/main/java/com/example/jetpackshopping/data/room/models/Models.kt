package com.example.jetpackshopping.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jetpackshopping.data.room.converters.DateConverter
import java.util.Date

//Representing shopping List
@Entity(tableName = "shopping_list")
data class ShoppingList(
    @ColumnInfo(name = "list_id") //---------------->Change the name of the column in the table (Shopping_List)
    @PrimaryKey
    val id : Int,
    val name : String
)

//Represent Items in the Shopping_List
@Entity(tableName = "items")
data class Item(
    @ColumnInfo(name = "item_id") //----------------->change the name of the column in the table(ITEMS)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemsName: String,
    val qty: String,
    val listId: Int, //---------------->Join the  items to the shopping list via Foreign key
    val storeIdFk: Int,//---------------->Join the items to the stores via Foreign key
    val date: Date,
    val isChecked: Boolean
)

//Represent Shores
@Entity(tableName = "stores")
data class Store(
    @ColumnInfo(name = "stores_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val listIdFk: Int,
    val storeName : String
)