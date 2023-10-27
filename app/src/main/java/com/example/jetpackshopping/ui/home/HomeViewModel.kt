package com.example.jetpackshopping.ui.home


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackshopping.Graph
import com.example.jetpackshopping.data.room.ItemsWithStoreAndList
import com.example.jetpackshopping.data.room.models.Item
import com.example.jetpackshopping.ui.repository.Repository
import com.example.jetpackshopping.ui.Category
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeViewModel( private val repository : Repository = Graph.repository ) : ViewModel() {

     var state by mutableStateOf(HomeState())
        private set


    init {
        getItem()
    }



    //function for getting item first
    private fun getItem(){
        viewModelScope.launch {
            //Collect the Data
            repository.getItemsWithListAndStore.collectLatest {
            //Updating State state
                state = state.copy(
                    items = it
                )
            }
        }
    }

    //Deleting items
    private fun deleteItem(item : Item){
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }


    //Filtering data according to Category
    fun onCategoryChange(category: Category){
        state = state.copy(category = category)
        filterBy(category.id)
    }


    fun onItemChecked(item : Item , isChecked : Boolean){
        viewModelScope.launch {
            repository.updateItem(
                item = item.copy(isChecked = isChecked)
            )
        }
    }



    //Filtering data by the shopping List id
    private fun filterBy(shoppingListId : Int){
        if (shoppingListId != 10001){
            viewModelScope.launch {
                repository.getItemWithStoreAndListFilteredById(shoppingListId)
                    .collectLatest {
                        state = state.copy(
                            items = it
                        )
                    }
            }
        } else{
            getItem()
        }
    }

}



//Data class For
//Store the states of the Compose
data class HomeState(
    //Stores list of item
    val items : List<ItemsWithStoreAndList> = emptyList(),

    val category : Category = Category(),

    val itemChecked : Boolean = false

)