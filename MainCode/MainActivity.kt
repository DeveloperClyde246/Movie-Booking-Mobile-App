package com.example.moviemenu.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemenu.database.Payment
import com.example.moviemenu.database.PaymentDao
import com.example.moviemenu.database.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(private val paymentRepository: PaymentRepository) : ViewModel() {

    // State for payment details
    private val _totalPrice = MutableStateFlow<Double?>(null)
    val totalPrice: StateFlow<Double?> = _totalPrice

    private val _paymentMode = MutableStateFlow<String>("")
    val paymentMode: StateFlow<String> = _paymentMode

    // State for card details
    private val _cardNumber = MutableStateFlow<String?>(null)
    val cardNumber: StateFlow<String?> = _cardNumber

    private val _expiryMonth = MutableStateFlow<String?>(null)
    val expiryMonth: StateFlow<String?> = _expiryMonth

    private val _expiryYear = MutableStateFlow<String?>(null)
    val expiryYear: StateFlow<String?> = _expiryYear

    private val _cvv = MutableStateFlow<String?>(null)
    val cvv: StateFlow<String?> = _cvv

    private val _nameOnCard = MutableStateFlow<String?>(null)
    val nameOnCard: StateFlow<String?> = _nameOnCard

    // Function to update card details and payment info
    fun updatePaymentDetails(
        paymentMode: String,
        totalPrice: Double?,
        cardNumber: String? = null,
        expiryMonth: String? = null,
        expiryYear: String? = null,
        cvv: String? = null,
        nameOnCard: String? = null
    ) {
        viewModelScope.launch {
            _paymentMode.value = paymentMode
            _totalPrice.value = totalPrice
            _cardNumber.value = cardNumber
            _expiryMonth.value = expiryMonth
            _expiryYear.value = expiryYear
            _cvv.value = cvv
            _nameOnCard.value = nameOnCard
        }
    }

    // Function to store payment data in the database
    fun savePayment() {
        viewModelScope.launch {
            val payment = Payment(
                totalPrice = _totalPrice.value,
                paymentMode = _paymentMode.value,
                cardNumber = _cardNumber.value,
                expiryMonth = _expiryMonth.value,
                expiryYear = _expiryYear.value,
                cvv = _cvv.value,
                nameOnCard = _nameOnCard.value
            )
            paymentRepository.insertPayment(payment)
        }
    }

    // Function to reset form after a successful payment
    fun resetForm() {
        viewModelScope.launch {
            _totalPrice.value = null
            _paymentMode.value = ""
            _cardNumber.value = ""
            _expiryMonth.value = ""
            _expiryYear.value = ""
            _cvv.value = ""
            _nameOnCard.value = ""
        }
    }
}
