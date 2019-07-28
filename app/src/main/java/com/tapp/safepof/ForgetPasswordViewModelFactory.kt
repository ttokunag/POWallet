package com.tapp.safepof

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ForgetPasswordViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)) {
            return ForgetPasswordViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}