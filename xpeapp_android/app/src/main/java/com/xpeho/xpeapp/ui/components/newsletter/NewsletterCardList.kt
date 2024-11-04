package com.xpeho.xpeapp.ui.components.newsletter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.data.model.Newsletter
import com.xpeho.xpeapp.ui.components.layout.NoContentPlaceHolder

@Composable
fun NewsletterCardList(
    newsletters: List<Newsletter>,
) {
    if (newsletters.isEmpty()) {
        // Display a placeholder if there is no newsletter
        NoContentPlaceHolder()
    } else {
        // Display the list of newsletters
        Column {
            for (newsletter in newsletters) {
                NewsletterCard(
                    newsletter = newsletter,
                    open = false
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}