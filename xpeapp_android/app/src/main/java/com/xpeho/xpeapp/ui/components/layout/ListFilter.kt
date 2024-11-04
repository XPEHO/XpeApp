package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun <T> ListFilter(
    elements: List<T>,
    defaultSelectedElement: T,
    onSelect: (T) -> Unit
) {
    var selectedElement by remember { mutableStateOf(defaultSelectedElement) }
    var isDropdownOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.White)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(80.dp)
                .clickable { isDropdownOpen = !isDropdownOpen }
        ) {
            ListFilterTitle(label = selectedElement.toString())
            Icon(
                painter = painterResource(
                    id = XpehoRes.chevron_down
                ),
                tint = XpehoColors.CONTENT_COLOR,
                contentDescription = "Dropdown icon",
            )
        }
        DropdownMenu(
            expanded = isDropdownOpen,
            onDismissRequest = { isDropdownOpen = false },
            modifier = Modifier
                .width(80.dp)
                .wrapContentHeight()
                .background(color = Color.White)
                .padding(0.dp)
        ) {
            elements.forEach { element ->
                if (element != selectedElement) {
                    DropdownMenuItem(
                        onClick = {
                            selectedElement = element
                            onSelect(element)
                            isDropdownOpen = false
                        },
                        text = {
                            ListFilterTitle(label = element.toString())
                        },
                        modifier = Modifier
                            .height(30.dp)
                    )
                }
            }
        }
    }
}