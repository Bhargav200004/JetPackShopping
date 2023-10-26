package com.example.jetpackshopping.ui.home

import androidx.lifecycle.ViewModel
import com.example.jetpackshopping.Graph
import com.example.jetpackshopping.ui.repository.Repository

class HomeViewModel(val repository: Repository = Graph.repository) : ViewModel() {

}