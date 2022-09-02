package com.example.mvvmtodo.utill

sealed class UiEvent{
    object PopBackSatck: UiEvent()
    data class Navigate(val route: String): UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
}
