package com.xpeho.xpeapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Helper function to create a ViewModelProvider.Factory that creates a ViewModel using the provided initializer.
 */
inline fun <reified VM: ViewModel> viewModelFactory(crossinline initializer: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(VM::class.java)){
                @Suppress("UNCHECKED_CAST")
                return initializer() as T
            } else {
                throw IllegalArgumentException("ViewModelFactory can only create instances of ${VM::class.java}")
            }
        }
    }
}