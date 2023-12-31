package com.example.jetpackshopping.ui.home

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackshopping.data.room.ItemsWithStoreAndList
import com.example.jetpackshopping.data.room.models.Item
import com.example.jetpackshopping.ui.Category
import com.example.jetpackshopping.ui.Utils
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate : (Int) -> Unit,
) {
    val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)

    val homeState = homeViewModel.state

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {onNavigate.invoke(-1)}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        LazyColumn{
            item{
                LazyRow{
                    items(Utils.category){category : Category->
                        CategoryItem(
                            iconRes = category.resId,
                            title = category.title,
                            selected = category == homeState.category
                        ) {
                            homeViewModel.onCategoryChange(category)
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
            items(homeState.items){
                ShoppingItems(
                    item = it,
                    isChecked = it.item.isChecked,
                    onCheckedChange = homeViewModel::onItemChecked
                ) {
                    onNavigate.invoke(it.item.id)
                }
            }
        }


    }
    



}


@Composable
fun CategoryItem(
    @DrawableRes iconRes : Int,
    title : String,
    selected : Boolean,
    onItemCheck : () -> Unit
    ) {

    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp, start = 8.dp)
            .selectable(
                selected = selected,
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(),
                onClick = { onItemCheck.invoke() }
            ),
        border = BorderStroke(2.dp , if (selected) MaterialTheme.colorScheme.primary.copy(.5f)
        else MaterialTheme.colorScheme.onSurface),
        shape = ShapeDefaults.ExtraLarge,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(.5f)
            else MaterialTheme.colorScheme.surface,
            contentColor = if (selected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row (horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)) {

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
        }

    }

}

@Composable
fun ShoppingItems(
    item : ItemsWithStoreAndList,
    isChecked : Boolean,
    onCheckedChange : (Item , Boolean) -> Unit,
    onItemClick : () -> Unit
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick.invoke() }
            .padding(8.dp)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .padding(8.dp),
            ) {
                Text(
                    text = item.item.itemsName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = item.store.storeName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.size(4.dp))

                CompositionLocalProvider(
                    LocalContentColor provides
                        LocalContentColor.current.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = formatDate(item.item.date),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }


            }
            
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = "Qty: ${item.item.qty}" ,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.size(4.dp))

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        onCheckedChange.invoke( item.item ,it)
                    }
                )
            }

        }
    }
}


//Format date
@SuppressLint("SimpleDateFormat")
fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // Define your date format
    return sdf.format(date)
}


//@SuppressLint("ResourceType")
//@Preview(showBackground = true)
//@Composable
//fun PreviewScreen() {
//
//
//}

