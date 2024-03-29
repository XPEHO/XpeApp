package com.xpeho.xpeapp.ui.uiState

interface WordpressUiState {
    object EMPTY : WordpressUiState
    object LOADING : WordpressUiState
    data class ERROR(val error: String) : WordpressUiState
    object SUCCESS : WordpressUiState
}
