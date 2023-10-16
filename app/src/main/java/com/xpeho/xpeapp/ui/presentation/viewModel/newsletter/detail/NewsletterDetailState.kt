package com.xpeho.xpeapp.ui.presentation.viewModel.newsletter.detail

import com.xpeho.xpeapp.data.model.Newsletter

interface NewsletterDetailState {

    object LOADING : NewsletterDetailState
    data class ERROR(val message: String) : NewsletterDetailState
    data class SUCCESS(val newsletterDetail: Newsletter) : NewsletterDetailState
}
