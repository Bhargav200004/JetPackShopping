package com.example.jetpackshopping.ui.detail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackshopping.ui.Category
import com.example.jetpackshopping.ui.Utils
import com.example.jetpackshopping.ui.home.CategoryItem
import com.example.jetpackshopping.ui.home.formatDate
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id : Int,
    navigateUp : () -> Unit
) {
    val viewModel = viewModel<DetailViewModel>(factory = DetailViewModelFactor(id))
    Scaffold() {
        DetailEntry(
            state = viewModel.state,
            onDateChange = viewModel::onDateChange,
            onStoreChange = viewModel::onStoreChange,
            onItemChange = viewModel::onItemChange,
            onQtyChange = viewModel::onQtyChange,
            onCategoryChange = viewModel::onCategoryChange,
            onDialogDismiss = viewModel::onIsScreenDialogDismissed,
            onSaveStore = viewModel::addStore,
            updateItem = { viewModel.updateShoppingItem(id) },
            saveItem = { viewModel::addShoppingItem }) {
            navigateUp.invoke()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailEntry(
    modifier : Modifier = Modifier,
    state: DetailState,
    onDateChange : (Date) -> Unit,
    onStoreChange : (String) -> Unit,
    onItemChange : (String) -> Unit,
    onQtyChange : (String) -> Unit,
    onCategoryChange : (Category) -> Unit,
    onDialogDismiss : (Boolean) -> Unit,
    onSaveStore : () -> Unit,
    updateItem : () -> Unit,
    saveItem : () -> Unit,
    navigateUp : () -> Unit
) {

    var isNewEnable by remember{
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        TextField(
            value = state.item,
            onValueChange = { onItemChange.invoke(it) },
            label = { Text(text = "Item")},
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = ShapeDefaults.Large
        )
        Spacer(modifier = Modifier.size(12.dp))

        Row {
            TextField(
                value =state.store,
                onValueChange ={
                    if (!isNewEnable) onStoreChange.invoke(it)
                },
                modifier = Modifier
                    .weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = ShapeDefaults.Large,
                label = { Text(text = "Store")},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                onDialogDismiss.invoke(!state.isScreenDialogDismissed)
                            }
                    )
                }
            )
            if (!state.isScreenDialogDismissed){
                Popup(onDismissRequest = {
                    onDialogDismiss.invoke(!state.isScreenDialogDismissed)
                }) {
                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Column {
                            state.storeList.forEach{
                                Text(
                                    text = it.storeName,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable {
                                            onStoreChange.invoke(it.storeName)
                                            onDialogDismiss(!state.isScreenDialogDismissed)
                                        }
                                )
                            }
                        }
                    }
                }
            }

            TextButton(onClick = { isNewEnable = if (isNewEnable) {
                onSaveStore.invoke()
                !isNewEnable
            }
                else !isNewEnable
            }) {
                Text(text = if (isNewEnable) "Save" else "New")
            }
        }
        
        Spacer(modifier = Modifier.size(12.dp))

        Row (horizontalArrangement = Arrangement.SpaceEvenly){
            Row (verticalAlignment = Alignment.CenterVertically){
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
                
                Spacer(modifier = Modifier.size(4.dp))
                
                Text(text = formatDate(state.date))
                
                Spacer(modifier = Modifier.size(4.dp))

                val datePickerDialog = datePickerDialog(
                    context = LocalContext.current,
                    onDateSelected = {date->
                        onDateChange.invoke(date)
                    }
                )

                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
                
                
            }

            TextField(
                value = state.qty,
                onValueChange = { onQtyChange(it) },
                label = { Text(text = "Qty")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = ShapeDefaults.Large
            )

        }

        Spacer(modifier = Modifier.size(12.dp))

        LazyRow{
            items(Utils.category){category:Category ->
                CategoryItem(
                    iconRes = category.resId,
                    title = category.title,
                    selected =  category == state.category
                ) {
                    onCategoryChange(category)
                }

                Spacer(modifier = Modifier.size(12.dp))
            }
        }

        val buttonTitle = if (state.isUpdatingItem) "Update Item"
        else "Add Item"

        Button(
            onClick = {
                      when(state.isUpdatingItem){
                          true -> {
                              updateItem.invoke()
                          }
                          false -> {
                              saveItem.invoke()
                          }
                      }
                navigateUp.invoke()
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = state.item.isNotEmpty() && state.store.isNotEmpty() && state.qty.isNotEmpty(),
            shape = ShapeDefaults.Large
        ) {
            Text(text = buttonTitle)
        }

    }
}

@Composable
fun datePickerDialog(
    context: Context,
    onDateSelected : (Date) ->Unit
) : DatePickerDialog {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
    context,
        {_:DatePicker , mYear : Int , mMonth : Int , mDay : Int ->
            val calendar = Calendar.getInstance()
            calendar.set(mYear,mMonth,mDay)
            onDateSelected.invoke(calendar.time)
        },
        year,
        month,
        day
    )

    return mDatePickerDialog


}

//@Preview (showSystemUi = true)
//@Composable
//fun PreviewEntry() {
//    DetailEntry(
//        state = DetailState(),
//        onDateChange = {},
//        onStoreChange = {},
//        onItemChange = {},
//        onQtyChange = {},
//        onCategoryChange = {} ,
//        onDialogDismiss = {},
//        onSaveStore = { /*TODO*/ },
//        updateItem = { /*TODO*/ },
//        saveItem = { /*TODO*/ }) {
//
//    }
//}