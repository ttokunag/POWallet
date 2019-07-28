package com.tapp.safepof


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.NumberFormat
import java.util.*

class FinanceTrackerViewModel : ViewModel() {

    // LiveData for a current balance
    private val _currentBalance = MutableLiveData<Double>()
    val currentBalance: LiveData<Double>
        get() = _currentBalance
    // LiveData for a button click
    private val _whichButtonClicked = MutableLiveData<Int>()
    val whichButtonClicked: LiveData<Int>
        get() = _whichButtonClicked
    // LiveData for the amount entered
    private val _enteredAmount = MutableLiveData<Double>()

    // Firestore database instance
    private val db = FirebaseFirestore.getInstance()
    private val COLLECTION_NAME = "transaction_data"
    private var transactionId = 0

    // value initialization
    init {
        // reflects the latest state to the device
        getTheLatestData()
        // retrieve the transaction id for data addition
        db.collection("transaction_data_id")
            .document("id")
            .get()
            .addOnSuccessListener { snapshot ->
                transactionId = snapshot.data?.get("latest_id").toString().toInt()
            }
        _enteredAmount.value = 0.0
        _whichButtonClicked.value = null // 0 -> ADD, 1 -> MINUS
    }

    /*
     * a method which adds data to the Firestore database
     *
     * @param type: transaction type
     * @param purpose: trasaction purpose
     */
    private fun addDataToFirestore(type: String, purpose: String)
    {
        val data = TransactionData(transactionId, _currentBalance.value,
            type, _enteredAmount.value, Date(), purpose)
        // add new data to the Firestore
        db.collection(COLLECTION_NAME).document("transaction_id_$transactionId")
            .set(data)
        // updates the current transaction id
        db
            .collection("transaction_data_id")
            .document("id")
            .update("latest_id", ++transactionId)
    }

    /*
     * a method which gets data from the Firestore database
     */
    private fun getTheLatestData() {
        db.collection(COLLECTION_NAME)
            .orderBy("transactionDate", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val transactionData = snapshot.toObjects(TransactionData::class.java)
                if (transactionData.size != 0) {
                    _currentBalance.value = transactionData[0].balance
                }
                else {
                    _currentBalance.value = 0.0
                }
            }
    }

    fun onClickModifyButton(buttonClicked: Int) {
        _whichButtonClicked.value = buttonClicked
    }

    fun setAmount(givenAmount: Double) {
        _enteredAmount.value = givenAmount
    }

    fun isEnteredAmountInvalid(): Boolean {
        return _currentBalance.value!!.compareTo(_enteredAmount.value!!) < 0
    }

    fun updateCurrentBalance(whichButton: Int, purpose: String) {
        if (whichButton == 0) {
            _currentBalance.value = _currentBalance.value!!.toDouble() + _enteredAmount.value!!.toDouble()
            // add the data to Firebase
            addDataToFirestore("Deposit", purpose)
        }
        else {
            _currentBalance.value = _currentBalance.value!!.toDouble() - _enteredAmount.value!!.toDouble()
            // add the data to Firebase
            addDataToFirestore("Withdraw", purpose)
        }

    }

    fun currentBalanceString(): String {
        return NumberFormat.getCurrencyInstance(Locale.US).format(_currentBalance.value)
    }

    fun notifyModifyCompleted(): String {
        return when (_whichButtonClicked.value) {
            0 -> "Deposited $" + _enteredAmount.value.toString()
            else -> "Withdrew $" + _enteredAmount.value.toString()
        }
    }

}