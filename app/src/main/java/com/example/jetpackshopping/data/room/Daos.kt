package com.example.jetpackshopping.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.jetpackshopping.data.room.models.Item
import com.example.jetpackshopping.data.room.models.ShoppingList
import com.example.jetpackshopping.data.room.models.Store
import kotlinx.coroutines.flow.Flow

//Data Access Object

//ItemDAO
@Dao
interface ItemDao{

    /*
    OnConflict strategy constant to replace the old data and continue the transaction.
    An Insert DAO method that returns the inserted rows ids will never return -1 since this
    strategy will always insert a row even if there is a conflict.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) //Same IDs replace will newOne
    suspend fun insert(item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items")  //taking all records from the item tables
    fun getAllItems() : Flow<List<Item>>  //return the list of Item from the table


    @Query("SELECT * FROM items WHERE item_id =:itemId") //Selecting Specific itemId from the table
    fun getItem(itemId : Int) : Flow<Item>

}


//StoreDAO
@Dao
interface StoreDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(store: Store)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(store: Store)

    @Delete
    suspend fun delete(store: Store)

    @Query("SELECT * FROM stores")
    fun getAllStores() : Flow<List<Store>>

    @Query("SELECT * FROM stores WHERE stores_id =:storeId")
    fun getStore(storeId : Item) : Flow<Store>

}

//ShoppingListDAO
@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(shoppingList: ShoppingList)


    //it takes item from the store and list
    //Basically it link the two table
    @Query(
        """
        SELECT * FROM items AS I INNER JOIN shopping_list AS S /* --HERE items and shopping_list are join to eachOther */
        ON I.listId = S.list_id INNER JOIN stores AS ST      /* --here given the condition between the list and items and join these table to the stores table as ST*/
        ON I.storeIdFk = ST.stores_id
    """
    )
    fun getItemsWithStoreAndList() : Flow<List<ItemsWithStoreAndList>>

    //IT filter the listIdFk by the listId
@Query(
    """
        SELECT * FROM items AS I INNER JOIN shopping_list AS S /* --HERE items and shopping_list are join to eachOther */
        ON I.listId = S.list_id INNER JOIN stores AS ST      /* --Here given the condition between the list and items and join these table to the stores table as ST*/
        ON I.storeIdFk = ST.stores_id WHERE S.list_id =:listId 
    """
)
    fun getItemsWithStoreAndListFilteredById(listId : Int) : Flow<List<ItemsWithStoreAndList>>

    //IT filter single item from the itemId
    @Query(
        """
        SELECT * FROM items AS I INNER JOIN shopping_list AS S /* --HERE items and shopping_list are join to eachOther */
        ON I.listId = S.list_id INNER JOIN stores AS ST      /* --Here given the condition between the list and items and join these table to the stores table as ST*/
        ON I.storeIdFk = ST.stores_id WHERE I.item_id =:itemId 
    """
    )
    fun getItemWithStoreAndListFilteredById(itemId : Int) : Flow<ItemsWithStoreAndList>

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    @Delete
    suspend fun deleteShoppingList(shoppingList: ShoppingList)
}

data class ItemsWithStoreAndList(
    @Embedded val item: Item,
    @Embedded val shoppingList: ShoppingList,
    @Embedded val store: Store
)