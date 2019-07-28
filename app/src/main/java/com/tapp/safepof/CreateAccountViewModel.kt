package com.tapp.safepof

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class CreateAccountViewModel: ViewModel() {

    private val _isPasswordMatch = MutableLiveData<Boolean>()
    val isPasswordMatch: LiveData<Boolean>
        get() = _isPasswordMatch

    private val _isNameFound = MutableLiveData<Boolean>()
    val isNameFound: LiveData<Boolean>
        get() = _isNameFound

    private val db = FirebaseFirestore.getInstance()
    private val dbName = "user_info"

    fun checkPassword(userName: String, password1: String, password2: String)
    {
        if (password1 == password2)
        {
            val data = hashMapOf("password" to password1)
            db
                .collection(dbName)
                .document(userName)
                .set(data)
                .addOnSuccessListener {
                    _isPasswordMatch.value = true
                    Log.e("PassViewModel", "correct")
                }
                .addOnFailureListener {
                    Log.e("PassViewModel", "incorrect")
                    _isNameFound.value = false
                }
        }
        else {
            _isPasswordMatch.value = false
        }
    }
}