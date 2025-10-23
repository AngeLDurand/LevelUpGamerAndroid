package com.example.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.levelupgamer.model.CartUiState
import com.example.levelupgamer.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(private val repo: CartRepository): ViewModel() {
    val uiState: StateFlow<CartUiState> =
        repo.flow().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CartUiState()
        )

    fun add(id: Int) = viewModelScope.launch { repo.add(id) }
    fun inc(id: Int) = viewModelScope.launch { repo.add(id, 1) }
    fun dec(id: Int) = viewModelScope.launch {
        val current = uiState.value.items.find { it.id == id }?.qty ?: 0
        repo.setQty(id, (current - 1).coerceAtLeast(0))
    }
    fun setQty(id: Int, qty: Int) = viewModelScope.launch { repo.setQty(id, qty) }
    fun remove(id: Int) = viewModelScope.launch { repo.remove(id) }
    fun clear() = viewModelScope.launch { repo.clear() }
    fun checkout(email: String?, onDone: (Int) -> Unit) =
        viewModelScope.launch {
            val id = repo.checkout(email)
            onDone(id)
        }
}