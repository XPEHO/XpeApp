package com.xpeho.xpeapp.ui.uiState

import com.xpeho.xpeapp.data.model.WordpressToken

interface WordpressUiState {
    object EMPTY : WordpressUiState
    object LOADING : WordpressUiState
    data class ERROR(val error: String) : WordpressUiState
    data class SUCCESS(val token: WordpressToken) : WordpressUiState
}