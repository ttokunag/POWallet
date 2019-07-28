package com.tapp.safepof

import java.util.*

data class TransactionData(
    var transaction_id: Int? = 0,
    var balance: Double? = 0.0,
    var transactionType: String? = "",
    var transactionAmount: Double? = 0.0,
    var transactionDate: Date? = Date(),
    var purpose: String? = ""
)