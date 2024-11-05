package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <E> TitleWithFilter(
    title: String,
    filterList: List<E>,
    state: MutableState<E>,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Display the title
        Title(label = title)
        // Display the filter if there is more than one year in the list
        if (filterList.size > 1) {
            ListFilter<E>(
                elements = filterList,
                defaultSelectedElement = state.value,
            ) { selectedElement ->
                state.value = selectedElement
            }
        } else {
            ListFilterTitle(label = state.value.toString())
        }
    }
}