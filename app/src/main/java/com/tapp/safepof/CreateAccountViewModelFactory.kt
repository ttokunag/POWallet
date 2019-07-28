package com.tapp.safepof

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateAccountViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateAccountViewModel::class.java)) {
            return CreateAccountViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}