package com.tapp.safepof

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActivityHistoryViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityHistoryViewModel::class.java)) {
            return ActivityHistoryViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}