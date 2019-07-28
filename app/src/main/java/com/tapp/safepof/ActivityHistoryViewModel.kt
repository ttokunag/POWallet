package com.tapp.safepof

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ActivityHistoryViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _transactions = MutableLiveData<List<TransactionData>>()
    val transactions : LiveData<List<TransactionData>>
        get() = _transactions

    init {
        db.collection("transaction_data")
            .orderBy("transactionDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                _transactions.value = snapshot.toObjects(TransactionData::class.java)
            }
    }

}