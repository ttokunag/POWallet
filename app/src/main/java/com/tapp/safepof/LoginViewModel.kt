package com.tapp.safepof

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {

    private val _isUserInfoCorrect = MutableLiveData<Int>()
    val isUserInfoCorrect: LiveData<Int>
        get() = _isUserInfoCorrect

    private val db = FirebaseFirestore.getInstance()
    private val dbName = "user_info"


    fun checkUserInfo(name: String, password: String)
    {
        if (name == "" || password == "") {
            _isUserInfoCorrect.value = 0
            return
        }
        db
            .collection(dbName)
            .document(name)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.data != null) {
                    if (password == snapshot.data?.get("password"))
                    {
                        _isUserInfoCorrect.value = 1
                        Log.e("LoginViewModel", "Password is correct!")
                    }
                    else
                    {
                        _isUserInfoCorrect.value = 2
                        Log.e("LoginViewModel", "Password is incorrect!")
                    }
                }
                else {
                    _isUserInfoCorrect.value = 3
                    Log.e("LoginViewModel", "User name is incorrect!")
                }
            }
            .addOnFailureListener {
                _isUserInfoCorrect.value = 4
                Log.e("LoginViewModel", "Check the Internet connection.")
            }
    }
}