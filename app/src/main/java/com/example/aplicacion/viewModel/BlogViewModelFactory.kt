package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacion.data.remote.RetrofitInstance
import com.example.aplicacion.model.repository.BlogRepository

class BlogViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlogViewModel::class.java)) {
            val api = RetrofitInstance.api
            val repo = BlogRepository(api)
            return BlogViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}