package com.tapp.safepof

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FinanceTrackerViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceTrackerViewModel::class.java)) {
            return FinanceTrackerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}