package com.example.jetpackshopping.ui.repository

import com.example.jetpackshopping.data.room.ItemDao
import com.example.jetpackshopping.data.room.ListDao
import com.example.jetpackshopping.data.room.StoreDao
import com.example.jetpackshopping.data.room.models.Item
import com.example.jetpackshopping.data.room.models.ShoppingList
import com.example.jetpackshopping.data.room.models.Store


//It helps to connect data layer and UI Layer of the App
//IN class we access the all the daos
class Repository(
    private val listDao : ListDao,
    private val storeDao: StoreDao,
    private val itemDao : ItemDao
) {

    //Getting all stores
    val stores = storeDao.getAllStores()
    val getItemsWithListAndStore = listDao.getItemsWithStoreAndList()


    /*Function for get item with store and list with help of id
    ----------> return Single item with Id
     */
    fun getItemWithStoreAndList(id : Int) = listDao.getItemWithStoreAndListFilteredById(id)


    /*Function for get Items with store and list filtered By Id
    -----------> return list of item with id
     */
    fun getItemWithStoreAndListFilteredById(id: Int) = listDao.getItemsWithStoreAndListFilteredById(id)


    //Inserting
    suspend fun insertList(shoppingList: ShoppingList){
        listDao.insertShoppingList(shoppingList)
    }

    suspend fun insertStore(store: Store){
        storeDao.insert(store)
    }

    suspend fun insertItem(item : Item){
        itemDao.insert(item)
    }

    suspend fun deleteItem(item: Item){
        itemDao.delete(item)
    }

    suspend fun updateItem(item: Item){
        itemDao.update(item)
    }


}